package com.example.demo.dao;


import com.example.demo.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserInfoMapper {
    @Select("select * from user_info where status=1")
    List<UserInfo> selectList();
}
