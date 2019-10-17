package com.frame.consumer.controller;

import com.frame.common.entity.Order;
import com.frame.consumer.service.ConsumerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @RefreshScope 仅限于修改那个类，在当前类上注解
 * 并且无法修改配置相关，只能修改配置文件@Value参数
 */
@RestController
@RefreshScope
@Api(tags = "消费者接口")
public class ConsumerController {

    @Autowired
    ConsumerService consumerService;

    @Value("${hello}")
    private String flag;

    /**
     * Feign 调用接口
     */
    @ApiOperation("获取订单接口")
    @GetMapping("/order")
    public Order getOrder(@RequestParam Long id) {
        System.out.println("ConsumerController...");
        return consumerService.getOrder(id);
    }

    /**
     * 实现 config bus实时刷新@Value配置文件(不可刷新项目配置，项目启动端口数据库等)
     */
    @ApiOperation("测试热更新接口")
    @GetMapping("/bus")
    public String hello() {
        System.out.println(flag);
        return flag;
    }
}
