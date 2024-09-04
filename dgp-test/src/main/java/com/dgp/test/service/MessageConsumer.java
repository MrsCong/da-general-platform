package com.dgp.test.service;

import com.dgp.kafka.consumer.ConsumerStrategy;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer implements ConsumerStrategy {
    @Override
    public String getTopic() {
        return "KAFKA_TEST_TOPIC";
    }

    @Override
    public void messageHandler(String message) {
        System.out.println("message: " + message);
    }
}
