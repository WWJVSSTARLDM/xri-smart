package com.frame.provider.service;

import com.frame.api.provider.OrderService;
import com.frame.common.entity.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProviderServiceImpl implements OrderService {

    @Value("${server.port}")
    Long post;

    @Override
    public Order getOrder(Long id) {
        Order order = new Order();
        order.setOrderId(post);
        order.setOrderName("兰博基尼");
        return order;
    }
}
