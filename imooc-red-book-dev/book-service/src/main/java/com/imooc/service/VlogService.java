package com.imooc.service;

import com.imooc.bo.VlogBO;
import com.imooc.pojo.Vlog;
import com.imooc.utils.PagedGridResult;
import com.imooc.vo.IndexVlogVO;

import java.util.List;

/**
 * @author 包建丰
 * @date 2021/11/25 09 :43
 * @description
 **/
public interface VlogService {
    /**
     * 新增视频vlog
     */
    public void createVlog(VlogBO vlogBO);

    /**
     * 查询首页的短视频list
     */
    public PagedGridResult queryIndexVlogList(String userId, String search, Integer page, Integer pageSize);


    /**
     * 根据主键查询vlog具体内容
     */
    public IndexVlogVO getVlogDetail(String userId,String vlogId);

    /**
     * 用户把视频改为公开/私密视频
     */
    public void changeToPrivateOrPublic(String userId,
                                        String vlogId,
                                        Integer yesOrNo);

    /**
     * 查询用户的公开/私密的短视频
     */
    public PagedGridResult queryMyVlogList(String userId, Integer page,
                                           Integer pageSize, Integer yesOrNo);

    /**
     * 用户喜欢/点赞视频
     */
    public void userLikeVolg(String vlogId, String userId);

    /**
     * 用户取消喜欢/点赞视频
     */
    public void userUnLikeVolg(String vlogId, String userId);

    // 获得用户点赞视频的总数
    public Integer getVlogBeLikedCounts(String vlogId);

    /**
     * 查询用户点赞的短视频
     */
    public PagedGridResult queryMyLikedList(String userId, Integer page, Integer pageSize);

    /**
     * 查询我关注博主的短视频list
     */
    public PagedGridResult getMyFollowVlogList(String myId, Integer page, Integer pageSize);

    /**
     * 查询朋友页面的短视频list
     */
    public PagedGridResult getMyFriendVlogList(String myId, Integer page, Integer pageSize);


    //根据主键查询 vlog
    public Vlog getVlog(String id);

    /**
     * 把counts数值刷入数据库
     */
    public void flushCounts(String vlogId, Integer counts);

}
