package com.imooc.service;

import com.imooc.bo.VlogBO;
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
    public PagedGridResult queryIndexVlogList(String search, Integer page, Integer pageSize);


    /**
     * 根据主键查询vlog具体内容
     */
    public IndexVlogVO getVlogDetail(String vlogId);

    /**
     * 用户把视频改为公开/私密视频
     */
    public void changeToPrivateOrPublic(String userId,
                                        String vlogId,
                                        Integer yesOrNo);


}
