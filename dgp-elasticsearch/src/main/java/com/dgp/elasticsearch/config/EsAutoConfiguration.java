package com.dgp.elasticsearch.config;

import com.dgp.elasticsearch.connection.EsDataSource;
import com.dgp.elasticsearch.connection.impl.EsDataSourceImpl;
import com.dgp.elasticsearch.support.ServiceBeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(EsMarkerConfiguration.Marker.class)
@EnableConfigurationProperties(EsConfigProperties.class)
public class EsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public EsDataSource getEsDataSource(EsConfigProperties properties) {
        return new EsDataSourceImpl(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public ServiceBeanDefinitionRegistry getServiceBeanDefinitionRegistry() {
        return new ServiceBeanDefinitionRegistry();
    }

}
