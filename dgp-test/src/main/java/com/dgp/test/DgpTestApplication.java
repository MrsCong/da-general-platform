package com.dgp.test;

import com.dgp.elasticsearch.config.EnableEs;
import com.dgp.kafka.config.EnableKafka;
import com.dgp.redis.autoconfig.EnableRedis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEs
@EnableKafka
@EnableRedis
@SpringBootApplication
@EnableFeignClients
public class DgpTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(DgpTestApplication.class, args);
        System.out.println("---------加载完毕------------");
    }

}