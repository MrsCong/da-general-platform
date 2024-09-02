package com.dgp.kafka.core;

import com.alibaba.fastjson.JSONObject;
import com.dgp.common.context.SessionContextHolder;
import com.dgp.common.exception.BaseException;
import com.dgp.kafka.config.KafkaProperties;
import com.dgp.kafka.dto.BaseEventDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

public class KafkaSenderPool {

    private static final Logger logger = LoggerFactory.getLogger(KafkaSenderPool.class);
    KafkaProperties kafkaProperties;
    KafkaSender[] kafkaSenders;
    private int count;
    private ThreadLocalRandom ran = ThreadLocalRandom.current();

    public KafkaSenderPool(KafkaProperties kafkaProperties) throws BaseException {
        this.kafkaProperties = kafkaProperties;
        this.count = kafkaProperties.getProducer().getCount();
        if (!kafkaProperties.isEnable()) {
            logger.info("---KafkaSenderPool: kafka isEnable:{}", kafkaProperties.isEnable());
            return;
        }
        if (count <= 0) {
            count = 1;
        }
        kafkaSenders = new KafkaSender[count];
        logger.info("---KafkaSenderPool: pool.size:{}", count);
        for (int i = 0; i < count; i++) {
            kafkaSenders[i] = new KafkaSender(kafkaProperties);
        }
    }

    public KafkaSenderPool(KafkaProperties kafkaProperties, int count) throws BaseException {
        this.count = count;
        this.kafkaProperties = kafkaProperties;
        if (!kafkaProperties.isEnable()) {
            logger.info("---KafkaSenderPool: kafka isEnable:{}", kafkaProperties.isEnable());
            return;
        }
        if (count <= 0) {
            count = 1;
        }
        kafkaSenders = new KafkaSender[count];
        logger.info("---KafkaSenderPool: pool.size:{}", count);
        for (int i = 0; i < count; i++) {
            kafkaSenders[i] = new KafkaSender(kafkaProperties);
        }
    }

    public boolean send(String topic, Object content) throws BaseException {
        return kafkaSenders[getNum()].sendData(topic, sendBefore(content));
    }

    public boolean sendAsync(String topic, Object content) throws BaseException {
        return kafkaSenders[getNum()].sendDataAsync(topic, sendBefore(content));
    }

    public boolean send(String topic, String key, Object content) throws BaseException {
        return kafkaSenders[getNum()].sendData(topic, key, sendBefore(content));
    }

    public boolean sendAsync(String topic, String key, Object content) throws BaseException {
        return kafkaSenders[getNum()].sendDataAsync(topic, key, sendBefore(content));
    }

    public boolean sendNoWait(String topic, Object content) throws BaseException {
        return kafkaSenders[getNum()].sendDataNoWait(topic, sendBefore(content));
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getNum() {
        return ran.nextInt(getCount());
    }

    private String sendBefore(Object object) {
        BaseEventDto baseEventDto = new BaseEventDto();
        baseEventDto.setClientToken(SessionContextHolder.getClientToken());
        baseEventDto.setUserToken(SessionContextHolder.getUserToken());
        baseEventDto.setData(JSONObject.toJSONString(object));
        return JSONObject.toJSONString(baseEventDto);
    }
}
