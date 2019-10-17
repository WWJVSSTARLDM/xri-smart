package com.frame.hystrix.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author : Crazy.X
 * @Date : 2019/10/16
 *
 * HystrixDashboard 监控ui
 * @Param localhost Hystrix Dashboard Service Address
 * @Param port      Hystrix Dashboard Service Address
 * {@link} http://localhost:port/hystrix
 *
 * 监控集群ui 表单
 * @Param localhost Micro Service Address
 * @Param port      Micro Service Port
 * {@link} http://localhost:port/turbine.stream
 */
@EnableEurekaClient
@RestController
@EnableHystrix
@EnableHystrixDashboard
@EnableTurbine
@SpringCloudApplication
public class HystrixDashboardApp {
    public static void main(String[] args) {
        SpringApplication.run(HystrixDashboardApp.class, args);
    }
}
