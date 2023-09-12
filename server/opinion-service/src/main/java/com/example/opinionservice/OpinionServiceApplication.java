package com.example.opinionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class OpinionServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpinionServiceApplication.class, args);
    }
}
