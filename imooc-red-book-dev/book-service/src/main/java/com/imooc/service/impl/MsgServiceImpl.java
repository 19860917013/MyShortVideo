package com.imooc.service.impl;

import com.imooc.mo.MessageMO;
import com.imooc.pojo.Users;
import com.imooc.respository.MessageRepository;
import com.imooc.service.MsgService;
import com.imooc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author 包建丰
 * @date 2021/11/26 08 :57
 * @description
 **/

@Service
public class MsgServiceImpl implements MsgService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    @Override
    public void createMsg(String fromUserId, String toUserId, Integer type, Map msgContent) {

        Users fromUser = userService.getUser(fromUserId);

        MessageMO messageMO = new MessageMO();
        messageMO.setFromUserId(fromUserId);
        messageMO.setFromNickname(fromUser.getNickname());
        messageMO.setFromFace(fromUser.getFace());
        messageMO.setToUserId(toUserId);

        if (msgContent != null) {
            messageMO.setMsgContent(msgContent);
        }

        messageMO.setMsgType(type);
        messageMO.setCreateTime(new Date());
        messageRepository.save(messageMO);
    }

}
