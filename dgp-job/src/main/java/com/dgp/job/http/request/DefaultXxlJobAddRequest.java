package com.dgp.job.http.request;

import com.dgp.job.enums.ExecutorBlockStrategyEnum;
import com.dgp.job.enums.ExecutorRouteStrategyEnum;
import com.dgp.job.enums.MisfireStrategyEnum;
import com.dgp.job.enums.ScheduleTypeEnum;
import com.xxl.job.core.glue.GlueTypeEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class DefaultXxlJobAddRequest implements Serializable {

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
     * 执行器
     */
    protected String executorHandler;

    /**
     * 执行器，任务参数
     */
    protected String executorParam;

    /**
     * GLUE类型
     */
    protected GlueTypeEnum glueType = GlueTypeEnum.BEAN;

    /**
     * 执行器路由策略
     */
    protected ExecutorRouteStrategyEnum executorRouteStrategy = ExecutorRouteStrategyEnum.FIRST;

    /**
     * 子任务ID，多个逗号分隔
     */
    protected String childJobId;

    /**
     * 调度过期策略
     */
    protected MisfireStrategyEnum misfireStrategy = MisfireStrategyEnum.DO_NOTHING;

    /**
     * 阻塞处理策略
     */
    protected ExecutorBlockStrategyEnum executorBlockStrategy = ExecutorBlockStrategyEnum.SERIAL_EXECUTION;

    /**
     * 任务执行超时时间，单位秒
     */
    protected int executorTimeout = 0;

    /**
     * 失败重试次数
     */
    protected int executorFailRetryCount = 0;

    /**
     * GLUE备注
     */
    protected String glueRemark = "glueRemark";

}
