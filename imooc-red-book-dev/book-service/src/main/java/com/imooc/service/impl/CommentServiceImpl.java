package com.imooc.service.impl;

import com.github.pagehelper.PageHelper;
import com.imooc.base.BaseInfoProperties;
import com.imooc.bo.CommentBO;
import com.imooc.enums.MessageEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.CommentMapper;
import com.imooc.mapper.CommentMapperCustom;
import com.imooc.mapper.VlogMapper;
import com.imooc.pojo.Comment;
import com.imooc.pojo.Vlog;
import com.imooc.service.CommentService;
import com.imooc.service.MsgService;
import com.imooc.service.VlogService;
import com.imooc.utils.PagedGridResult;
import com.imooc.vo.CommentVO;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    private VlogService vlogService;

    @Autowired
    private MsgService msgService;

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

        // 评论回复短视频
        Vlog vlog = vlogService.getVlog(commentBO.getVlogId());
        Map msgContent = new HashMap();
        msgContent.put("vlogId", vlog.getId());
        msgContent.put("vlogCover", vlog.getCover());
        msgContent.put("commentId", commentId);
        msgContent.put("commentContent", commentBO.getContent());
        Integer type = MessageEnum.COMMENT_VLOG.type;
        if (StringUtils.isNotBlank(commentBO.getFatherCommentId()) && !commentBO.getFatherCommentId().equals("0")) {
            type = MessageEnum.REPLY_YOU.type;
        }
        msgService.createMsg(commentBO.getCommentUserId(), commentBO.getVlogerId(), type, msgContent);


        return commentVO;
    }


    @Autowired
    private CommentMapperCustom commentMapperCustom;


    @Override
    public PagedGridResult queryVlogComments(String vlogId, String userId, Integer page, Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("vlogId", vlogId);

        PageHelper.startPage(page, pageSize);
        List<CommentVO> list = commentMapperCustom.getCommentList(map);

        for (CommentVO cv : list) {
            String commentId = cv.getCommentId();

            // 当前视频的总点赞数，redis实时，数据库有延迟并且不精确，所以以redis为主
            String countsStr = redis.getHashValue(REDIS_VLOG_COMMENT_LIKED_COUNTS, commentId);
            Integer counts = 0;
            if (StringUtils.isNotBlank(countsStr)) {
                counts = Integer.valueOf(countsStr);
            }
            cv.setLikeCounts(counts);

            // 当前用户是否点赞过该视频
            String doILike = redis.hget(REDIS_USER_LIKE_COMMENT, userId + ":" + commentId);
            if (StringUtils.isNotBlank(doILike) && doILike.equalsIgnoreCase("1")) {
                cv.setIsLike(YesOrNo.YES.type);
            }
        }

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

    @Override
    public Comment getComment(String id) {
        return commentMapper.selectByPrimaryKey(id);
    }
}
