package com.dgp.kafka.consumer;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Lazy;

import java.util.Map;
import java.util.stream.Collectors;

@Lazy
public class ConsumerContext {

    private final Map<String, ConsumerStrategy> consumerMap;

    public ConsumerContext(ObjectProvider<ConsumerStrategy> strategy) {
        consumerMap = strategy.stream().collect(Collectors.toMap(ConsumerStrategy::getTopic, key -> key));
    }

    public ConsumerStrategy getConsumerImpl(String topic) {
        return consumerMap.get(topic);
    }

}
