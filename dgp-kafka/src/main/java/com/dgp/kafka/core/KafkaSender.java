package com.dgp.kafka.core;

import com.dgp.common.exception.BaseException;
import com.dgp.kafka.config.KafkaProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class KafkaSender {
    private static final Logger logger = LoggerFactory.getLogger(KafkaSender.class);
    KafkaProperties kafkaProperties;
    KafkaProducer<String, String> producer;

    public KafkaSender(KafkaProperties kafkaProperties) throws BaseException {
        this.kafkaProperties = kafkaProperties;
        initProducer();
    }

    /**
     * 初始化配置
     */
    private void initProducer() throws BaseException {
        Map<String, Object> producerConfig = new HashMap<>();
        if (StringUtils.isEmpty(kafkaProperties.getProducer().getBootstrapServers())) {
            throw new BaseException("***bootstrapServer配置为空");
        }
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getProducer().getBootstrapServers());
        if (StringUtils.isNotEmpty(kafkaProperties.getProducer().getRetries())) {
            producerConfig.put(ProducerConfig.RETRIES_CONFIG, kafkaProperties.getProducer().getRetries());
        }
        if (StringUtils.isNotEmpty(kafkaProperties.getProducer().getBatchSize())) {
            producerConfig.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProperties.getProducer().getBatchSize());
        }
        if (StringUtils.isNotEmpty(kafkaProperties.getProducer().getLinger())) {
            producerConfig.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProperties.getProducer().getLinger());
        }
        if (StringUtils.isNotEmpty(kafkaProperties.getProducer().getBufferMemory())) {
            producerConfig.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaProperties.getProducer().getBufferMemory());
        }
        if (StringUtils.isNotEmpty(kafkaProperties.getProducer().getKeySerializer())) {
            producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProperties.getProducer().getKeySerializer());
        }
        if (StringUtils.isNotEmpty(kafkaProperties.getProducer().getValueSerializer())) {
            producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProperties.getProducer().getValueSerializer());
        }
        if (StringUtils.isNotEmpty(kafkaProperties.getProducer().getPartitionerClass())) {
            producerConfig.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, kafkaProperties.getProducer().getPartitionerClass());
        }
        if (StringUtils.isNotEmpty(kafkaProperties.getProducer().getRequestTimeoutMs())) {
            producerConfig.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaProperties.getProducer().getRequestTimeoutMs());
        }
        if (StringUtils.isNotEmpty(kafkaProperties.getProducer().getAcks())) {
            producerConfig.put(ProducerConfig.ACKS_CONFIG, kafkaProperties.getProducer().getAcks());
        }
        if (StringUtils.isNotEmpty(kafkaProperties.getProducer().getMaxBlockTimeMs())) {
            producerConfig.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, kafkaProperties.getProducer().getMaxBlockTimeMs());
        }
        producer = new KafkaProducer<>(producerConfig);
    }

    public void close() {
        logger.warn("---producer close!");
        producer.flush();
        producer.close();
    }

    public boolean sendData(String topic, String content) throws BaseException {
        return sendData(topic, null, content);
    }

    /**
     * 发送数据 等待返回
     *
     * @param topic
     * @param content
     * @return
     * @throws Exception
     */
    public boolean sendData(String topic, String key, String content) throws BaseException {
        if (StringUtils.isEmpty(topic) || StringUtils.isEmpty(content)) {
            logger.warn("---topic or content is null！");
            return false;
        }
        ProducerRecord<String, String> record = null;
        if (StringUtils.isNotEmpty(key)) {
            record = new ProducerRecord<>(topic.trim(), key, content);
        } else {
            record = new ProducerRecord<>(topic.trim(), content);
        }
        RecordMetadata re;
        try {
            re = producer.send(record).get(60 * 1000, TimeUnit.MILLISECONDS);
            if (re != null && re.offset() >= 0) {
                return true;
            } else {
                logger.warn("===sendDataFail:topic:{},content:{}", topic.trim(), content);
                return false;
            }
        } catch (Exception e) {
            logger.error("***sendDataFail:topic:{},content:{}", topic.trim(), content, e);
            throw new BaseException("***sendData:" + e.getMessage());
        }
    }

    public boolean sendDataAsync(String topic, String content) throws BaseException {
        return sendDataAsync(topic, null, content);
    }

    public boolean sendDataAsync(String topic, String key, String content) throws BaseException {
        if (StringUtils.isEmpty(topic) || StringUtils.isEmpty(content)) {
            logger.warn("---topic or content is null！");
            return false;
        }
        ProducerRecord<String, String> record = null;
        if (StringUtils.isNotEmpty(key)) {
            record = new ProducerRecord<>(topic.trim(), key, content);
        } else {
            record = new ProducerRecord<>(topic.trim(), content);
        }
        try {
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception exception) {
                    if (null == exception) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("--send:{}成功,partition:{},offset:{},content:{}", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(), content);
                        }
                    } else {
                        logger.warn("--send:{}失败,partition:{},offset:{},content:{}", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(), content);

                    }

                }
            });
            return true;
        } catch (Exception e) {
            logger.error("***sendDataFail:topic:{},content:{}", topic.trim(), content, e);
            throw new BaseException("***sendData:" + e.getMessage());
        }
    }

    /**
     * 发送数据
     *
     * @param topic
     * @param content
     * @return
     * @throws Exception
     */
    public boolean sendDataNoWait(String topic, String content) throws BaseException {
        if (StringUtils.isEmpty(topic) || StringUtils.isEmpty(content)) {
            logger.warn("---topic or content is null！");
            return false;
        }
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic.trim(), content);
        try {
            producer.send(record);
            return true;
        } catch (Exception e) {
            logger.error("***sendData:topic:{},content:{}", topic.trim(), content, e);
            throw new BaseException("***sendData:" + e.getMessage());
        }
    }
}
