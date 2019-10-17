package com.frame.provider.controller;


import com.frame.api.provider.OrderService;
import com.frame.common.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProviderController {

    @Autowired
    private OrderService orderServiceImpl;

    @GetMapping("/order")
    public Order getOrder(@RequestParam Long id) {
        System.out.println("ProviderController......");
        return orderServiceImpl.getOrder(id);
    }
}
