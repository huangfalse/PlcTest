package com.mes;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author : zhoush
 * @Date: 2024/3/25 10:49
 * @Description:
 */
@Slf4j
@SpringBootApplication
@MapperScan("com.mes.*.mapper")
@EnableDiscoveryClient
@EnableScheduling
@EnableAsync
public class PlcConnectModuleApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(PlcConnectModuleApplication.class, args);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
