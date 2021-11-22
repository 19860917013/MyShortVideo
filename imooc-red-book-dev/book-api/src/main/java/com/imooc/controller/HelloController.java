package com.imooc.controller;

import com.imooc.grace.result.GraceJSONResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 包建丰
 * @date 2021/11/22 19 :41
 * @description
 **/

@RestController
public class HelloController {
    @GetMapping("/hello")
    public Object hello() {
        return GraceJSONResult.ok("Hello ,SpringBoot");
    }
}
