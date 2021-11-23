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
}

