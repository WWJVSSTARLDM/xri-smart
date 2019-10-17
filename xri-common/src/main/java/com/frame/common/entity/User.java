package com.frame.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author : Crazy.X
 * @Date : 2019/10/8
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 3579448909191014800L;
    private String username;
    private String password;
}
