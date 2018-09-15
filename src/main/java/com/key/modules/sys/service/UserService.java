package com.key.modules.sys.service;

import com.github.pagehelper.PageInfo;
import com.key.modules.sys.model.User;

/**
 * @author tujia
 */
public interface UserService {
    /**
     * 添加用户
     * @param user
     * @return  成功返回1，失败返回0
     */
    int addUser(User user);

    /**
     * 分页查询所有用户
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<User> findAllUser(int pageNum, int pageSize);
}
