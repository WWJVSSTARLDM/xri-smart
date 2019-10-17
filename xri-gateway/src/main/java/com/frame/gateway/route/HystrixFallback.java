package com.frame.gateway.route;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author : Crazy.X
 * @Date : 2019/10/9
 * <p>
 * Gateway Hystrix 回退类
 */
@RestController
public class HystrixFallback {

    @RequestMapping("/hystrixFallback")
    public String hystrixFallback() {
        return "This is a fallback";
    }
}
