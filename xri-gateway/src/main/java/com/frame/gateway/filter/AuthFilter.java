package com.frame.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frame.common.constant.Constant;
import com.frame.common.entity.User;
import com.frame.common.enums.URLEnum;
import com.frame.gateway.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Objects;

/**
 * @Author : Crazy.X
 * @Date : 2019/10/9
 * <p>
 * 拦截所有URL进行判断是否携带token
 */
//@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {


    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestURL = request.getURI().toString();
        // 判断是否是登录接口
        if (requestURL.contains(URLEnum.LOGIN.getURL())) {
            log.info("请求URL路径:[{}]", request.getPath());
            return chain.filter(exchange);
        }
        // 判断是否登携带token 进行拦截or放行
        String token = checkToken(request);
        if (StringUtils.isNotBlank(token)) {
            // 查询token是否过期或者不一致
            User user = checkTokenExpire(token);
            if (user != null) {
                /* 是否将用于信息设置request,根据业务需求,也可以从Redis使用token获取 */
                log.info("请求URL路径:[{}],用户:[{}]", request.getPath(), user.getUsername());
                return chain.filter(exchange);
            }
            return tokenInvalid(exchange);
        } else {
            return tokenInvalid(exchange);
        }
    }

    /**
     * token失效或不存在
     */
    private Mono<Void> tokenInvalid(ServerWebExchange exchange) {
        // 无token设置状态码
        ServerHttpResponse response = exchange.getResponse();
        // 封装错误信息
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("code", HttpStatus.UNAUTHORIZED.value());
        responseData.put("message", "请登录");
        responseData.put("cause", "Token is empty");
        try {
            // 把结果转换为json
            ObjectMapper objectMapper = new ObjectMapper();
            byte[] data = objectMapper.writeValueAsBytes(responseData);
            // 把错误信息输出到页面
            DataBuffer buffer = response.bufferFactory().wrap(data);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("AuthFilter异常");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检查token是否过期或一致
     */
    private User checkTokenExpire(String token) {
        User user = (User) redisUtil.get(token);
        if (user != null) {
            redisUtil.expire(token, Constant.TOKEN_EXPIRE_TIME);
        }
        return user;
    }

    /**
     * 校验是否携带Token
     */
    private String checkToken(ServerHttpRequest request) {
        String token = null;
        if (request.getCookies().containsKey(Constant.ACCESS_TOKEN)) {
            String redisToken = Objects.requireNonNull(request.getCookies().getFirst(Constant.ACCESS_TOKEN)).toString();
            token = StringUtils.replaceOnce(redisToken, "=", ":");
        }
        return token;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
