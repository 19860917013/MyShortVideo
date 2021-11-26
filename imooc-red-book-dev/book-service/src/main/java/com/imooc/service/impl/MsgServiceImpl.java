package com.imooc.service.impl;

import com.imooc.base.BaseInfoProperties;
import com.imooc.enums.MessageEnum;
import com.imooc.mo.MessageMO;
import com.imooc.pojo.Users;
import com.imooc.respository.MessageRepository;
import com.imooc.service.MsgService;
import com.imooc.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.imooc.base.BaseInfoProperties.REDIS_FANS_AND_VLOGGER_RELATIONSHIP;

/**
 * @author 包建丰
 * @date 2021/11/26 08 :57
 * @description
 **/

@Service
public class MsgServiceImpl extends BaseInfoProperties implements MsgService {
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


    @Override
    public List<MessageMO> queryList(String toUserId, Integer page, Integer pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "createTime");

        List<MessageMO> list = messageRepository.findAllByToUserIdEqualsOrderByCreateTimeDesc(toUserId, pageable);

        for (MessageMO msg : list) {
            // 如果类型是关注，则需要查询一下我有没有关注他，用于在前端页面显示“关注”或“互粉”
            if (msg.getMsgType().equals(MessageEnum.FOLLOW_YOU.type)) {
                Map map = msg.getMsgContent();
                if (map == null) {
                    map = new HashMap();
                }
                String relationShip = redis.get(REDIS_FANS_AND_VLOGGER_RELATIONSHIP + ":" + msg.getToUserId() + ":" + msg.getFromUserId());
                if (StringUtils.isNotBlank(relationShip) && relationShip.equalsIgnoreCase("1")) {
                    map.put("isFriend", true);
                } else {
                    map.put("isFriend", false);
                }
                msg.setMsgContent(map);
            }
        }

        return list;
    }


}
