package com.dgp.job.service;

import com.dgp.job.enums.TriggerStatusEnum;
import com.dgp.job.http.request.DefaultXxlJobAddRequest;
import com.dgp.job.http.request.JobGroupQueryRequest;
import com.dgp.job.http.request.JobQueryRequest;
import com.dgp.job.http.request.JobUpdateRequest;
import com.dgp.job.http.request.XxlJobInfoAddRequest;
import com.dgp.job.http.request.XxlJobInfoRequest;
import com.dgp.job.http.response.JobGroupPageResponse;
import com.dgp.job.http.response.JobInfoPageResponse;
import com.dgp.job.http.response.item.JobInfoPageItem;

import java.util.Date;
import java.util.List;

@SuppressWarnings("unused")
public interface XxlJobService {

    /**
     * 分页查询任务数据
     *
     * @param request {@link JobQueryRequest} 任务查询请求
     * @return {@link JobInfoPageResponse} 任务分页数据
     */
    JobInfoPageResponse pageList(JobQueryRequest request);

    /**
     * 分页查询执行器数据
     *
     * @param request {@link JobGroupQueryRequest} 执行器查询请求
     * @return {@link JobGroupPageResponse} 执行器分页数据
     */
    JobGroupPageResponse pageList(JobGroupQueryRequest request);

    /**
     * 通过执行器名称查询执行器id
     *
     * @param appName {@link String} 执行器名称
     * @return {@link Integer} 执行器id
     */
    int getFirstJobGroupIdByAppName(String appName);

    /**
     * 通过配置的appName查询执行器id
     *
     * @return {@link Integer} 执行器id
     */
    int getDefaultJobGroupId();

    /**
     * 包含添加job的所有参数
     *
     * @param request {@link XxlJobInfoRequest} 任务信息
     * @return {@link Integer} 任务id
     */
    Integer add(XxlJobInfoRequest request);

    /**
     * 通过必要参数添加job，其它参数和通过网页添加job的默认参数一样
     *
     * @param request {@link XxlJobInfoAddRequest} 任务信息
     * @return {@link Integer} 任务id
     */
    Integer add(XxlJobInfoAddRequest request);

    /**
     * 通过必要参数添加job，其它参数和通过网页添加job的默认参数一样。但是可以修改默认参数
     *
     * @param request {@link DefaultXxlJobAddRequest} 任务信息
     * @return {@link Integer} 任务id
     */
    Integer add(DefaultXxlJobAddRequest request);

    /**
     * 添加只在将来执行一次的任务
     *
     * @param customId        {@link String} 自定义的唯一业务id，用于查询任务 {@link #getJobByCustomId(String)
     * @param triggerTime     {@link Date} 任务执行时间，必须大于当前时间
     * @param executorParam   {@link String} 任务参数，关联@XxlJob的param
     * @param executorHandler {@link String} 任务执行器，关联@XxlJob的value
     * @return {@link Integer} 任务id
     */
    Integer addJustExecuteOnceJob(String customId, Date triggerTime, String executorParam, String executorHandler);

    /**
     * 通过自定义的唯一业务id查询任务id
     *
     * @param customId {@link String} 自定义的唯一业务id，用于查询任务
     * @return {@link Integer} 任务id
     */
    Integer getJobIdByCustomId(String customId);

    /**
     * 通过自定义的唯一业务id查询任务
     *
     * @param customId {@link String} 自定义的唯一业务id，用于查询任务
     * @return {@link JobInfoPageItem} 任务信息
     */
    JobInfoPageItem getJobByCustomId(String customId);

    /**
     * 通过任务id查询任务
     *
     * @param jobId {@link Integer} 任务id
     * @return {@link JobInfoPageItem} 任务信息
     */
    JobInfoPageItem getJobByJobId(Integer jobId);

    /**
     * 更新任务
     *
     * @param request {@link JobUpdateRequest} 任务更新请求
     */
    void update(JobUpdateRequest request);

    /**
     * 修改任务
     *
     * @param jobInfoPageItem {@link JobInfoPageItem} 任务信息
     */
    void update(JobInfoPageItem jobInfoPageItem);

    /**
     * remove job
     * *
     *
     * @param id {@link Integer} 任务id
     */
    void remove(int id);


    /**
     * 删除符合条件的所有任务---默认的执行器
     *
     * @param triggerStatus   {@link TriggerStatusEnum} 任务状态
     * @param jobDesc         {@link String} 任务描述
     * @param executorHandler {@link String} 任务执行器
     * @param author          {@link String} 任务创建者
     */
    void remove(TriggerStatusEnum triggerStatus, String jobDesc, String executorHandler, String author);


    /**
     * 启动job
     *
     * @param id {@link Integer} 任务id
     */
    void start(int id);

    /**
     * 开始所有符合条件的任务
     *
     * @param triggerStatus   {@link TriggerStatusEnum} 任务状态
     * @param jobDesc         {@link String} 任务描述
     * @param executorHandler {@link String} 任务执行器
     * @param author          {@link String} 任务创建者
     */
    void start(TriggerStatusEnum triggerStatus, String jobDesc, String executorHandler, String author);

    /**
     * 暂停job
     *
     * @param id {@link Integer} 任务id
     */
    void stop(int id);

    /**
     * 停止所有符合条件的任务
     *
     * @param triggerStatus   {@link TriggerStatusEnum} 任务状态
     * @param jobDesc         {@link String} 任务描述
     * @param executorHandler {@link String} 任务执行器
     * @param author          {@link String} 任务创建者
     */
    void stop(TriggerStatusEnum triggerStatus, String jobDesc, String executorHandler, String author);

    /**
     * 触发一次job
     *
     * @param id            {@link Integer} 任务id
     * @param executorParam {@link String} 任务参数
     * @param addressList   {@link String} 执行器地址
     */
    void triggerJob(int id, String executorParam, String addressList);

    /**
     * 获取任务下次执行时间
     *
     * @param scheduleType {@link String} 调度类型
     * @param scheduleConf {@link String} 调度配置
     * @return {@link List<String>} 下次执行时间
     */
    List<String> nextTriggerTime(String scheduleType, String scheduleConf);

}
