package com.imooc.service.impl;

import com.github.pagehelper.PageHelper;
import com.imooc.base.BaseInfoProperties;
import com.imooc.bo.CommentBO;
import com.imooc.mapper.CommentMapper;
import com.imooc.mapper.CommentMapperCustom;
import com.imooc.pojo.Comment;
import com.imooc.service.CommentService;
import com.imooc.utils.PagedGridResult;
import com.imooc.vo.CommentVO;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 包建丰
 * @date 2021/11/25 21 :43
 * @description
 **/
@Service
public class CommentServiceImpl extends BaseInfoProperties implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private Sid sid;

    @Transactional
    @Override
    public CommentVO createComment(CommentBO commentBO) {
        String commentId = sid.nextShort();

        Comment comment = new Comment();
        comment.setId(commentId);

        comment.setVlogId(commentBO.getVlogId());
        comment.setVlogerId(commentBO.getVlogerId());

        comment.setCommentUserId(commentBO.getCommentUserId());
        comment.setFatherCommentId(commentBO.getFatherCommentId());
        comment.setContent(commentBO.getContent());
        comment.setLikeCounts(0);
        comment.setCreateTime(new Date());

        commentMapper.insert(comment);

        // 评论总数累加
        redis.increment(REDIS_VLOG_COMMENT_COUNTS + ":" + commentBO.getVlogId(), 1);

        // 留言后的最新评论需要返回到前端进行展示
        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties(comment, commentVO);

        return commentVO;
    }

    @Autowired
    private CommentMapperCustom commentMapperCustom;


    @Override
    public PagedGridResult queryVlogComments(String vlogId, Integer page, Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("vlogId", vlogId);

        PageHelper.startPage(page, pageSize);
        List<CommentVO> list = commentMapperCustom.getCommentList(map);

        return setterPagedGrid(list, page);
    }


    @Transactional
    @Override
    public void deleteComment(String commentUserId, String commentId, String vlogId) {

        Comment pendingComment = new Comment();
        pendingComment.setId(commentId);
        pendingComment.setCommentUserId(commentUserId);

        commentMapper.delete(pendingComment);

        // 评论总数累减
        redis.decrement(REDIS_VLOG_COMMENT_COUNTS + ":" + vlogId, 1);
    }


}
