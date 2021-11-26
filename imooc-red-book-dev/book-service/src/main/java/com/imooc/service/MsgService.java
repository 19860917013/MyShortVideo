package com.imooc.service;

import java.util.Map;

/**
 * @author 包建丰
 * @date 2021/11/26 08 :56
 * @description
 **/
public interface MsgService {

    /**
     * 创建消息
     */
    public void createMsg(String fromUserId, String toUserId, Integer type, Map msgContent);

}


