package com.frame.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Crazy.X
 * @since 2019-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 密码
     */
    private String password;

    /**
     * 创建日期
     */
    private LocalDateTime createDate;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 是否可用
     */
    private Integer enabled;

    /**
     * 是否过期
     */
    private Integer accountNonExpired;

    /**
     * 是否锁定
     */
    private Integer accountNonLocked;

    /**
     * 证书是否过期
     */
    private Integer credentialsNonExpired;


}
