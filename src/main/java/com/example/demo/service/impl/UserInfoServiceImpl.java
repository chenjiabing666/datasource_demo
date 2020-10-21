package com.example.demo.service.impl;

import com.example.demo.dao.UserInfoMapper;
import com.example.demo.domain.UserInfo;
import com.example.demo.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public List<UserInfo> list() {
        return userInfoMapper.selectList();
    }
}
