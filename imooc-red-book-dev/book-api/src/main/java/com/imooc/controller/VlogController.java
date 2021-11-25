package com.imooc.controller;

import com.imooc.base.BaseInfoProperties;
import com.imooc.bo.VlogBO;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.service.VlogService;
import com.imooc.utils.PagedGridResult;
import com.imooc.vo.IndexVlogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 包建丰
 * @date 2021/11/25 12 :12
 * @description
 **/

@Api(tags = "VlogController Vlog短视频功能模块")
@Slf4j
@RestController
@RequestMapping("vlog")
public class VlogController extends BaseInfoProperties {

    @Autowired
    private VlogService vlogService;

    @ApiOperation(value = "发布视频")
    @PostMapping("publish")
    public GraceJSONResult publish(@RequestBody VlogBO vlogBO) {

        // TODO BO校验作为作业
        vlogService.createVlog(vlogBO);
        return GraceJSONResult.ok();
    }


    @GetMapping("indexList")
    public GraceJSONResult indexList(@RequestParam(defaultValue = "") String search,
                                     @RequestParam Integer page,
                                     @RequestParam Integer pageSize) {

        // 关于page和pageSize的默认值
        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = vlogService.queryIndexVlogList(search, page, pageSize);
        return GraceJSONResult.ok(gridResult);
    }

    @GetMapping("detail")
    public GraceJSONResult detail(@RequestParam(defaultValue = "") String userId,
                                  @RequestParam String vlogId) {
        IndexVlogVO result = vlogService.getVlogDetail(vlogId);
        return GraceJSONResult.ok(result);
    }


}
