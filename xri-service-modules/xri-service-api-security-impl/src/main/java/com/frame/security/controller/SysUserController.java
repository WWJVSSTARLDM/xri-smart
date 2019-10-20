package com.frame.security.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Crazy.X
 * @since 2019-10-20
 */
@RestController
//@RequestMapping("/sys-user")
public class SysUserController {


    @GetMapping("/addOrder")
    public String addOrder() {
        return "ok";
    }
}
