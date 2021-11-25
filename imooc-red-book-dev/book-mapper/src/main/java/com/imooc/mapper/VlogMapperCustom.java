package com.imooc.mapper;

import com.imooc.my.mapper.MyMapper;
import com.imooc.pojo.Vlog;
import com.imooc.vo.IndexVlogVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface VlogMapperCustom {

    public List<IndexVlogVO> getIndexVlogList(@Param("paramMap") Map<String, Object> map);

}
