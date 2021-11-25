package com.imooc.service.impl;

import com.imooc.bo.VlogBO;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.VlogMapper;
import com.imooc.pojo.Vlog;
import com.imooc.service.VlogService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author 包建丰
 * @date 2021/11/25 09 :44
 * @description
 **/

@Service
public class VlogServiceImpl implements VlogService {

    @Autowired
    private VlogMapper vlogMapper;

    @Autowired
    private Sid sid;

    @Transactional
    @Override
    public void createVlog(VlogBO vlogBO) {

        Vlog vlog = new Vlog();
        BeanUtils.copyProperties(vlogBO, vlog);

        String vid = sid.nextShort();
        vlog.setId(vid);
        vlog.setLikeCounts(0);
        vlog.setCommentsCounts(0);
        vlog.setIsPrivate(YesOrNo.NO.type);

        vlog.setCreatedTime(new Date());
        vlog.setUpdatedTime(new Date());

        vlogMapper.insert(vlog);
    }
}
