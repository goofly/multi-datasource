package com.goofly.multidb.demo.service;

import com.goofly.multidb.demo.vo.UserInfo;

public interface SlaveService {
	
	public UserInfo selectService(String city);

	public void insertService(UserInfo happiness);
}
