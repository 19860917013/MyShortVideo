package com.imooc.controller;

import com.imooc.base.BaseInfoProperties;
import com.imooc.bo.RegistLoginBO;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.Users;
import com.imooc.service.UserService;
import com.imooc.utils.IPUtil;
import com.imooc.utils.SMSUtils;
import com.imooc.vo.UsersVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author 包建丰
 * @date 2021/11/23 12 :20
 * @description
 **/

@Api(tags = "PassportController 通行证接口模块")
@RestController
@RequestMapping("passport")
@Slf4j
public class PassportController extends BaseInfoProperties {

    @Autowired
    private SMSUtils smsUtils;

    @Autowired
    private UserService userService;

    @PostMapping("getSMSCode")
    public GraceJSONResult getSMSCode(@RequestParam String mobile,
                                      HttpServletRequest request) throws Exception {
        if (StringUtils.isBlank(mobile)) {
            return GraceJSONResult.ok();
        }

        // TODO 获得用户ip，
        String userIp = IPUtil.getRequestIp(request);
        // TODO 根据用户ip进行限制，限制用户在60秒之内只能获得一次验证码
        redis.setnx60s(MOBILE_SMSCODE + ":" + userIp, userIp);

        String code = (int) ((Math.random() * 9 + 1) * 100000) + "";
        smsUtils.sendSMS("19860917013", code);
        log.info(code);

        // TODO 把验证码放入到redis中，用于后续的验证
        redis.set(MOBILE_SMSCODE + ":" + mobile, code, 30 * 60 * 1000);

        return GraceJSONResult.ok();
    }

    @ApiOperation(value = "用户一键登录/注册接口")
    @PostMapping("login")
    public GraceJSONResult doLogin(@Valid @RequestBody RegistLoginBO registLoginBO,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {

        String mobile = registLoginBO.getMobile();
        String smsCode = registLoginBO.getSmsCode();

        // 1. 从redis中获得校验验证码是否匹配
        String redisSMSCode = redis.get(MOBILE_SMSCODE + ":" + mobile);
        if (StringUtils.isBlank(redisSMSCode) || !redisSMSCode.equalsIgnoreCase(smsCode)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }

        // 2. 查询数据库，判断该用户是否注册
        Users user = userService.queryMobileIsExist(mobile);
        if (user == null) {
            // 2.1 如果用户没有注册过，则为null，需要注册信息入库
            user = userService.createUser(mobile);
        }


        // 3.如果用户不为空，表示已注册用户，那么保存用户会话信息
        String uToken = UUID.randomUUID().toString();
        redis.set(REDIS_USER_TOKEN + ":" + user.getId(), uToken);

        // 4. 用户登录注册成功以后，删除redis中的短信验证码，验证码只能使用一次，用过后作废
        redis.del(MOBILE_SMSCODE + ":" + mobile);

        // 5. 返回用户信息，包括token
        // 6. 编写自定义vo类，把用户信息和token放入
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user, usersVO);
        usersVO.setUserToken(uToken);

        return GraceJSONResult.ok(usersVO);

    }

    @ApiOperation(value = "退出登录")
    @PostMapping("logout")
    public GraceJSONResult logout(String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        log.info(userId);
        // 后端只需要清除用户token信息即可，前端也需要清除用户所存的会话信息
        redis.del(REDIS_USER_TOKEN + ":" + userId);

        // 思考：考虑到如果有多端的话，那么h5端的会话信息存储在哪里？退出登录后在哪里清除？
        return GraceJSONResult.ok();
    }


}
