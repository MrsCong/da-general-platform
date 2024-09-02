package com.dgp.kafka.config;

import com.dgp.common.cache.Cache;
import com.dgp.kafka.consumer.ConsumerContext;
import com.dgp.kafka.consumer.ConsumerRunner;
import com.dgp.kafka.core.KafkaOffsetManager;
import com.dgp.kafka.core.KafkaProvider;
import com.dgp.kafka.core.KafkaSenderPool;
import com.dgp.kafka.impl.DefaultOffsetCacheImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(KafkaMarkerConfiguration.Marker.class)
@EnableConfigurationProperties(KafkaProperties.class)
@ImportAutoConfiguration({ConsumerRunner.class, ConsumerContext.class, KafkaProvider.class, KafkaOffsetManager.class})
public class KafkaAutoConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

    @ConditionalOnMissingBean
    @Bean
    public Cache getDefaultOffsetCache() {
        return new DefaultOffsetCacheImpl();
    }

    @ConditionalOnMissingBean
    @Bean
    public KafkaSenderPool getKafkaSenderPool() {
        return new KafkaSenderPool(kafkaProperties);
    }


}
