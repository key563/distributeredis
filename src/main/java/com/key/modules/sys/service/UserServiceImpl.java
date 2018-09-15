package com.key.modules.sys.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.key.modules.sys.mapper.UserMapper;
import com.key.modules.sys.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public int addUser(User user) {
        return userMapper.insert(user);
    }

    @Override
    public PageInfo<User> findAllUser(int pageNum, int pageSize) {
        //将参数传给这个方法就可以实现物理分页了
        PageHelper.startPage(pageNum, pageSize);
        //紧跟着的第一个select方法会被分页，Mapper会被PageInterceptor截拦器截拦,并从threadlocal中取出分页信息，把分页信息加到sql语句中，实现了分页查询
        List<User> userList = userMapper.selectAllUser();
        // 上述只返回列表数据信息，如果需要携带分页信息返回，需要通过PageInfo封装返回，使用方式则是直接通过列表构造PageInfo即可，注意PageInfo默认每页显示8条数据，且PageNum=1为第一页，具体可查看源码
        PageInfo<User> page = new PageInfo<>(userList);
        return page;
    }
}
