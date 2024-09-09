package com.dgp.redis.util;

import cn.hutool.extra.spring.SpringUtil;
import com.dgp.common.utils.EnvUtil;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

public class RedisUtil {

    private static final RedissonClient REDISSON_CLIENT;

    static {
        REDISSON_CLIENT = SpringUtil.getBean(RedissonClient.class);
    }

    public static void setStr(String redisKey, String value) {
        REDISSON_CLIENT.getBucket(EnvUtil.getEnvRedis(redisKey)).set(value);
    }

    public static void expireSetStr(String redisKey, String value, Integer expirationTime, TimeUnit time) {
        RBucket<String> bucket = REDISSON_CLIENT.getBucket(EnvUtil.getEnvRedis(redisKey));
        bucket.set(value, expirationTime, time);
    }

    public static String getStr(String redisKey) {
        RBucket<String> bucket = REDISSON_CLIENT.getBucket(EnvUtil.getEnvRedis(redisKey));
        return bucket.get();
    }

    public static String getStrAndDel(String redisKey) {
        RBucket<String> bucket = REDISSON_CLIENT.getBucket(EnvUtil.getEnvRedis(redisKey));
        return bucket.getAndDelete();
    }

}
