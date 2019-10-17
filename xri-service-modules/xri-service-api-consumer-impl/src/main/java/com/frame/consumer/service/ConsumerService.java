package com.frame.consumer.service;

import com.frame.common.entity.Order;
import com.frame.consumer.feign.OrderServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * OrderServiceFeign 通过Feign进行服务之间的调用
 */
@Service
public class ConsumerService {

    @Autowired
    OrderServiceFeign orderServiceFeign;

    public Order getOrder(Long id) {
        Order order = orderServiceFeign.getOrder(id);
        return order;
    }
}
