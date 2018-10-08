package com.key.modules.sys.service;

import com.github.pagehelper.PageInfo;
import com.key.modules.sys.model.UserInfo;

public interface UserInfoService {
    /**
     * 添加用户
     * @param userInfo
     * @return  成功返回1，失败返回0
     */
    int addUser(UserInfo userInfo);

    /**
     * 分页查询所有用户
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<UserInfo> findAllUser(int pageNum, int pageSize);
}
