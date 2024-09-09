package com.dgp.redis.autoconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "redis")
public class RedisConfigProperties {

    private int database = 0;

    private String appName = "dgp-redis";

    private String username;

    private String password;

    private Boolean isSSL = false;

    private String mode;

    private String url;

    private int timeout = 2000;

    private int scanInterval = 2000;

    private String readModel = "SLAVE";

    private String delayQueueName;

    private Redisson redisson;


    @Data
    @ConfigurationProperties(prefix = "redis.redisson")
    public static class Redisson {

        private Boolean enabled;

        private int nettyThreads = 6;

    }

}
