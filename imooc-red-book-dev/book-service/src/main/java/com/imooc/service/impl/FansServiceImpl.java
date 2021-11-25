package com.imooc.service.impl;

import com.imooc.base.BaseInfoProperties;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.FansMapper;
import com.imooc.pojo.Fans;
import com.imooc.service.FansService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

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

}
