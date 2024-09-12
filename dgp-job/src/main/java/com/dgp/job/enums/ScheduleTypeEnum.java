package com.dgp.job.enums;

import lombok.Getter;

@Getter
public enum ScheduleTypeEnum {
    /**
     * 无
     */
    NONE,

    /**
     * corn表达式
     */
    CRON,

    /**
     * 固定频率
     */
    FIX_RATE
}
