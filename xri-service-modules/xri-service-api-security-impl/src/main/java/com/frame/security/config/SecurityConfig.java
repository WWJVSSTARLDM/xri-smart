package com.frame.security.config;

import com.frame.common.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @Author : Crazy.X
 * @Date : 2019/10/19
 */
@EnableWebSecurity
@Component
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceConfig userDetailsServiceConfig;

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
        http.authorizeRequests()
                .antMatchers("/**")
                .fullyAuthenticated()
                .and()
                .formLogin();
    }
}
