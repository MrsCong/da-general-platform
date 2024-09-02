package com.dgp.kafka.consumer;

public interface ConsumerStrategy {

    String getTopic();

    void messageHandler(String message);

}
