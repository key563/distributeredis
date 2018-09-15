package com.key.modules.sys.controller;

import com.github.pagehelper.PageInfo;
import com.key.modules.sys.model.User;
import com.key.modules.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/add")
    public int addUser(User user){
        return userService.addUser(user);
    }

    @RequestMapping(value = "/all/{pageNum}/{pageSize}")
    public Object findAllUser(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize){
        PageInfo<User> userList = userService.findAllUser(pageNum,pageSize);
        return userList;
    }
}
