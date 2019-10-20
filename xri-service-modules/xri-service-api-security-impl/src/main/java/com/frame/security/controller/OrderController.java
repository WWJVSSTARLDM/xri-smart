package com.frame.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author : Crazy.X
 * @Date : 2019/10/20
 */
@RestController
public class OrderController {

    // 首页
    @RequestMapping("/")
    public String index() {
        return "index";
    }

    // 查询订单
    @RequestMapping("/showOrder")
    public String showOrder() {
        return "showOrder";
    }

    // 添加订单
    @RequestMapping("/addOrder")
    public String addOrder() {
        return "addOrder";
    }

    // 修改订单
    @RequestMapping("/updateOrder")
    public String updateOrder() {
        return "updateOrder";
    }

    // 删除订单
    @RequestMapping("/deleteOrder")
    public String deleteOrder() {
        return "deleteOrder";
    }

    /// 实现 userAdd 角色能够查询到findByOrder
    @RequestMapping("/findByOrder")
    public String findByOrder() {
        return "查询订单成功";
    }
}
