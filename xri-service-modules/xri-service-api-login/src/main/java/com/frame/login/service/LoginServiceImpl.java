package com.frame.login.service;

import com.frame.common.constant.Constant;
import com.frame.common.entity.User;
import com.frame.common.enums.CodeMsgEnum;
import com.frame.common.utils.CookieUtils;
import com.frame.common.utils.Md5Utils;
import com.frame.common.vo.Result;
import com.frame.login.mapper.UserDaoMapper;
import com.frame.login.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @Author : Crazy.X
 * @Date : 2019/10/8
 */
@Service
@Slf4j
public class LoginServiceImpl {

    @Autowired
    private UserDaoMapper userDaoMapper;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 登录业务
     * 从Redis取session
     * String userName = (String) request.getSession().getAttribute(Constant.ACCESS_TOKEN);
     *
     * @return
     */
    public Result login(User user, HttpServletResponse response) {
        // 密码Md5
        String md5Password = Md5Utils.MD5Encode(user.getPassword(), "UTF-8");
        user.setPassword(null);
        User resultUser = userDaoMapper.selectOne(user);
        if (resultUser == null) {
            return Result.error(CodeMsgEnum.USER_NOT_EXIST);
        }
        // 登录成功
        if (md5Password.equals(resultUser.getPassword())) {
            log.info("用户登录成功,登录用户:[{}]", user.getUsername());
            //登录成功，生成token
            String token = UUID.randomUUID().toString();
            redisUtil.set(Constant.ACCESS_TOKEN + ":" + token, user, Constant.TOKEN_EXPIRE_TIME);
            //写cookie
            CookieUtils.setCookie(response, Constant.ACCESS_TOKEN, token);
            return Result.success(token);
        } else {
            log.info("用户登录失败,登录用户:[{}]", user.getUsername());
            return Result.error(CodeMsgEnum.USER_PASSWORD_ERROR);
        }
    }
}