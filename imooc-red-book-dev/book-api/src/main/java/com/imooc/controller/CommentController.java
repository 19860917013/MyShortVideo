package com.imooc.controller;

import com.imooc.base.BaseInfoProperties;
import com.imooc.bo.CommentBO;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.service.CommentService;
import com.imooc.utils.PagedGridResult;
import com.imooc.vo.CommentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
public class CommentController extends BaseInfoProperties {
    @Autowired
    private CommentService commentService;

    @PostMapping("create")
    public GraceJSONResult create(@RequestBody @Valid CommentBO commentBO) {

        /**
         * TODO:
         * fatherCommentId不为空且不为“0”的时候，
         * 校验上一条评论是否真实存在，不存在抛出异常
         */


        /**
         * TODO:
         * vlogerId 和 commentUserId 都是对应用户的主键，
         * 需要校验是否真实存在用户，不存在抛出异常
         * 进阶：封装统一方法专门用于校验，[ checkUserExist(userId) ]
         */


        CommentVO commentVO = commentService.createComment(commentBO);

        return GraceJSONResult.ok(commentVO);
    }


    @ApiOperation(value = "短视频的评论总数")
    @GetMapping("counts")
    public GraceJSONResult counts(@RequestParam String vlogId) {

        String countsStr = redis.get(REDIS_VLOG_COMMENT_COUNTS + ":" + vlogId);
        if (StringUtils.isBlank(countsStr)) {
            countsStr = "0";
        }
        return GraceJSONResult.ok(Integer.valueOf(countsStr));
    }

    @GetMapping("list")
    public GraceJSONResult list(@RequestParam String vlogId,
                                @RequestParam(defaultValue = "") String userId,
                                @RequestParam Integer page,
                                @RequestParam Integer pageSize) {

        PagedGridResult gridResult = commentService.queryVlogComments(vlogId, page, pageSize);
        return GraceJSONResult.ok(gridResult);
    }

    @DeleteMapping("delete")
    public GraceJSONResult delete(@RequestParam String commentUserId,
                                  @RequestParam String commentId,
                                  @RequestParam String vlogId) {
        commentService.deleteComment(commentUserId, commentId, vlogId);
        return GraceJSONResult.ok();
    }

    @ApiOperation(value = "取消点赞评论")
    @PostMapping("unlike")
    public GraceJSONResult unlike(@RequestParam String userId, @RequestParam String commentId) {

        redis.decrementHash(REDIS_VLOG_COMMENT_LIKED_COUNTS, commentId, 1);

        return GraceJSONResult.ok();
    }


    @ApiOperation(value = "点赞评论")
    @PostMapping("like")
    public GraceJSONResult like(@RequestParam String userId, @RequestParam String commentId) {

//        故意犯错 bigkey
        redis.incrementHash(REDIS_VLOG_COMMENT_LIKED_COUNTS, commentId, 1);
        redis.setHashValue(REDIS_USER_LIKE_COMMENT, userId + ":" + commentId, "1");

        return GraceJSONResult.ok();
    }


}
