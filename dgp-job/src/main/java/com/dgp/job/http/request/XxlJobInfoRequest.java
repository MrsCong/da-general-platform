package com.dgp.job.http.request;

import com.dgp.job.enums.ExecutorBlockStrategyEnum;
import com.dgp.job.enums.ExecutorRouteStrategyEnum;
import com.dgp.job.enums.MisfireStrategyEnum;
import com.dgp.job.enums.ScheduleTypeEnum;
import com.dgp.job.enums.TriggerStatusEnum;
import com.xxl.job.core.glue.GlueTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class XxlJobInfoRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    private int id;

    /**
     * 任务执行器id
     */
    private int jobGroup;

    /**
     * 任务描述
     */
    private String jobDesc;

    /**
     * 任务添加时间
     */
    private Date addTime;

    /**
     * 任务修改时间
     */
    private Date updateTime;

    /**
     * 任务创建人
     */
    private String author;

    /**
     * 报警邮件
     */
    private String alarmEmail;

    /**
     * 执行器路由策略
     */
    private ScheduleTypeEnum scheduleType;

    /**
     * 任务调度配置
     */
    private String scheduleConf;

    /**
     * 任务过期策略
     */
    private MisfireStrategyEnum misfireStrategy;

    /**
     * 执行器路由策略
     */
    private ExecutorRouteStrategyEnum executorRouteStrategy;

    /**
     * 执行器，任务Handler名称
     */
    private String executorHandler;

    /**
     * 执行器，任务参数
     */
    private String executorParam;

    /**
     * 阻塞处理策略
     */
    private ExecutorBlockStrategyEnum executorBlockStrategy;

    /**
     * 任务执行超时时间，单位秒
     */
    private int executorTimeout;

    /**
     * 失败重试次数
     */
    private int executorFailRetryCount;

    /**
     * GLUE类型
     */
    private GlueTypeEnum glueType;

    /**
     * GLUE源代码
     */
    private String glueSource;

    /**
     * GLUE备注
     */
    private String glueRemark;

    /**
     * GLUE更新时间
     */
    private Date glueUpdateTime;

    /**
     * 子任务ID，多个逗号分隔
     */
    private String childJobId;

    /**
     * 调度状态：0-停止，1-运行
     */
    private TriggerStatusEnum triggerStatus;

    /**
     * 上次调度时间
     */
    private long triggerLastTime;

    /**
     * 下次调度时间
     */
    private long triggerNextTime;

}
