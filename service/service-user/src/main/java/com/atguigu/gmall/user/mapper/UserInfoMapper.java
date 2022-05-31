package com.atguigu.gmall.user.mapper;


import com.atguigu.gmall.model.user.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86185
* @description 针对表【user_info(用户表)】的数据库操作Mapper
* @createDate 2022-05-31 18:33:36
* @Entity com.atguigu.gmall.product.domain.UserInfo
*/
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}




