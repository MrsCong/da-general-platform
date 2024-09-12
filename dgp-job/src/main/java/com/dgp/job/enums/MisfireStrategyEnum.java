package com.dgp.job.enums;

import lombok.Getter;

@Getter
public enum MisfireStrategyEnum {

    /**
     * 忽略错过的任务
     */
    DO_NOTHING,

    /**
     * 以错过的第一个时间立刻开始执行一次
     */
    FIRE_ONCE_NOW
}
