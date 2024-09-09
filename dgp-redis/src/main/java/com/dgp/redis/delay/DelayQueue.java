package com.dgp.redis.delay;

import cn.hutool.core.lang.Pair;
import com.dgp.redis.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("ALL")
@Slf4j
public class DelayQueue extends AbstractQueue {

    private RDelayedQueue<Pair<String, String>> delayedQueue;
    private RBlockingDeque<Pair<String, String>> blockingDeque;

    public DelayQueue(String name, RDelayedQueue<Pair<String, String>> delayedQueue,
                      RBlockingDeque<Pair<String, String>> blockingDeque) {
        super(name);
        this.blockingDeque = blockingDeque;
        this.delayedQueue = delayedQueue;
    }

    public static DelayQueue create(String name) {
        RBlockingDeque<Pair<String, String>> blockingDeque = redissonClient.getBlockingDeque(name);
        RDelayedQueue<Pair<String, String>> delayedQueue = redissonClient.getDelayedQueue(
                blockingDeque);
        return new DelayQueue(name, delayedQueue, blockingDeque);
    }

    public <T> void offer(String topic, T body, long delay) {
        delayedQueue.offer(Pair.of(topic, ReflectUtil.transfer(body)), delay, TimeUnit.SECONDS);
    }

    public <T> void remove(String topic, T body) {
        delayedQueue.remove(Pair.of(topic, ReflectUtil.transfer(body)));
    }

    public <T> Boolean contains(String topic, T body) {
        return delayedQueue.contains(Pair.of(topic, ReflectUtil.transfer(body)));
    }

    @Override
    protected Pair<String, String> take() throws InterruptedException {
        return blockingDeque.take();
    }

}
