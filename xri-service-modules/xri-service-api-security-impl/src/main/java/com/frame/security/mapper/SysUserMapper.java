package com.frame.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.frame.security.entity.SysPermission;
import com.frame.security.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Crazy.X
 * @since 2019-10-20
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {


    /**
     * 根据用真实姓名名查询用户所有权限
     *
     * @param realName 用真实姓名
     * @return
     */
    @Select("SELECT " +
            "    sp.*  " +
            "FROM " +
            "    sys_user su " +
            "    INNER JOIN sys_user_role sur ON su.id = sur.user_id " +
            "    INNER JOIN sys_role sr ON sr.id = sur.role_id " +
            "    INNER JOIN sys_role_permission srp ON sr.id = srp.role_id " +
            "    INNER JOIN sys_permission sp ON srp.perm_id = sp.id  " +
            "WHERE " +
            "    su.realname = #{realName}")
    List<SysPermission> selectPermissionsByRealName(@Param("realName") String realName);
}
