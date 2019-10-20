package com.frame.security.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.frame.common.enums.URLEnum;
import com.frame.common.utils.Md5Utils;
import com.frame.security.entity.SysPermission;
import com.frame.security.handler.SecurityAuthenticationFailureHandler;
import com.frame.security.handler.SecurityAuthenticationSuccessHandler;
import com.frame.security.mapper.SysPermissionMapper;
import com.frame.security.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author : Crazy.X
 * @Date : 2019/10/19
 */
@EnableWebSecurity
@Component
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceConfig userDetailsServiceConfig;

    @Autowired
    SysPermissionMapper sysPermissionMapper;

    @Autowired
    SecurityAuthenticationSuccessHandler successHandler;

    @Autowired
    SecurityAuthenticationFailureHandler failureHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceConfig).passwordEncoder(new PasswordEncoder() {

            /**
             * 对表单密码进行加密
             */
            @Override
            public String encode(CharSequence rawPassword) {
                return Md5Utils.MD5Encode((String) rawPassword, "UTF-8");
            }

            /**
             * 加密的密码和数据库密码比对
             */
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encode(rawPassword).equals(encodedPassword);
            }
        });
    }

    /**
     * 设置拦截请求
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests = http
                .authorizeRequests();
        // 1.读取数据库权限列表
        List<SysPermission> permissions = selectPermission();
        // 设置权限
        permissions.forEach(p -> authorizeRequests.antMatchers(p.getUrl()).hasAuthority(p.getPermTag()));
        // Login除外的URL全部需要授权
        authorizeRequests.antMatchers(URLEnum.LOGIN.getURL())
                .permitAll()
                .antMatchers("/**")
                .fullyAuthenticated()
                .and()
                .formLogin()
                // 成功的自定义处理器
                .successHandler(successHandler)
                // 成功的自定义处理器
                .failureHandler(failureHandler)
                .and()
                .csrf()
                .disable();
    }

    /**
     * 查询所有权限
     */
    private List<SysPermission> selectPermission() {
        QueryWrapper<SysPermission> query = Wrappers.<SysPermission>query();
        return sysPermissionMapper.selectList(query);
    }
}
