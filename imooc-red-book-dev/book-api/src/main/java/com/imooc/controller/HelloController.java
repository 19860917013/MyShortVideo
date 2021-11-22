package com.imooc.controller;

import com.imooc.grace.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 包建丰
 * @date 2021/11/22 19 :41
 * @description
 **/
@Slf4j
@Api(tags = "Hello 测试的接口")
@RestController
public class HelloController {

    @ApiOperation(value = "hello-这是一个hello的测试路由")
    @GetMapping("/hello")
    public Object hello() {
        return GraceJSONResult.ok("Hello ,SpringBoot");
    }
}
