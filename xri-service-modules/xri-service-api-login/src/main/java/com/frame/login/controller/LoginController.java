package com.frame.login.controller;

import com.frame.common.entity.User;
import com.frame.common.enums.CodeMsgEnum;
import com.frame.common.vo.Result;
import com.frame.login.service.LoginServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author : Crazy.X
 * @Date : 2019/10/8
 */
@Slf4j
@RestController
public class LoginController {

    @Autowired
    private LoginServiceImpl loginService;

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(User user, HttpServletResponse response) {
        // 过滤为空
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            return Result.error(CodeMsgEnum.USER_OR_PASSWORD_NOT_NULL);
        }
        return loginService.login(user, response);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // todo 登出
    }
}