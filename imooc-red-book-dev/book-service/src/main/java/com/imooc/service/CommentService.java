package com.imooc.service;

import com.imooc.bo.CommentBO;
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
}

