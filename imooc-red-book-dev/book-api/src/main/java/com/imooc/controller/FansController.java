package com.imooc.controller;

import com.imooc.base.BaseInfoProperties;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.Users;
import com.imooc.service.FansService;
import com.imooc.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 包建丰
 * @date 2021/11/25 18 :12
 * @description
 **/

@Api(tags = "FansController 粉丝功能模块")
@Slf4j
@RestController
@RequestMapping("fans")
public class FansController extends BaseInfoProperties {

    @Autowired
    private UserService userService;
    @Autowired
    private FansService fansService;

    @PostMapping("follow")
    public GraceJSONResult follow(@RequestParam String myId,
                                  @RequestParam String vlogerId) {

        // 两者id不能为空
        if (StringUtils.isBlank(myId) || StringUtils.isBlank(vlogerId)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_RESPONSE_NO_INFO);
        }

        // 自己不能关注自己
        if (myId.equals(vlogerId)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_RESPONSE_NO_INFO);
        }

        /**
         * myId: 我的id，关注者
         * vlogerId: 视频博主的id，被关注者
         */

        // 校验两个用户id是否存在
        Users myInfo = userService.getUser(myId);
        Users vloger = userService.getUser(vlogerId);

        // 思考：两个用户id的数据库查询后的判断，要不要分开？
        if (myInfo == null || vloger == null) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_RESPONSE_NO_INFO);
        }

        // 保存关系到数据库
        fansService.doFollow(myId, vlogerId);

        // 博主粉丝+1，我的关注+1
        redis.increment(REDIS_MY_FOLLOWS_COUNTS + ":" + myId, 1);
        redis.increment(REDIS_MY_FANS_COUNTS + ":" + vlogerId, 1);

        // 我和博主的关注关联关系，依赖redis，不要存入数据库，避免db性能瓶颈
        // 我关注博主
        redis.set(REDIS_FANS_AND_VLOGGER_RELATIONSHIP + ":" + myId + ":" +vlogerId, "1");


        return GraceJSONResult.ok();
    }
}

