package com.goofly.multidb.demo.service.impl;

import com.goofly.multidb.annotation.DS;
import com.goofly.multidb.demo.mapper.MasterDao;
import com.goofly.multidb.demo.service.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goofly.multidb.demo.vo.UserInfo;

@Service
@DS("master")
public class MasterServiceImpl implements MasterService {
    @Autowired
    private MasterDao masterDao;

    
    public UserInfo selectService(String city){
        return masterDao.findByName(city);
    }

    @Transactional
    public void insertService(UserInfo happiness){
    	
    	if("小篱".equals(happiness.getUserName())) {
    		masterDao.insertHappiness(404L,"小篱", "9421");
    		//测试事务
    		int a = 1 / 0;
    		masterDao.insertHappiness(405L,"小篱", "9422");
    	}
    	masterDao.insertHappiness(happiness.getId(), happiness.getUserName(), happiness.getPassword());
    }
}
