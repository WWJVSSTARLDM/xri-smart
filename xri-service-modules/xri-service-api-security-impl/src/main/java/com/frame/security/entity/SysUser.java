package com.frame.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author Crazy.X
 * @since 2019-10-20
 */
//@Data
//@EqualsAndHashCode(callSuper = false)
//@Accessors(chain = true)
public class SysUser implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 密码
     */
    private String password;

    /**
     * 创建日期
     */
    @TableField("createDate")
    private LocalDateTime createDate;

    /**
     * 最后登录时间
     */
    @TableField("lastLoginTime")
    private LocalDateTime lastLoginTime;

    /**
     * 是否可用
     */
    private Integer enabled;

    /**
     * 是否过期
     */
    @TableField("accountNonExpired")
    private Integer accountNonExpired;

    /**
     * 是否锁定
     */
    @TableField("accountNonLocked")
    private Integer accountNonLocked;

    /**
     * 证书是否过期
     */
    @TableField("credentialsNonExpired")
    private Integer credentialsNonExpired;

    /**
     * 用户拥有权限
     */
    @TableField(exist = false)
    List<GrantedAuthority> authorities;


    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public void setAccountNonExpired(Integer accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(Integer accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(Integer credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getRealname() {
        return realname;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired == 0;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked == 0;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired == 0;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled == 1;
    }
}
