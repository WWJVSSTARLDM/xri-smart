package com.frame.security.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.frame.security.entity.SysUser;
import com.frame.security.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author : Crazy.X
 * @Date : 2019/10/20
 * <p>
 * 设置动态用户信息
 */
@Component
public class UserDetailsServiceConfig implements UserDetailsService {

    @Autowired
    SysUserMapper sysUserMapper;

    /**
     * 自定义查询逻辑 查询数据库
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = selectUserByRealName(username);
        return sysUser;
    }


    /**
     * 根据用户名查询用户数据
     */
    private SysUser selectUserByRealName(String username) {
        QueryWrapper<SysUser> query = Wrappers.<SysUser>query();
        query.eq("realname", username);
        return sysUserMapper.selectOne(query);
    }

    /**
     * 根据用户名查询用户拥有权限
     */
    private List<GrantedAuthority> selectPermissionsByRealName(String username) {
        return sysUserMapper.selectPermissionsByRealName(username);
    }
}
