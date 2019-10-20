package com.frame.security;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * @Author : Crazy.X
 * @Date : 2019/10/18
 * <p>
 * 该服务是认证授权服务
 */
//@EnableDiscoveryClient
//@EnableFeignClients
    @MapperScan("com.frame.security.mapper")
@SpringBootApplication
public class SecurityApp {
    public static void main(String[] args) {
        SpringApplication.run(SecurityApp.class, args);
    }


    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druid() {
        return new DruidDataSource();
    }
}
