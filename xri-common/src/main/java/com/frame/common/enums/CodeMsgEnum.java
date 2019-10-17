package com.frame.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author : Crazy.X
 * @Date : 2019/10/10
 */
@Getter
@AllArgsConstructor
public enum CodeMsgEnum {

    // 通用异常
    SUCCESS(0, "success"),
    SERVER_ERROR(500, "服务器繁忙,请稍候再试"),
    PARAMETER_IS_NULL(400, "输入不能参数为空"),
    USER_NOT_EXIST(200, "用户不存在"),

    /* 用户相关 */
    USER_OR_PASSWORD_NOT_NULL(400, "用户名或密码不能为空"),
    USER_PASSWORD_ERROR(400, "密码错误"),
    ;
    private int retCode;


    private String message;

}