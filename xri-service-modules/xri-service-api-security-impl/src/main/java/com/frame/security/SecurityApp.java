package com.frame.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author : Crazy.X
 * @Date : 2019/10/18
 * <p>
 * 该服务是认证授权服务
 */
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class SecurityApp {
    public static void main(String[] args) {
        SpringApplication.run(SecurityApp.class, args);
    }
}
