package com.frame.api.provider;

import com.frame.common.entity.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface OrderService {
    @GetMapping("/order")
    Order getOrder(@RequestParam Long id);
}
