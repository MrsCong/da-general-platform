package com.dgp.generator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.dgp.generator.mapper")
public class GeneratorBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(GeneratorBootstrap.class, args);
        System.out.println("===========================@@@@@@@@@@@@@@@@@@@@@@@@@@@===============================");
    }

}
