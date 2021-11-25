package com.imooc.service.impl;

import com.github.pagehelper.PageHelper;
import com.imooc.base.BaseInfoProperties;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.FansMapper;
import com.imooc.mapper.FansMapperCustom;
import com.imooc.pojo.Fans;
import com.imooc.service.FansService;
import com.imooc.utils.PagedGridResult;
import com.imooc.vo.FansVO;
import com.imooc.vo.VlogerVO;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 包建丰
 * @date 2021/11/25 18 :03
 * @description
 **/

@Service
public class FansServiceImpl extends BaseInfoProperties implements FansService {
    @Autowired
    private FansMapper fansMapper;

    @Autowired
    private Sid sid;

    // 判断对方是否关注我
    public Fans queryFansRelationship(String fanId, String vlogerId) {
        Example example = new Example(Fans.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("fanId", fanId);
        criteria.andEqualTo("vlogerId", vlogerId);
        List list = fansMapper.selectByExample(example);
        Fans fan = null;
        if (list != null && !list.isEmpty() && list.size() > 0) {
            fan = (Fans) list.get(0);
        }
        return fan;
    }

    @Transactional
    @Override
    public void doFollow(String myId, String vlogerId) {

        String fid = sid.nextShort();

        Fans fans = new Fans();
        fans.setId(fid);
        fans.setFanId(myId);
        fans.setVlogerId(vlogerId);

        // 判断对方是否关注我，如果关注我，那么双方都要互为朋友关系
        Fans vloger = queryFansRelationship(vlogerId, myId);

        if (vloger != null) {
            fans.setIsFanFriendOfMine(YesOrNo.YES.type);

            // 把对方的粉丝关系也改成朋友关系
            vloger.setIsFanFriendOfMine(YesOrNo.YES.type);
            fansMapper.updateByPrimaryKeySelective(vloger);
        } else {
            fans.setIsFanFriendOfMine(YesOrNo.NO.type);
        }

        fansMapper.insert(fans);
    }

    @Transactional
    @Override
    public void doCancel(String myId, String vlogerId) {
        // 判断是否朋友关系，如果是，则需要取消双方的关系
        Fans fan = queryFansRelationship(myId, vlogerId);
        if (fan != null && fan.getIsFanFriendOfMine() == 1) {
            // 抹除对方关联表的朋友关系，自己的关系删了就行
            Fans pendingFan = queryFansRelationship(vlogerId, myId);
            pendingFan.setIsFanFriendOfMine(YesOrNo.NO.type);
            fansMapper.updateByPrimaryKeySelective(pendingFan);
        }

        // 删除自己的关注关联关系
        fansMapper.delete(fan);

    }

    @Override
    public boolean queryDoIFollowVloger(String myId, String vlogerId) {
        Fans fan = queryFansRelationship(myId, vlogerId);
        return fan != null;
    }

    @Autowired
    private FansMapperCustom fansMapperCustom;

    @Override
    public PagedGridResult queryMyFollows(String myId, Integer page, Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("myId", myId);

        PageHelper.startPage(page, pageSize);
        List<VlogerVO> list = fansMapperCustom.queryMyFollows(map);

        return setterPagedGrid(list, page);
    }

    @Override
    public PagedGridResult queryMyFans(String myId, Integer page, Integer pageSize) {


        Map<String, Object> map = new HashMap<>();
        map.put("myId", myId);

        PageHelper.startPage(page, pageSize);
        List<FansVO> list = fansMapperCustom.queryMyFans(map);

        return setterPagedGrid(list, page);
    }


}
