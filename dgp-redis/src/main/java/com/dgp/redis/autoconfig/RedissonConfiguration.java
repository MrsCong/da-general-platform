package com.dgp.redis.autoconfig;

import cn.hutool.core.util.StrUtil;
import com.dgp.redis.aspect.RedisLockAspect;
import com.dgp.redis.delay.DelayClient;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.redisson.config.SingleServerConfig;
import org.redisson.connection.balancer.RoundRobinLoadBalancer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@SuppressWarnings("ALL")
@Configuration
@ConditionalOnProperty(prefix = "redis.redisson", name = "enabled", havingValue = "true")
public class RedissonConfiguration {

    @Resource
    private RedisConfigProperties properties;

    private static final Integer NETTY_THREADS = 6;

    /**
     * redisson客户端
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        return getRedissonClient();
    }

    @Bean
    public RedisLockAspect redisLockAspect(RedissonClient redissonClient) {
        return new RedisLockAspect(redissonClient);
    }

    @Bean
    public DelayClient redisDelayClient(RedissonClient redissonClient) {
        return new DelayClient();
    }

    private RedissonClient getRedissonClient() {
        Config config = new Config();
        String mode = properties.getMode();
        if (StringUtils.isBlank(mode)) {
            throw new RuntimeException("redission init client need config model");
        }
        //单机模式
        if (StringUtils.equals("single", mode)) {
            SingleServerConfig singleServer = config.useSingleServer();
            singleServer.setAddress(properties.getUrl());
            singleServer.setDatabase(properties.getDatabase());
            if (StrUtil.isNotEmpty(properties.getPassword())) {
                singleServer.setPassword(properties.getPassword());
            }
            config.setNettyThreads(properties.getRedisson().getNettyThreads());
            return Redisson.create(config);
        }
        //集群模式
        ClusterServersConfig clusterServers = config.useClusterServers();
        clusterServers.setScanInterval(properties.getScanInterval());
        clusterServers.addNodeAddress(properties.getUrl().split(","));
        clusterServers.setPassword(properties.getPassword());
        clusterServers.setReadMode(ReadMode.valueOf(properties.getReadModel()));
        clusterServers.setLoadBalancer(new RoundRobinLoadBalancer());
        return Redisson.create(config);
    }

}
