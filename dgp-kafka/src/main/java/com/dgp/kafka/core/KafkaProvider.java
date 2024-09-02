package com.dgp.kafka.core;

import com.dgp.common.exception.BaseException;
import com.dgp.kafka.config.KafkaApi;
import com.dgp.kafka.config.KafkaProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class KafkaProvider implements KafkaApi {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProvider.class);
    @Autowired
    KafkaProperties kafkaProperties;
    @Autowired
    KafkaOffsetManager offsetManager;

    /**
     * 创建kafka消费者
     *
     * @param topic
     * @return
     * @throws BaseException
     */
    public KafkaConsumer<String, String> createKafkaConsumer(final List<String> topic, final String groupId) throws BaseException {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(getConsumerConfig(groupId));
        consumer.subscribe(topic, new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                if (kafkaProperties.isRebalanced()) {
                    for (TopicPartition partition : partitions) {
                        offsetManager.saveOffsetInExternalStore(groupId, partition.topic(), partition.partition(), consumer.position(partition));
                        logger.info("---saveOffsetInExternalStore: groupId:{}topic:{},partition:{},offset:{}", groupId, partition.topic(), partition.partition(), consumer.position(partition));
                    }
                }
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {

                logger.info("---readOffsetFromExternalStore: groupId:{}，isRebalanced：{}", groupId, kafkaProperties.isRebalanced());
                if (kafkaProperties.isRebalanced()) {
                    for (TopicPartition partition : partitions) {
                        long offset = offsetManager.readOffsetFromExternalStore(groupId, partition.topic(), partition.partition());
                        if (offset >= 0) {
                            consumer.seek(partition, offset);
                            logger.info("---readOffsetFromExternalStore: groupId:{},topic:{},partition:{},offset:{}--{}", groupId, partition.topic(), partition.partition(), consumer.position(partition), offset);
                        }

                    }
                }
            }
        });
        return consumer;
    }

    /**
     * 获取数据
     *
     * @param consumer
     * @return
     */
    public ConsumerRecords<String, String> getData(KafkaConsumer<String, String> consumer) throws BaseException {
        return consumer.poll(Duration.ofMillis(kafkaProperties.getConsumer().getPollTimeOut()));
    }

    /**
     * 创建生产者
     *
     * @return
     * @throws Exception
     */
    public KafkaSenderPool createKafkaSenderPool() throws BaseException {
        return new KafkaSenderPool(kafkaProperties);
    }

    /**
     * 创建生产者
     *
     * @param sendNum 发送者数据
     * @return
     */
    public KafkaSenderPool createKafkaSenderPool(int sendNum) throws BaseException {
        return new KafkaSenderPool(kafkaProperties, sendNum);
    }

    /**
     * 初始化配置
     *
     * @return
     * @throws Exception
     */
    private Map<String, Object> getConsumerConfig(String groupId) throws BaseException {
        if (StringUtils.isEmpty(kafkaProperties.getConsumer().getBootstrapServers())) {
            throw new BaseException("***bootstrapServer配置为空");
        }
        if (StringUtils.isEmpty(kafkaProperties.getConsumer().getGroupId())) {
            throw new BaseException("***groupIdr配置为空");
        }
        Map<String, Object> consumerConfig = new HashMap<>();
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getConsumer().getBootstrapServers());
        if (StringUtils.isNotEmpty(groupId)) {
            consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        } else {
            consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumer().getGroupId());
        }

        // 手动提交
        consumerConfig.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaProperties.getConsumer().isEnableAutoCommit());

        if (StringUtils.isNotEmpty(kafkaProperties.getConsumer().getMaxPollRecords())) {
            consumerConfig.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaProperties.getConsumer().getMaxPollRecords());
        }
        if (StringUtils.isNotEmpty(kafkaProperties.getConsumer().getAutoOffsetReset())) {
            consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperties.getConsumer().getAutoOffsetReset());
        }
        if (StringUtils.isNotEmpty(kafkaProperties.getConsumer().getMaxPollInterval())) {
            consumerConfig.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, kafkaProperties.getConsumer().getMaxPollInterval());
        }
        if (StringUtils.isNotEmpty(kafkaProperties.getConsumer().getAutoCommitInterval())) {
            consumerConfig.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, Integer.valueOf(kafkaProperties.getConsumer().getAutoCommitInterval()));
        }
        if (StringUtils.isNotEmpty(kafkaProperties.getConsumer().getMaxPartitionFetchSize())) {
            consumerConfig.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, kafkaProperties.getConsumer().getMaxPartitionFetchSize());
        }
        if (StringUtils.isNotEmpty(kafkaProperties.getConsumer().getHeartbeatInterval())) {
            consumerConfig.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, kafkaProperties.getConsumer().getHeartbeatInterval());
        }
        if (StringUtils.isNotEmpty(kafkaProperties.getConsumer().getSessionTimeout())) {
            consumerConfig.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaProperties.getConsumer().getSessionTimeout());
        }
        if (StringUtils.isNotEmpty(kafkaProperties.getConsumer().getFetchMaxWait())) {
            consumerConfig.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, kafkaProperties.getConsumer().getFetchMaxWait());
        }
        if (StringUtils.isNotEmpty(kafkaProperties.getConsumer().getFetchMinSize())) {
            consumerConfig.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, kafkaProperties.getConsumer().getFetchMinSize());
        }
        if (StringUtils.isNotEmpty(kafkaProperties.getConsumer().getFetchMaxSize())) {
            consumerConfig.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, kafkaProperties.getConsumer().getFetchMaxSize());
        }
        if (StringUtils.isNotEmpty(kafkaProperties.getConsumer().getKeyDeserializer())) {
            consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getConsumer().getKeyDeserializer());
        }
        if (StringUtils.isNotEmpty(kafkaProperties.getConsumer().getValueDeserializer())) {
            consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getConsumer().getValueDeserializer());
        }
        logger.info("----consumerConfig: {}", consumerConfig);
        return consumerConfig;
    }

    public KafkaProperties getKafkaProperties() {
        return kafkaProperties;
    }

}
