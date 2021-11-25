package com.imooc.controller;

import com.imooc.bo.CommentBO;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.service.CommentService;
import com.imooc.vo.CommentVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author 包建丰
 * @date 2021/11/25 21 :39
 * @description
 **/
@Slf4j
@Api(tags = "CommentController 评论模块的接口")
@RequestMapping("comment")
@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("create")
    public GraceJSONResult create(@RequestBody @Valid CommentBO commentBO) {

        /**
         * TODO: 作业
         * fatherCommentId不为空且不为“0”的时候，
         * 校验上一条评论是否真实存在，不存在抛出异常
         */


        /**
         * TODO: 作业
         * vlogerId 和 commentUserId 都是对应用户的主键，
         * 需要校验是否真实存在用户，不存在抛出异常
         * 进阶：封装统一方法专门用于校验，[ checkUserExist(userId) ]
         */


        CommentVO commentVO = commentService.createComment(commentBO);

        return GraceJSONResult.ok(commentVO);
    }

}
