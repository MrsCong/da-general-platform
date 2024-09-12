package com.dgp.job.http.request;

import com.dgp.job.enums.ScheduleTypeEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class XxlJobInfoAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务描述
     */
    protected String jobDesc;

    /**
     * 任务创建人
     */
    protected String author;

    /**
     * 报警邮件
     */
    protected String alarmEmail;

    /**
     * 调度类型 默认CRON
     */
    protected ScheduleTypeEnum scheduleType = ScheduleTypeEnum.CRON;

    /**
     * 调度配置
     */
    protected String scheduleConf;

    /**
     * 执行器路由策略
     */
    protected String executorHandler;

    /**
     * 执行器，任务参数
     */
    protected String executorParam;

}
