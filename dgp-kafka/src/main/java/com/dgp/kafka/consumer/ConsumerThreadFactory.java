package com.dgp.kafka.consumer;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsumerThreadFactory implements ThreadFactory {

    private final AtomicInteger threadNo = new AtomicInteger(1);

    private final String nameStart;

    public ConsumerThreadFactory(String consumerGroup) {
        nameStart = "[consumer-" + consumerGroup + "-";
    }

    public Thread newThread(Runnable runnable) {
        String threadName = nameStart + threadNo.getAndIncrement() + "]";
        Thread newThread = new Thread(runnable, threadName);
        newThread.setDaemon(true);
        if (newThread.getPriority() != Thread.NORM_PRIORITY) {
            newThread.setPriority(Thread.NORM_PRIORITY);
        }
        return newThread;
    }

}
