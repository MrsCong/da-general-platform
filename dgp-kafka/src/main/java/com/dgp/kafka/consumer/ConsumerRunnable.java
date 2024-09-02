package com.dgp.kafka.consumer;

import com.alibaba.fastjson.JSONObject;
import com.dgp.common.context.ClientInfo;
import com.dgp.common.context.SessionContextHolder;
import com.dgp.common.context.TokenInfo;
import com.dgp.common.context.TokenUtil;
import com.dgp.kafka.core.KafkaOffsetManager;
import com.dgp.kafka.core.KafkaProvider;
import com.dgp.kafka.dto.BaseEventDto;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Objects;

@Slf4j
public class ConsumerRunnable implements Runnable {

    public static final String UID = "uid";
    private final KafkaProvider kafkaProvider;
    private final String groupId;
    private final List<String> topic;
    private final KafkaOffsetManager kafkaOffsetManager;
    private final ApplicationContext context;

    public ConsumerRunnable(ApplicationContext context, KafkaOffsetManager kafkaOffsetManager, KafkaProvider kafkaProvider, String groupId, List<String> topic) {
        this.kafkaProvider = kafkaProvider;
        this.groupId = groupId;
        this.topic = topic;
        this.kafkaOffsetManager = kafkaOffsetManager;
        this.context = context;
    }

    @Override
    public void run() {
        KafkaConsumer<String, String> consumer = kafkaProvider.createKafkaConsumer(topic, groupId);
        TokenUtil tokenUtil = context.getBean(TokenUtil.class);
        try {
            while (true) {
                try {
                    if (consumer == null) {
                        consumer = kafkaProvider.createKafkaConsumer(topic, groupId);
                    }
                    ConsumerRecords<String, String> messageRecordConsumers = kafkaProvider.getData(consumer);
                    messageRecordConsumers.forEach(record -> {
                        val queue = record.topic();
                        try {
                            ConsumerContext consumerContext = context.getBean(ConsumerContext.class);
                            val strategy = consumerContext.getConsumerImpl(queue);
                            log.info("ConsumerContext==={}, queue==={}", consumerContext, queue);
                            if (Objects.nonNull(strategy)) {
                                strategy.messageHandler(receiveBefore(record.value(), tokenUtil));
                            }
                        } catch (Exception e) {
                            log.error("===消息消费异常===queue:{},groupId:{},partition:{},offset:{},key:{},消息参数:{}", queue, groupId, record.partition(), record.offset(), record.key(), record.value(), e);

                        }
                        kafkaOffsetManager.saveOffsetInExternalStore(groupId, queue, record.partition(), record.offset());
                    });
                } catch (Exception e) {
                    log.error("***groupId：{}，事件信息获取失败:", groupId, e);
                }
            }
        } catch (Exception e) {
            log.error("***groupId：{}，事件信息获取失败2:", groupId, e);
        } finally {
            if (consumer != null) {
                try {
                    consumer.close();
                } catch (Exception e2) {
                    log.error("***consumer.close:", e2);
                }
            }
        }
    }

    private String receiveBefore(String value, TokenUtil tokenUtil) {
        BaseEventDto eventDto = JSONObject.parseObject(value, BaseEventDto.class);
        String userToken = eventDto.getUserToken();
        String clientToken = eventDto.getClientToken();
        if (StringUtils.isNotEmpty(userToken)) {
            TokenInfo tokenInfo = tokenUtil.getUserTokenInfo(userToken);
            SessionContextHolder.setTokenInfo(tokenInfo, userToken);
        }
        if (StringUtils.isNotEmpty(clientToken)) {
            ClientInfo clientInfo = tokenUtil.getClientInfo(clientToken);
            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setUserId(clientInfo.getId());
            tokenInfo.setName(clientInfo.getName());
            SessionContextHolder.setTokenInfo(tokenInfo, null);
            SessionContextHolder.setClientToken(clientToken);
        }
        return eventDto.getData();
    }

}