package com.frame.security.controller;

import com.frame.security.entity.SysUser;
import com.frame.security.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author : Crazy.X
 * @Date : 2019/10/19
 */
@RestController
public class TestController {

    @Autowired
    SysUserService sysUserService;

    @GetMapping("/a")
    public String data() {
        return null;
    }
}
