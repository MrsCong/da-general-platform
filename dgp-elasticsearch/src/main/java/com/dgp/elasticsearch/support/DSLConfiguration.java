package com.dgp.elasticsearch.support;

import com.dgp.elasticsearch.config.EsConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

@Slf4j
public class DSLConfiguration extends Configuration {

    public DSLConfiguration(EsConfigProperties properties) {
        super();
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resource = resolver.getResources(properties.getDslPath());
            for (Resource rs : resource) {
                new XMLMapperBuilder(rs.getInputStream(), this, rs.toString(), this.getSqlFragments()).parse();
            }
        } catch (Exception e) {
            log.error("dsl加载异常:{}", e);
        }
    }

}
