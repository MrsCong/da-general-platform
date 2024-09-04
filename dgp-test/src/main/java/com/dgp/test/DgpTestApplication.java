package com.dgp.test;

import com.dgp.elasticsearch.config.EnableEs;
import com.dgp.kafka.config.EnableKafka;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableEs
@EnableKafka
@SpringBootApplication
public class DgpTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(DgpTestApplication.class, args);
    }

}