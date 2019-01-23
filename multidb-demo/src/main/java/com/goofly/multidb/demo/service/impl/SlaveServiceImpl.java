package com.goofly.multidb.demo.service.impl;

import com.goofly.multidb.demo.service.SlaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goofly.multidb.annotation.DS;
import com.goofly.multidb.demo.mapper.SlaveDao;
import com.goofly.multidb.demo.vo.UserInfo;

@Service
@DS("slave")
public class SlaveServiceImpl implements SlaveService {
    @Autowired
    private SlaveDao slaveDao;

    public UserInfo selectService(String city){
        return slaveDao.findByName(city);
    }

    @Transactional
    public void insertService(UserInfo userInfo){
    	
    	if("小篱".equals(userInfo.getUserName())) {
    		slaveDao.insertHappiness(404L,"小篱", "9421");
    		//测试事务
    		int a = 1 / 0;
    		slaveDao.insertHappiness(405L,"小篱", "9422");
    	}
    	slaveDao.insertHappiness(userInfo.getId(), userInfo.getUserName(), userInfo.getPassword());
    }
}
