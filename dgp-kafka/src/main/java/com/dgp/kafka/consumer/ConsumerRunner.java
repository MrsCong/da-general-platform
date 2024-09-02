package com.dgp.kafka.consumer;

import com.dgp.common.utils.EnvUtil;
import com.dgp.kafka.core.KafkaOffsetManager;
import com.dgp.kafka.core.KafkaProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class ConsumerRunner implements CommandLineRunner {

    @Autowired
    private KafkaProvider kafkaProvider;

    @Autowired
    private KafkaOffsetManager kafkaOffsetManager;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run(String... args) {
        try {
            String groupId = kafkaProvider.getKafkaProperties().getConsumer().getGroupId();
            int count = kafkaProvider.getKafkaProperties().getConsumer().getCount();
            List<String> topicList = kafkaProvider.getKafkaProperties().getConsumer().getTopicList();
            if (CollectionUtils.isEmpty(topicList)) {
                return;
            }
            List<String> envTopicList = new ArrayList<>();
            for (String topic : topicList) {
                envTopicList.add(EnvUtil.getEnvKafka(topic));
            }
            ExecutorService pool = Executors.newFixedThreadPool(count, new ConsumerThreadFactory(groupId));
            for (int i = 0; i < count; i++) {
                ConsumerRunnable runnable = new ConsumerRunnable(applicationContext, kafkaOffsetManager, kafkaProvider, groupId, envTopicList);
                pool.submit(runnable);
            }
            log.info("===启动事件消费者成功===\n  {}", envTopicList);
        } catch (Exception e) {
            log.error("===启动事件消费者失败===", e);
        }
    }
}

