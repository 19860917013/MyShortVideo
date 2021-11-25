package com.imooc.service;

/**
 * @author 包建丰
 * @date 2021/11/25 18 :01
 * @description
 **/
public interface FansService {

    /**
     * 关注
     */
    public void doFollow(String myId, String vlogerId);

    /**
     * 取关
     */
    public void doCancel(String myId, String vlogerId);

    /**
     * 查询用户是否关注博主
     */
    public boolean queryDoIFollowVloger(String myId, String vlogerId);


}
