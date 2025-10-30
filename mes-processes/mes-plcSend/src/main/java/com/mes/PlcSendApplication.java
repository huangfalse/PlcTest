package com.mes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author huang
 * @since 2025/10/29
 */

@SpringBootApplication
@EnableFeignClients(basePackages = "com.mes.feign")
public class PlcSendApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlcSendApplication.class, args);
    }
}