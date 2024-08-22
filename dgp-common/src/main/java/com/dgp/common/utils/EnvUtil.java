package com.dgp.common.utils;

import org.apache.commons.lang3.StringUtils;

public class EnvUtil {

    private static String env;

    public EnvUtil(String env) {
        EnvUtil.env = env;
    }

    public static String getEnvKafka(String topic) {
        return StringUtils.isNotEmpty(env) ? env + "_" + topic : topic;
    }

    public static String getEnv(String value) {
        return env;
    }

    public static String getEnvRedis(String key) {
        return StringUtils.isNotEmpty(env) ? env + ":" + key : key;
    }

    public static String getEnvEsIndex(String index) {
        return StringUtils.isNotEmpty(env) ? env + "-" + index : index;
    }

}
