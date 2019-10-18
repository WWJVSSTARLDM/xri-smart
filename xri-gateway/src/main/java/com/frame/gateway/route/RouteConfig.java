package com.frame.gateway.route;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author : Crazy.X
 * @Date : 2019/10/9
 * <p>
 * Gateway 路由配置
 */
@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        /**
         * 详情配置参照官方文档
         * {@link} https://github.com/spring-cloud-samples/spring-cloud-gateway-sample/blob/master/src/main/java/com/example/demogateway/DemogatewayApplication.java
         */
        return builder.routes()
                /**
                 * consumer 是本次consumer的ID
                 * r.path("/consumer/**")是请求的url
                 * .uri("http://www.jd.com")则是进行转发到京东
                 */
                .route("consumer", r -> r.path("/consumer/**")
                        .uri("http://www.jd.com"))
                .route("scm-service-consumer", r -> r.path("/api/**")
                        /**
                         * 整合 Hystrix 加入回滚
                         * f.stripPrefix(1) 去掉第一个前缀转发 没有了api
                         * localhost:9000/api/order?id=1 -> localhost:9000/xr-service-api-consumer-impl/order?id=1
                         * url就是转发路径。lb是轮训机制到该服务下面的所有节点
                         */
                        .filters(f -> f.stripPrefix(1).hystrix(config -> config.setFallbackUri("forward:/hystrixFallback")))
                        .uri("lb://XRI-SERVICE-API-CONSUMER-IMPL"))
                .build();
    }
}
