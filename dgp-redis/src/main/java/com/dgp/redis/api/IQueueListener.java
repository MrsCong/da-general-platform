package com.dgp.redis.api;

public interface IQueueListener {

    /**
     * 消费指定队列中的消息
     *
     * @param body 消息内容
     */
    void consumer(String body);
}
