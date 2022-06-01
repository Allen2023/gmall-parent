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
        //{"loginName":"aaaa","passwd":"dddd"}
        //select * from user_info where login_name='admin' and passwd='111加密后的'
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_name",userInfo.getLoginName());
        queryWrapper.eq("passwd", MD5.encrypt(userInfo.getPasswd()));

        //成功的用户完整数据
        UserInfo loginUser = userInfoMapper.selectOne(queryWrapper);
        if(loginUser == null){
            //前端带的账号密码是错误的
            return null;
        }
        //用户登录成功
        LoginUserResponseVo loginUserResponseVo = new LoginUserResponseVo();
        //去redis保存登陆成功的用户的认证信息
        loginUser.setIpAddr(userInfo.getIpAddr()); //Ip复制过来
        String token = saveAuthentication(loginUser);
        //设置令牌
        loginUserResponseVo.setToken(token);
        //设置昵称
        loginUserResponseVo.setNickName(loginUser.getNickName());
        return loginUserResponseVo;
    }

    /**
     * 保存用户信息到redis
     *
     * @param loginUser
     * @return
     */
    private String saveAuthentication(UserInfo userInfo) {
        String token = UUID.randomUUID().toString().replace("-", "");
        // user:login:token = {用户数据}
        //redis按照token与用户的对应关系保存令牌
        redisTemplate.opsForValue().set(RedisConst.USER_LOGIN_PREFIX+token, JSONs.toStr(userInfo),7, TimeUnit.DAYS);
        return token;
    }


    @Override
    public void logOut(String token) {
        //删除该用户的缓存
        redisTemplate.delete(RedisConst.USER_LOGIN_PREFIX + token);
    }
}




