package com.imooc.service.impl;

import com.github.pagehelper.PageHelper;
import com.imooc.base.BaseInfoProperties;
import com.imooc.bo.VlogBO;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.MyLikedVlogMapper;
import com.imooc.mapper.VlogMapper;
import com.imooc.mapper.VlogMapperCustom;
import com.imooc.pojo.MyLikedVlog;
import com.imooc.pojo.Vlog;
import com.imooc.service.VlogService;
import com.imooc.utils.PagedGridResult;
import com.imooc.vo.IndexVlogVO;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 包建丰
 * @date 2021/11/25 09 :44
 * @description
 **/

@Service
public class VlogServiceImpl extends BaseInfoProperties implements VlogService {

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

    @Autowired
    private VlogMapperCustom vlogMapperCustom;

    @Override
    public PagedGridResult queryIndexVlogList(String search, Integer page, Integer pageSize) {

        /**
         * page: 第几页
         * pageSize: 每页显示条数
         */
        PageHelper.startPage(page, pageSize);

        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(search)) {
            map.put("search", search);
        }
        List<IndexVlogVO> list = vlogMapperCustom.getIndexVlogList(map);

        return setterPagedGrid(list, page);
//    return list;
    }

    @Override
    public IndexVlogVO getVlogDetail(String vlogId) {
        Map map = new HashMap();
        map.put("vlogId", vlogId);
        List<IndexVlogVO> list = vlogMapperCustom.getVlogDetailById(map);

        if (list != null && list.size() > 0 && !list.isEmpty()) {
            IndexVlogVO vlogDetail = list.get(0);
            return vlogDetail;
        }

        return null;
    }

    @Transactional
    @Override
    public void changeToPrivateOrPublic(String userId, String vlogId, Integer yesOrNo) {
        Example vlogExample = new Example(Vlog.class);
        Example.Criteria vlogCriteria = vlogExample.createCriteria();
        vlogCriteria.andEqualTo("id", vlogId);
        vlogCriteria.andEqualTo("vlogerId", userId);

        Vlog pendingPrivate = new Vlog();
        pendingPrivate.setIsPrivate(yesOrNo);

        vlogMapper.updateByExampleSelective(pendingPrivate, vlogExample);
    }

    @Override
    public PagedGridResult queryMyVlogList(String userId, Integer page, Integer pageSize, Integer yesOrNo) {

        Example vlogExample = new Example(Vlog.class);
        Example.Criteria vlogCriteria = vlogExample.createCriteria();
        vlogCriteria.andEqualTo("vlogerId", userId);
        vlogCriteria.andEqualTo("isPrivate", yesOrNo);

        PageHelper.startPage(page, pageSize);
        List<Vlog> list = vlogMapper.selectByExample(vlogExample);

        return setterPagedGrid(list, page);
    }

    @Autowired
    private MyLikedVlogMapper myLikedVlogMapper;


    @Transactional
    @Override
    public void userLikeVolg(String vlogId, String userId) {
        String rid = sid.nextShort();

        MyLikedVlog myLikedVlog = new MyLikedVlog();
        myLikedVlog.setId(rid);
        myLikedVlog.setUserId(userId);
        myLikedVlog.setVlogId(vlogId);

        myLikedVlogMapper.insert(myLikedVlog);
    }


}
