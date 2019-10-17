package com.frame.consumer.fallback;

import com.frame.common.entity.Order;
import com.frame.consumer.feign.OrderServiceFeign;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author : Crazy.X
 * @Date : 2019/10/16
 * <p>
 * Hystrix 回退类
 * Provider服务若中断则进行服务降级
 * 要求实现实现了Feign的接口
 */
@Component
@Slf4j
public class OrderServiceFallback implements OrderServiceFeign {
    private static Order order;

    static {
        order = new Order(500L, "系统异常，服务降级");
    }

    @Override
    public Order getOrder(Long id) {
        log.error("系统异常,请及时处理");
        return order;
    }
}
