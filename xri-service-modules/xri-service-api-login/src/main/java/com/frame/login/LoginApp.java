package com.frame.login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author : Crazy.X
 * @Date : 2019/10/16
 */
@MapperScan("com.frame.login.mapper")
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class LoginApp {
    public static void main(String[] args) {
        SpringApplication.run(LoginApp.class, args);
    }

}
