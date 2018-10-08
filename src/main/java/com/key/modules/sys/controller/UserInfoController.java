package com.key.modules.sys.controller;

import com.github.pagehelper.PageInfo;
import com.key.modules.sys.model.UserInfo;
import com.key.modules.sys.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/userInfo")
public class UserInfoController {

    @Autowired
    UserInfoService userInfoService;

    @RequestMapping(value = "/add")
    public int addUser(UserInfo userInfo){
        return userInfoService.addUser(userInfo);
    }

    @RequestMapping(value = "/all/{pageNum}/{pageSize}")
    public Object findAllUser(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize){
        PageInfo<UserInfo> userList = userInfoService.findAllUser(pageNum,pageSize);
        return userList;
    }
}
