package com.imooc.service;

import com.imooc.pojo.Users;

/**
 * @author 包建丰
 * @date 2021/11/23 21 :45
 * @description
 **/
public interface UserService {

    /**
     * 判断用户是否存在，如果存在返回user信息
     */
    public Users queryMobileIsExist(String mobile);

    /**
     * 创建用户，新增用户记录到数据库
     */
    public Users createUser(String mobile);

}

