package com.dgp.job.http.response.item;

import com.dgp.job.enums.ExecutorBlockStrategyEnum;
import com.dgp.job.enums.ExecutorRouteStrategyEnum;
import com.dgp.job.enums.MisfireStrategyEnum;
import com.dgp.job.enums.ScheduleTypeEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class JobInfoPageItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 最后一次调度时间
     */
    private Long triggerLastTime;

    /**
     * 报警邮件
     */
    private String alarmEmail;

    /**
     * Glue更新时间
     */
    private String glueUpdateTime;

    /**
     * 执行器参数
     */
    private String executorParam;

    /**
     * 添加时间
     */
    private String addTime;

    /**
     * 阻塞策略
     */
    private ExecutorBlockStrategyEnum executorBlockStrategy;

    /**
     * 创建人
     */
    private String author;

    /**
     * 调度配置
     */
    private String scheduleConf;

    /**
     * 路由策略
     */
    private ExecutorRouteStrategyEnum executorRouteStrategy;

    /**
     * 触发状态
     */
    private Long triggerStatus;

    /**
     * 子任务ID
     */
    private Integer childJobId;

    /**
     * 下次触发时间
     */
    private long triggerNextTime;


    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 任务组
     */
    private int jobGroup;

    /**
     * Glue备注
     */
    private String glueRemark;

    /**
     * 任务描述
     */
    private String jobDesc;

    /**
     * Glue源代码
     */
    private String glueSource;

    /**
     * 任务过期策略
     */
    private MisfireStrategyEnum misfireStrategy;

    /**
     * 调度类型
     */
    private ScheduleTypeEnum scheduleType;

    /**
     * Glue类型
     */
    private String glueType;

    /**
     * 执行器
     */
    private String executorHandler;

    /**
     * 失败重试次数
     */
    private int executorFailRetryCount;

    /**
     * 任务ID
     */
    private int id;

    /**
     * 执行器超时时间
     */
    private int executorTimeout;

}
