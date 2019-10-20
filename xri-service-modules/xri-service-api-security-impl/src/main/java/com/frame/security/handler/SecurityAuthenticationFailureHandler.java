package com.frame.security.handler;

import com.alibaba.fastjson.JSON;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @Author : Crazy.X
 * @Date : 2019/10/20
 * <p>
 * 自定义失败返回为json
 */
@Component
public class SecurityAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        //返回json数据
        HashMap<String, Object> result = new HashMap<>();
        result.put("success", false);
        //错误信息
        result.put("errorMsg", exception.getMessage());
        String json = JSON.toJSONString(result);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(json);
    }
}