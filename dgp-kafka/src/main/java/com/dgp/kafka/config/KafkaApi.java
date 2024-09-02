package com.dgp.kafka.config;

import com.dgp.kafka.core.KafkaSenderPool;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.List;

public interface KafkaApi {

    /**
     * 创建kafka消费者
     *
     * @param topic
     * @return
     * @throws Exception
     */
    public KafkaConsumer<String, String> createKafkaConsumer(List<String> topic, String groupId) throws Exception;

    /**
     * 获取数据
     *
     * @param consumer
     * @return
     */
    public ConsumerRecords<String, String> getData(KafkaConsumer<String, String> consumer);

    /**
     * 创建生产者
     *
     * @return
     */
    public KafkaSenderPool createKafkaSenderPool();

    /**
     * 创建生产者
     *
     * @param sendNum 发送者数据
     * @return
     */
    public KafkaSenderPool createKafkaSenderPool(int sendNum);
}
