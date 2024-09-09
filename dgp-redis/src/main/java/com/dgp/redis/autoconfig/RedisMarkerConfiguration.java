package com.dgp.redis.autoconfig;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RedisConfigProperties.class)
public class RedisMarkerConfiguration {

    @Bean
    public Marker redisMarkerBean() {
        return new Marker();
    }

    class Marker {
    }

}
