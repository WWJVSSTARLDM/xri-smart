package com.frame.consumer.feign;

import com.frame.api.provider.OrderService;
import com.frame.consumer.fallback.OrderServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author : Crazy.X
 * @Date : 2019/10/16
 * <p>
 * Feign组件
 * @Param name     Eureka注册的要调取的服务
 * @Parar fallback 出现服务中断，则调取降级方法
 */
@FeignClient(name = "xri-service-api-provider-impl", fallback = OrderServiceFallback.class)
public interface OrderServiceFeign extends OrderService {
}
