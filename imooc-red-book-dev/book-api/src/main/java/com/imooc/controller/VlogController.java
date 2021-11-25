package com.imooc.controller;

import com.imooc.bo.VlogBO;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.service.VlogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 包建丰
 * @date 2021/11/25 12 :12
 * @description
 **/

@Api(tags = "VlogController Vlog短视频功能模块")
@Slf4j
@RestController
@RequestMapping("vlog")
public class VlogController {

    @Autowired
    private VlogService vlogService;

    @ApiOperation(value = "发布视频")
    @PostMapping("publish")
    public GraceJSONResult publish(@RequestBody VlogBO vlogBO) {

        // TODO BO校验作为作业
        vlogService.createVlog(vlogBO);
        return GraceJSONResult.ok();
    }
}
