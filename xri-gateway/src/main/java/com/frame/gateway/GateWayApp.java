package com.frame.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author : Crazy.X
 * @Date : 2019/10/9
 *
 * GateWay 网关
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GateWayApp {
    public static void main(String[] args) {
        SpringApplication.run(GateWayApp.class,args);
    }
}
