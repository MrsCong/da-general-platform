package com.dgp.job.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.PageUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dgp.core.utils.BeanCopierUtil;
import com.dgp.job.assembler.XxlJobAssembler;
import com.dgp.job.config.XxlJobAdminProperties;
import com.dgp.job.enums.TriggerStatusEnum;
import com.dgp.job.http.HttpHeader;
import com.dgp.job.http.request.DefaultXxlJobAddRequest;
import com.dgp.job.http.request.JobGroupQueryRequest;
import com.dgp.job.http.request.JobQueryRequest;
import com.dgp.job.http.request.JobUpdateRequest;
import com.dgp.job.http.request.XxlJobInfoAddRequest;
import com.dgp.job.http.request.XxlJobInfoRequest;
import com.dgp.job.http.response.JobGroupPageResponse;
import com.dgp.job.http.response.JobInfoPageResponse;
import com.dgp.job.http.response.item.DataItem;
import com.dgp.job.http.response.item.JobInfoPageItem;
import com.dgp.job.service.XxlJobService;
import com.xxl.job.core.biz.model.ReturnT;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dgp.job.constants.XxlJobRequestPathConstants.JOB_ADD_PATH;
import static com.dgp.job.constants.XxlJobRequestPathConstants.JOB_DELETE_PATH;
import static com.dgp.job.constants.XxlJobRequestPathConstants.JOB_GROUP_LIST_PATH;
import static com.dgp.job.constants.XxlJobRequestPathConstants.JOB_NEXT_TRIGGER_TIME_PATH;
import static com.dgp.job.constants.XxlJobRequestPathConstants.JOB_PAGE_LIST_PATH;
import static com.dgp.job.constants.XxlJobRequestPathConstants.JOB_START_PATH;
import static com.dgp.job.constants.XxlJobRequestPathConstants.JOB_STOP_PATH;
import static com.dgp.job.constants.XxlJobRequestPathConstants.JOB_TRIGGER_PATH;
import static com.dgp.job.constants.XxlJobRequestPathConstants.JOB_UPDATE_PATH;


@Slf4j
public class XxlJobServiceImpl implements XxlJobService {

    /**
     * 登录请求头
     */
    private final HttpHeader loginHeader;

    /**
     * xxl-job配置
     */
    private final XxlJobAdminProperties properties;

    /**
     * 超时时间
     */
    private final int timeout;

    /**
     * 构造函数
     * @param header {@link HttpHeader} 登录请求头
     * @param properties {@link XxlJobAdminProperties} xxl-job配置
     */
    public XxlJobServiceImpl(HttpHeader header, XxlJobAdminProperties properties) {
        this.loginHeader = header;
        this.properties = properties;
        this.timeout = this.properties.getConnectionTimeOut();
    }

    /**
     * 分页查询任务数据
     * @param request {@link JobQueryRequest} 任务查询请求
     * @return {@link JobInfoPageResponse} 任务分页数据
     */
    @Override
    public JobInfoPageResponse pageList(JobQueryRequest request) {
        this.valid(request);
        HttpRequest httpRequest = this.getHttpRequest(JOB_PAGE_LIST_PATH);
        Map<String, Object> params = XxlJobAssembler.convert(request);
        return (JobInfoPageResponse) getResponse(httpRequest, params, JobInfoPageResponse.class);
    }

    /**
     * 分页查询执行器数据
     * @param request {@link JobGroupQueryRequest} 执行器查询请求
     * @return {@link JobGroupPageResponse} 执行器分页数据
     */
    @Override
    public JobGroupPageResponse pageList(JobGroupQueryRequest request) {
        this.valid(request);
        HttpRequest httpRequest = this.getHttpRequest(JOB_GROUP_LIST_PATH);
        Map<String, Object> params = XxlJobAssembler.convert(request);
        return (JobGroupPageResponse) getResponse(httpRequest, params, JobGroupPageResponse.class);
    }

    /**
     * 通过执行器名称查询执行器id
     * @param appName {@link String} 执行器名称
     * @return {@link Integer} 执行器id
     */
    @Override
    public int getFirstJobGroupIdByAppName(String appName) {
        JobGroupQueryRequest request = JobGroupQueryRequest.builder().appName(appName).build();
        request.setCurrent(0);
        request.setPageSize(1);
        JobGroupPageResponse response = this.pageList(request);
        String errorMsgTemplate = "查询结果为空，请检查是否创建对应名称的执行器";
        Assert.notNull(response, errorMsgTemplate);
        List<DataItem> data = response.getData();
        Assert.isTrue(CollUtil.isNotEmpty(data), errorMsgTemplate);
        DataItem dataItem = data.get(0);
        return dataItem.getId();
    }

    /**
     * 通过配置的appName查询执行器id
     * @return {@link Integer} 执行器id
     */
    @Override
    public int getDefaultJobGroupId() {
        String appName = properties.getAppname();
        return this.getFirstJobGroupIdByAppName(appName);
    }

    /**
     * 包含添加job的所有参数
     * @param request {@link XxlJobInfoRequest} 任务信息
     * @return {@link Integer} 任务id
     */
    @Override
    public Integer add(XxlJobInfoRequest request) {
        HttpRequest httpRequest = postHttpRequest(JOB_ADD_PATH);
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(request));
        ReturnT<String> returnT = getResponse(httpRequest, jsonObject, new TypeReference<ReturnT<String>>() {});
        return Integer.valueOf(returnT.getContent());
    }

    /**
     * 通过必要参数添加job，其它参数和通过网页添加job的默认参数一样
     * @param request {@link XxlJobInfoAddRequest} 任务信息
     * @return {@link Integer} 任务id
     */
    @Override
    public Integer add(XxlJobInfoAddRequest request) {
        DefaultXxlJobAddRequest defaultRequest = new DefaultXxlJobAddRequest();
        BeanCopierUtil.copyProperties(request, defaultRequest);
        return this.add(defaultRequest);
    }

    /**
     * 通过必要参数添加job，其它参数和通过网页添加job的默认参数一样。但是可以修改默认参数
     * @param defaultRequest {@link DefaultXxlJobAddRequest} 任务信息
     * @return {@link Integer} 任务id
     */
    @Override
    public Integer add(DefaultXxlJobAddRequest defaultRequest) {
        XxlJobInfoRequest request = XxlJobAssembler.convert(defaultRequest);
        request.setJobGroup(this.getDefaultJobGroupId());
        this.valid(request);
        return this.add(request);
    }

    /**
     * 添加只在将来执行一次的任务
     * @param customId        {@link String} 自定义的唯一业务id，用于查询任务 {@link #getJobByCustomId(String)
     * @param triggerTime     {@link Date } 任务执行时间，必须大于当前时间
     * @param executorParam   {@link String} 任务参数，关联@XxlJob的param
     * @param executorHandler {@link String} 任务执行器，关联@XxlJob的value
     * @param triggerTime     {@link Date } 任务执行时间，必须大于当前时间
     * @param executorParam   {@link String} 任务参数，关联@XxlJob的param
     * @param executorHandler {@link String} 任务执行器，关联@XxlJob的value
     * @return {@link Integer} 任务id
     */
    @Override
    public Integer addJustExecuteOnceJob(String customId, Date triggerTime, String executorParam, String executorHandler) {
        JobInfoPageItem jobInfoPageItem = this.getJobByCustomId(customId);
        Assert.isNull(jobInfoPageItem,"已经存在自定义id为{}的任务，请修改", customId);
        boolean after = triggerTime.after(new Date());
        Assert.isTrue(after,"任务执行时间必须大于当前时间");
        XxlJobInfoAddRequest request = XxlJobAssembler.convert(customId, triggerTime, executorParam, executorHandler);
        return this.add(request);
    }

    /**
     * 通过自定义的唯一业务id查询任务id
     * @param customId {@link String} 自定义的唯一业务id，用于查询任务
     * @return {@link Integer} 任务id
     */
    @Override
    public Integer getJobIdByCustomId(String customId) {
        JobInfoPageItem jobInfoPageItem = this.getJobByCustomId(customId);
        return jobInfoPageItem == null ? null : jobInfoPageItem.getId();
    }

    /**
     * 通过自定义的唯一业务id查询任务
     * @param customId {@link String} 自定义的唯一业务id，用于查询任务
     * @return {@link JobInfoPageItem} 任务信息
     */
    @Override
    public JobInfoPageItem getJobByCustomId(String customId) {
        int jobGroup = this.getDefaultJobGroupId();
        JobQueryRequest request = XxlJobAssembler.convert(customId, jobGroup);
        JobInfoPageResponse response = this.pageList(request);
        List<JobInfoPageItem> data = response.getData();
        if (CollUtil.isEmpty(data)) {
            return null;
        }
        return data.get(0);
    }

    /**
     * 通过任务id查询任务
     * @param jobId {@link Integer} 任务id
     * @return {@link JobInfoPageItem} 任务信息
     */
    @Override
    public JobInfoPageItem getJobByJobId(Integer jobId) {
        int jobGroup = this.getDefaultJobGroupId();
        JobQueryRequest request = XxlJobAssembler.convert(jobId, jobGroup);
        JobInfoPageResponse response = this.pageList(request);
        List<JobInfoPageItem> data = response.getData();
        if (CollUtil.isEmpty(data)) {
            return null;
        }
        return data.get(0);
    }

    /**
     * 更新任务
     * @param request {@link JobUpdateRequest} 任务更新请求
     */
    @Override
    public void update(JobUpdateRequest request) {
        HttpRequest httpRequest = postHttpRequest(JOB_UPDATE_PATH);
        request.setJobGroup(this.getDefaultJobGroupId());
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(request));
        getResponse(httpRequest, jsonObject, new TypeReference<ReturnT<String>>() {});
    }

    /**
     * 修改任务
     * @param jobInfoPageItem {@link JobInfoPageItem} 任务信息
     */
    @Override
    public void update(JobInfoPageItem jobInfoPageItem) {
        this.update(XxlJobAssembler.convert(jobInfoPageItem));
    }

    /**
     * 移除任务
     * @param id {@link Integer} 任务id
     */
    @Override
    public void remove(int id) {
        HttpRequest httpRequest = postHttpRequest(JOB_DELETE_PATH);
        Map<String,Object> params = new HashMap<>();
        params.put("id",id);
        getResponse(httpRequest, params, new TypeReference<ReturnT<String>>() {});
    }

    /**
     * 删除符合条件的所有任务---默认的执行器
     * @param triggerStatus   {@link TriggerStatusEnum} 任务状态
     * @param jobDesc         {@link String} 任务描述
     * @param executorHandler {@link String} 任务执行器
     * @param author          {@link String} 任务创建者
     */
    @Override
    public void remove(TriggerStatusEnum triggerStatus, String jobDesc, String executorHandler, String author) {
        int jobGroup = this.getDefaultJobGroupId();
        JobQueryRequest request = XxlJobAssembler.convert(triggerStatus, jobDesc, executorHandler, author, jobGroup);
        JobInfoPageResponse response = this.pageList(request);
        List<JobInfoPageItem> data = response.getData();
        if (CollUtil.isEmpty(data)) {
            return;
        }
        data.forEach(item -> this.remove(item.getId()));

        int recordsTotal = response.getRecordsTotal();
        int totalPage = PageUtil.totalPage(recordsTotal, request.getPageSize());
        for (int i = 1; i < totalPage; i++) {
            response = this.pageList(request);
            response.getData().forEach(item -> this.remove(item.getId()));
        }
    }

    /**
     * 启动job
     * @param id {@link Integer} 任务id
     */
    @Override
    public void start(int id) {
        HttpRequest httpRequest = postHttpRequest(JOB_START_PATH);
        Map<String,Object> params = new HashMap<>();
        params.put("id",id);
        getResponse(httpRequest, params, new TypeReference<ReturnT<String>>() {});
    }

    /**
     * 开始所有符合条件的任务
     * @param triggerStatus   {@link TriggerStatusEnum} 任务状态
     * @param jobDesc         {@link String} 任务描述
     * @param executorHandler {@link String} 任务执行器
     * @param author          {@link String} 任务创建者
     */
    @Override
    public void start(TriggerStatusEnum triggerStatus, String jobDesc, String executorHandler, String author) {
        int jobGroup = this.getDefaultJobGroupId();
        JobQueryRequest request = XxlJobAssembler.convert(triggerStatus, jobDesc, executorHandler, author, jobGroup);
        JobInfoPageResponse response = this.pageList(request);
        List<JobInfoPageItem> data = response.getData();
        if (CollUtil.isEmpty(data)) {
            return;
        }
        data.forEach(item -> this.start(item.getId()));
        int recordsTotal = response.getRecordsTotal();
        int totalPage = PageUtil.totalPage(recordsTotal, request.getPageSize());
        for (int i = 1; i < totalPage; i++) {
            request.setCurrent(i);
            response = this.pageList(request);
            response.getData().forEach(item -> this.start(item.getId()));
        }
    }

    /**
     * 暂停job
     * @param id {@link Integer} 任务id
     */
    @Override
    public void stop(int id) {
        HttpRequest httpRequest = postHttpRequest(JOB_STOP_PATH);
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        getResponse(httpRequest, params, new TypeReference<ReturnT<String>>() {});
    }

    /**
     * 停止所有符合条件的任务
     * @param triggerStatus   {@link TriggerStatusEnum} 任务状态
     * @param jobDesc         {@link String} 任务描述
     * @param executorHandler {@link String} 任务执行器
     * @param author          {@link String} 任务创建者
     */
    @Override
    public void stop(TriggerStatusEnum triggerStatus, String jobDesc, String executorHandler, String author) {
        int jobGroup =  this.getDefaultJobGroupId();
        JobQueryRequest request = XxlJobAssembler.convert(triggerStatus, jobDesc, executorHandler, author, jobGroup);
        JobInfoPageResponse response = this.pageList(request);
        List<JobInfoPageItem> data = response.getData();
        if (CollUtil.isEmpty(data)) {
            return;
        }
        data.forEach(item -> this.stop(item.getId()));

        int recordsTotal = response.getRecordsTotal();
        int totalPage = PageUtil.totalPage(recordsTotal, request.getPageSize());
        for (int i = 1; i < totalPage; i++) {
            request.setCurrent(i);
            response = this.pageList(request);

            data = response.getData();
            data.forEach(item -> this.stop(item.getId()));
        }
    }

    /**
     * 触发一次job
     * @param id            {@link Integer} 任务id
     * @param executorParam {@link String} 任务参数
     * @param addressList   {@link String} 执行器地址
     */
    @Override
    public void triggerJob(int id, String executorParam, String addressList) {
        HttpRequest httpRequest = postHttpRequest(JOB_TRIGGER_PATH);
        Map<String, Object> params = XxlJobAssembler.convert(id, executorParam, addressList);
        getResponse(httpRequest, params, new TypeReference<ReturnT<String>>() {});
    }

    /**
     * 获取任务下次执行时间
     * @param scheduleType {@link String} 调度类型
     * @param scheduleConf {@link String} 调度配置
     * @return {@link List <String>} 下次执行时间
     */
    @Override
    public List<String> nextTriggerTime(String scheduleType, String scheduleConf) {
         HttpRequest httpRequest = getHttpRequest(JOB_NEXT_TRIGGER_TIME_PATH);
        Map<String,Object> map = new HashMap<>();
        map.put("scheduleType",scheduleType);
        map.put("scheduleConf",scheduleConf);
        ReturnT<List<String>> returnT = getResponse(httpRequest, map,new TypeReference<ReturnT<List<String>>>(){});
        return returnT.getContent();
    }

    /**
     * 任务查询参数验证
     * @param request {@link JobQueryRequest} 任务查询请求
     */
    private void valid(JobQueryRequest request) {
        int jobGroup = request.getJobGroup();
        Assert.notNull(jobGroup, "jobGroup can not be null");
        int page = request.getCurrent();
        Assert.isTrue(page >= 0, "page must be greater than or equal to 0");
        int size = request.getPageSize();
        Assert.isTrue(size >= 0, "size must be greater than or equal to 0");
        TriggerStatusEnum triggerStatus = request.getTriggerStatus();
        Assert.notNull(triggerStatus, "triggerStatus can not be null");
    }

    /**
     * 任务执行器查询参数验证
     * @param request {@link JobGroupQueryRequest} 任务执行器查询请求
     */
    private void valid(JobGroupQueryRequest request) {
        int page = request.getCurrent();
        Assert.isTrue(page >= 0, "page must be greater than or equal to 0");
        int size = request.getPageSize();
        Assert.isTrue(size >= 0, "size must be greater than or equal to 0");
    }

    /**
     * 任务信息查询参数验证
     * @param request {@link XxlJobInfoRequest} 任务信息请求
     */
    private void valid(XxlJobInfoRequest request) {
        Assert.notNull(request,"参数不能为null");
        Assert.notNull(request.getJobGroup(),"jobGroup参数不能为null");
        Assert.notNull(request.getJobDesc(),"jobDesc参数不能为null");
        Assert.notNull(request.getAuthor(),"author参数不能为null");
        Assert.notNull(request.getScheduleType(),"scheduleType参数不能为null");
        Assert.notNull(request.getGlueType(),"glueType参数不能为null");
        Assert.notNull(request.getExecutorHandler(),"executorHandler参数不能为null");
    }

    /**
     * GET请求
     * @param path {@link String} 请求路径
     * @return {@link HttpRequest} 请求
     */
    private HttpRequest getHttpRequest(String path) {
        return HttpRequest.get(properties.getAdminUrl() + path)
                .header(loginHeader.getHeaderName(), loginHeader.getHeaderValue());
    }

    /**
     * POST请求
     * @param path {@link String} 请求路径
     * @return {@link HttpRequest} 请求
     */
    private HttpRequest postHttpRequest(String path) {
        return HttpRequest.post(properties.getAdminUrl() + path)
                .header(loginHeader.getHeaderName(), loginHeader.getHeaderValue());
    }

    /**
     * 获取响应
     * @param request {@link HttpRequest} 请求
     * @param params {@link Map} 请求参数
     * @param clazz {@link Class} 返回类型
     * @return {@link Object} 返回结果
     */
    private Object getResponse(HttpRequest request, Map<String, Object> params , Class<?> clazz) {
        HttpResponse response = request.form(params).timeout(timeout).execute();
        int status = response.getStatus();
        String body = response.body();
        log.debug("status:{}, body:{}", status, body);
        Assert.isTrue(status == 200, body);
        return JSON.parseObject(body, clazz);
    }

    /**
     * 获取响应
     * @param request {@link HttpRequest} 请求
     * @param params {@link Map} 请求参数
     * @param type {@link TypeReference<T>} 返回类型
     * @param <T> {@link ReturnT} 返回类型
     * @return {@link ReturnT} 返回结果
     */
    private <T extends ReturnT<?>> T getResponse(HttpRequest request, Map<String, Object> params, TypeReference<T> type) {
        HttpResponse response = request.form(params).timeout(timeout).execute();
        int status = response.getStatus();
        String body = response.body();
        log.debug("status:{}, body:{}", status, body);
        Assert.isTrue(status == 200, body);
        T returnT = JSON.parseObject(body, type);
        Assert.isTrue(status == 200, body);
        int code = returnT.getCode();
        log.debug("returnT:{}",returnT);
        Assert.isTrue(code == 200, returnT.getMsg());
        return returnT;
    }
}
