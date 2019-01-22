package com.goofly.multidb.demo.controller;

import com.goofly.multidb.demo.service.MasterService;
import com.goofly.multidb.demo.service.SlaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.goofly.multidb.demo.vo.UserInfo;

@RestController
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    private MasterService masterService;
    
    @Autowired
    private SlaveService slaveService;

    @RequestMapping("/master")
    public UserInfo master(@RequestParam("name") String name){
        return masterService.selectService(name);
    }
    
    @RequestMapping("/slave")
    public UserInfo slave(@RequestParam("name") String name){
    	return slaveService.selectService(name);
    }

    @RequestMapping("/masterInsert")
    public UserInfo masterInsert(@RequestBody UserInfo happiness){
        masterService.insertService(happiness);
        return masterService.selectService(happiness.getUserName());
    }
    
    @RequestMapping("/slaveInsert")
    public UserInfo slaveInsert(@RequestBody UserInfo happiness){
    	slaveService.insertService(happiness);
    	return slaveService.selectService(happiness.getUserName());
    }
}