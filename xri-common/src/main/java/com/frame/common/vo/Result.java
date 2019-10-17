package com.frame.common.vo;


import com.frame.common.enums.CodeMsgEnum;

import java.io.Serializable;

/**
 * @Author : Crazy.X
 * @Date : 2019/10/10
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = -2274383355561443202L;
    private String message;
    private int code;
    private T data;

    private Result(T data) {
        this.code = 0;
        this.message = "成功";
        this.data = data;
    }

    private Result(CodeMsgEnum cm) {
        if (cm == null) {
            return;
        }
        this.code = cm.getRetCode();
        this.message = cm.getMessage();
    }

    /**
     * 成功时候的调用
     *
     * @return
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(data);
    }

    /**
     * 成功，不需要传入参数
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Result<T> success() {
        return (Result<T>) success("");
    }

    /**
     * 失败时候的调用
     *
     * @return
     */
    public static <T> Result<T> error(CodeMsgEnum cm) {
        return new Result<T>(cm);
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}