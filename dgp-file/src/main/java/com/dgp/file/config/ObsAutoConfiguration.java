package com.dgp.file.config;

import com.dgp.file.service.IStorageService;
import com.dgp.file.service.QiNiuStorageServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ObsConfigProperties.class)
public class ObsAutoConfiguration {

    @Bean
    public IStorageService getStorageService(ObsConfigProperties properties) {
        if (StringUtils.equals(properties.getObsCode(), "QINIU")) {
            return new QiNiuStorageServiceImpl();
        }
        return null;
    }


}
