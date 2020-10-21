package com.example.demo.his.impl;

import com.example.demo.annotation.SwitchSource;
import com.example.demo.dao.HisDeptInfoMapper;
import com.example.demo.domain.DeptInfo;
import com.example.demo.domain.UserInfo;
import com.example.demo.his.HisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HisServiceImpl implements HisService {

    @Autowired
    private HisDeptInfoMapper hisDeptInfoMapper;

    //不开启事务
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    //切换到HIS的数据源
    @SwitchSource
    @Override
    public List<DeptInfo> list() {
        return hisDeptInfoMapper.listDept();
    }
}
