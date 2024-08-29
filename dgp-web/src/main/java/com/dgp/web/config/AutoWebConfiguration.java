package com.dgp.web.config;

import com.dgp.common.utils.EnvUtil;
import com.dgp.web.utils.SnowflakeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ImportAutoConfiguration({JacksonConfig.class})
public class AutoWebConfiguration {

    @Value("${spring.profiles.active:default}")
    private String env;

    @Bean
    public GlobalExceptionHandler getGlobalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    public SnowflakeUtil getSnowflakeUtil() {
        return new SnowflakeUtil();
    }

    @Bean
    public EnvUtil getEnvUtil() {
        return new EnvUtil(env);
    }

}
