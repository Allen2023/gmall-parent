package com.atguigu.gmall.user.service.impl;

import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.common.util.MD5;
import com.atguigu.gmall.model.user.UserInfo;
import com.atguigu.gmall.model.vo.LoginUserResponseVo;
import com.atguigu.gmall.starter.constants.RedisConst;
import com.atguigu.gmall.user.mapper.UserInfoMapper;
import com.atguigu.gmall.user.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 86185
 * @description 针对表【user_info(用户表)】的数据库操作Service实现
 * @createDate 2022-05-31 18:33:36
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {
    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public LoginUserResponseVo login(UserInfo userInfo) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_name",userInfo.getLoginName());
        queryWrapper.eq("passwd", MD5.encrypt(userInfo.getPasswd()));
        UserInfo loginUser = userInfoMapper.selectOne(queryWrapper);
        if (loginUser == null) {
            //登陆失败
            //账号密码错误
            return null;
        }
        //登录成功 创建值对象 保存token 和 nickName
        LoginUserResponseVo responseVo = new LoginUserResponseVo();
        //复制Ip地址保存到对象
        loginUser.setIpAddr(userInfo.getIpAddr());
        //将查询到的User保存到Redis
        String token = saveAuthentication(loginUser);
        responseVo.setToken(token);
        responseVo.setNickName(loginUser.getNickName());

        return responseVo;
    }

    /**
     * 保存用户信息到redis
     *
     * @param loginUser
     * @return
     */
    private String saveAuthentication(UserInfo loginUser) {
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set(RedisConst.USER_LOGIN_PREFIX + token, JSONs.toStr(loginUser), 7, TimeUnit.DAYS);
        return token;
    }


    @Override
    public void logOut(String token) {
        //删除该用户的缓存
        redisTemplate.delete(RedisConst.USER_LOGIN_PREFIX + token);
    }
}




