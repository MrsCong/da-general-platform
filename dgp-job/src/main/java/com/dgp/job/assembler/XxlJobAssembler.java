package com.dgp.job.assembler;

import com.dgp.job.enums.ExecutorBlockStrategyEnum;
import com.dgp.job.enums.ExecutorRouteStrategyEnum;
import com.dgp.job.enums.MisfireStrategyEnum;
import com.dgp.job.enums.ScheduleTypeEnum;
import com.dgp.job.enums.TriggerStatusEnum;
import com.dgp.job.http.request.DefaultXxlJobAddRequest;
import com.dgp.job.http.request.JobGroupQueryRequest;
import com.dgp.job.http.request.JobQueryRequest;
import com.dgp.job.http.request.JobUpdateRequest;
import com.dgp.job.http.request.XxlJobInfoAddRequest;
import com.dgp.job.http.request.XxlJobInfoRequest;
import com.dgp.job.http.response.item.JobInfoPageItem;
import com.dgp.job.utils.CronUtil;
import com.xxl.job.core.glue.GlueTypeEnum;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class XxlJobAssembler {

    /**
     * 转换任务查询请求参数
     *
     * @param request {@link JobQueryRequest} 任务查询请求
     * @return {@link Map} 任务查询请求参数
     */
    public static Map<String, Object> convert(JobQueryRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("start", request.getCurrent() * request.getPageSize());
        params.put("length", request.getPageSize());
        params.put("jobId", request.getJobId());
        params.put("jobGroup", request.getJobGroup());
        TriggerStatusEnum triggerStatus = request.getTriggerStatus();
        if (triggerStatus != null) {
            params.put("triggerStatus", getTriggerStatus(triggerStatus));
        }
        params.put("jobDesc", request.getJobDesc());
        params.put("executorHandler", request.getExecutorHandler());
        params.put("author", request.getAuthor());
        return params;
    }

    /**
     * 转换任务执行器查询请求参数
     *
     * @param request {@link JobGroupQueryRequest} 任务执行器查询请求
     * @return {@link Map} 任务执行器查询请求参数
     */
    public static Map<String, Object> convert(JobGroupQueryRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("start", request.getCurrent() * request.getPageSize());
        params.put("length", request.getPageSize());
        params.put("appName", request.getAppName());
        params.put("title", request.getTitle());
        return params;
    }

    /**
     * 转换任务新增请求
     *
     * @param defaultRequest {@link DefaultXxlJobAddRequest} 默认xxl-job任务新增请求
     * @return {@link XxlJobInfoRequest} xxl-job任务信息请求
     */
    public static XxlJobInfoRequest convert(DefaultXxlJobAddRequest defaultRequest) {
        XxlJobInfoRequest request = new XxlJobInfoRequest();
        ScheduleTypeEnum scheduleType = defaultRequest.getScheduleType();
        if (scheduleType != null) {
            request.setScheduleType(scheduleType);
        }
        MisfireStrategyEnum misfireStrategy = defaultRequest.getMisfireStrategy();
        if (misfireStrategy != null) {
            request.setMisfireStrategy(misfireStrategy);
        }
        ExecutorRouteStrategyEnum executorRouteStrategy = defaultRequest.getExecutorRouteStrategy();
        if (executorRouteStrategy != null) {
            request.setExecutorRouteStrategy(executorRouteStrategy);
        }
        ExecutorBlockStrategyEnum executorBlockStrategy = defaultRequest.getExecutorBlockStrategy();
        if (executorBlockStrategy != null) {
            request.setExecutorBlockStrategy(executorBlockStrategy);
        }
        GlueTypeEnum glueType = defaultRequest.getGlueType();
        if (glueType != null) {
            request.setGlueType(glueType);
        }
        BeanUtils.copyProperties(defaultRequest, request);
        return request;
    }

    /**
     * 转换任务新增请求
     *
     * @param customId        {@link String} 自定义ID
     * @param triggerTime     {@link Date} 触发时间
     * @param executorParam   {@link String} 执行参数
     * @param executorHandler {@link String} 执行器
     * @return {@link XxlJobInfoAddRequest} xxl-job任务新增请求
     */
    public static XxlJobInfoAddRequest convert(String customId, Date triggerTime, String executorParam, String executorHandler) {
        XxlJobInfoAddRequest request = new XxlJobInfoAddRequest();
        request.setJobDesc("none");
        request.setAuthor(customId);
        request.setScheduleType(ScheduleTypeEnum.CRON);

        String cron = CronUtil.getCron(triggerTime);
        request.setScheduleConf(cron);
        request.setExecutorHandler(executorHandler);
        request.setExecutorParam(executorParam);
        return request;
    }

    /**
     * 转换任务查询请求
     *
     * @param customId {@link String} 自定义ID
     * @param jobGroup {@link Integer} 任务执行器
     * @return {@link JobQueryRequest} 任务查询请求
     */
    public static JobQueryRequest convert(String customId, int jobGroup) {
        JobQueryRequest request = JobQueryRequest.builder()
                .jobGroup(jobGroup)
                .triggerStatus(TriggerStatusEnum.ALL)
                .jobDesc(customId).build();
        request.setCurrent(0);
        request.setPageSize(1);
        return request;
    }

    /**
     * 转换任务查询请求
     *
     * @param jobId    {@link Integer} 任务ID
     * @param jobGroup {@link Integer} 任务执行器
     * @return {@link JobQueryRequest} 任务查询请求
     */
    public static JobQueryRequest convert(int jobId, int jobGroup) {
        JobQueryRequest request = JobQueryRequest.builder()
                .jobGroup(jobGroup)
                .triggerStatus(TriggerStatusEnum.ALL)
                .jobId(jobId).build();
        request.setCurrent(0);
        request.setPageSize(1);
        return request;
    }

    /**
     * 转换任务更新请求
     *
     * @param item {@link JobInfoPageItem} 任务信息
     * @return {@link JobUpdateRequest} 任务更新请求
     */
    public static JobUpdateRequest convert(JobInfoPageItem item) {
        JobUpdateRequest request = new JobUpdateRequest();
        request.setScheduleConf(item.getScheduleConf());
        request.setId(item.getId());
        request.setJobGroup(item.getJobGroup());
        request.setJobDesc(item.getJobDesc());
        request.setAuthor(item.getAuthor());
        request.setAlarmEmail(item.getAlarmEmail());
        request.setScheduleType(item.getScheduleType());
        request.setMisfireStrategy(item.getMisfireStrategy());
        request.setExecutorRouteStrategy(item.getExecutorRouteStrategy());
        request.setExecutorHandler(item.getExecutorHandler());
        request.setExecutorParam(item.getExecutorParam());
        request.setExecutorBlockStrategy(item.getExecutorBlockStrategy());
        request.setExecutorTimeout(item.getExecutorTimeout());
        request.setExecutorFailRetryCount(item.getExecutorFailRetryCount());
        request.setChildJobId(item.getChildJobId());
        return request;
    }

    /**
     * 转换任务查询请求
     *
     * @param triggerStatus   {@link TriggerStatusEnum} 任务触发状态
     * @param jobDesc         {@link String} 任务描述
     * @param executorHandler {@link String} 执行器
     * @param author          {@link String} 作者
     * @return {@link JobQueryRequest} 任务查询请求
     */
    public static JobQueryRequest convert(TriggerStatusEnum triggerStatus, String jobDesc, String executorHandler, String author, int jobGroup) {
        JobQueryRequest request = JobQueryRequest.builder()
                .triggerStatus(triggerStatus)
                .jobDesc(jobDesc)
                .executorHandler(executorHandler)
                .author(author)
                .jobGroup(jobGroup).build();
        request.setCurrent(0);
        request.setPageSize(10);
        return request;
    }

    /**
     * 转换任务删除请求
     *
     * @param id            {@link Integer} 任务ID
     * @param executorParam {@link String} 执行参数
     * @param addressList   {@link String} 执行器地址列表
     * @return {@link Map} 任务参数
     */
    public static Map<String, Object> convert(int id, String executorParam, String addressList) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("executorParam", executorParam);
        params.put("addressList", addressList);
        return params;
    }

    /**
     * 获取任务触发状态
     *
     * @param status {@link TriggerStatusEnum} 任务状态
     * @return {@link Integer} 任务状态
     */
    private static Integer getTriggerStatus(TriggerStatusEnum status) {
        switch (status) {
            case ALL:
                return -1;
            case START:
                return 0;
            case STOP:
                return 1;
            default:
                return null;
        }
    }

}
