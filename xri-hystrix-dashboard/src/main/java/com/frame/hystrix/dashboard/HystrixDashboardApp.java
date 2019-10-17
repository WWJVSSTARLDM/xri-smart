package com.frame.hystrix.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * @Author : Crazy.X
 * @Date : 2019/10/16
 * <p>
 * HystrixDashboard 监控ui
 * @Param localhost Hystrix Dashboard Service Address
 * @Param port      Hystrix Dashboard Service Address
 * {@link} http://localhost:port/hystrix
 * <p>
 * 监控ui 表单
 * @Param localhost Micro Service Address
 * @Param port      Micro Service Port
 * {@link} http://localhost:port/actuator/hystrix.stream
 */
@EnableHystrixDashboard
@SpringBootApplication
@EnableDiscoveryClient
public class HystrixDashboardApp {
    public static void main(String[] args) {
        SpringApplication.run(HystrixDashboardApp.class, args);
    }
}
