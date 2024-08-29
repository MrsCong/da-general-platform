package com.dgp.test;

import com.dgp.elasticsearch.config.EnableEs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableEs
@SpringBootApplication
public class DgpTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(DgpTestApplication.class, args);
    }

}