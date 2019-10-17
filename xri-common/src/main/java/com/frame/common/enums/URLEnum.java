package com.frame.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : Crazy.X
 * @Date : 2019/10/10
 */
@Getter
@AllArgsConstructor
public enum URLEnum {

    /* 登录接口 */
    LOGIN("/login"),
    ;
    private String URL;
}
