package com.example.demo;

import com.example.demo.dao.HisDeptInfoMapper;
import com.example.demo.domain.DeptInfo;
import com.example.demo.domain.UserInfo;
import com.example.demo.his.HisService;
import com.example.demo.service.UserInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class DatasourceDemoApplicationTests {


    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private HisService hisService;

    @Test
    @Transactional
    void contextLoads() {
        List<DeptInfo> deptInfos = hisService.list();
        List<UserInfo> userInfos = userInfoService.list();
        System.out.println(userInfos);
    }

}
