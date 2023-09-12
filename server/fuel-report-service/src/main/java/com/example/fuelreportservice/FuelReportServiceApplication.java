package com.example.fuelreportservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class FuelReportServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FuelReportServiceApplication.class, args);
    }
}
