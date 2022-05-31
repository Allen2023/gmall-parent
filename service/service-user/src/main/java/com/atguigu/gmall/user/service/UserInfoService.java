package com.atguigu.gmall.user.service;


import com.atguigu.gmall.model.user.UserInfo;
import com.atguigu.gmall.model.vo.LoginUserResponseVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86185
* @description 针对表【user_info(用户表)】的数据库操作Service
* @createDate 2022-05-31 18:33:36
*/
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 登录账户
     * @param userInfo
     * @return
     */
    LoginUserResponseVo login(UserInfo userInfo);

    /**
     * 登出账户
     * @param token
     */
    void logOut(String token);

}
