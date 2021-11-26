package com.imooc.mo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Map;

/**
 * @author 包建丰
 * @date 2021/11/26 08 :50
 * @description
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document("message")
public class MessageMO {

    @Id
    private String id;      // 消息主键id

    @Field("fromUserId")
    private String fromUserId;      // 消息来自的用户id
    @Field("fromNickname")
    private String fromNickname;    // 消息来自的用户昵称
    @Field("fromFace")
    private String fromFace;        // 消息来自的用户头像

    @Field("toUserId")
    private String toUserId;        // 消息发送对象的用户id

    @Field("msgType")
    private Integer msgType;        // 消息类型 对应枚举 -> MessageEnum
    @Field("msgContent")
    private Map msgContent;         // 消息内容

    @Field("create_time")
    private Date createTime;        // 消息创建的时间

}

