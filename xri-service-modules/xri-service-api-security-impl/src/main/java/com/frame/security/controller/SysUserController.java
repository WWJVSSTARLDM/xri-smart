package com.frame.security.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.frame.security.entity.SysUser;
import com.frame.security.service.SysUserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Autowired
    SysUserService sysUserService;

    @GetMapping("/getOne")
    public SysUser addOrder() {
        SysUser one = sysUserService.getOne(Wrappers.<SysUser>query().eq("realname", "张三"));
        return one;
    }

    @GetMapping("/list")
    public List<SysUser> list() {
        QueryWrapper<SysUser> like = Wrappers.<SysUser>query().like("realname", "张");

        List<SysUser> list = sysUserService.list(like);
        return list;

    }
}
