package com.imooc.exceptions;

/**
 * @author 包建丰
 * @date 2021/11/23 20 :42
 * @description
 **/

import com.imooc.grace.result.GraceJSONResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常拦截处理
 * 可以针对异常的类型进行捕获，然后返回json信息到前端
 */
@ControllerAdvice
public class GraceExceptionHandler {

    @ExceptionHandler(MyCustomException.class)
    @ResponseBody
    public GraceJSONResult returnMyException(MyCustomException e) {
//        e.printStackTrace();
        return GraceJSONResult.exception(e.getResponseStatusEnum());
    }
}


