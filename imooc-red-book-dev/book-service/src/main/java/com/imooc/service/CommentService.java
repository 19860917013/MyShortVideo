package com.imooc.service;

import com.imooc.bo.CommentBO;
import com.imooc.pojo.Comment;
import com.imooc.utils.PagedGridResult;
import com.imooc.vo.CommentVO;

/**
 * @author 包建丰
 * @date 2021/11/25 21 :41
 * @description
 **/
public interface CommentService {

    /**
     * 发表评论
     */
    public CommentVO createComment(CommentBO commentBO);

    /**
     * 查询短视频的评论列表
     */
    public PagedGridResult queryVlogComments(String vlogId,
                                             String userId,
                                             Integer page,
                                             Integer pageSize);

    /**
     * 删除评论
     */
    public void deleteComment(String commentUserId, String commentId, String vlogId);


    // 根据主键查询id
    public Comment getComment(String id);
}

