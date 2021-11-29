package com.imooc.controller;


import com.imooc.base.RabbitMQConfig;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.utils.SMSUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author 包建丰
 * @date 2021/11/22 19 :41
 * @description
 **/
@Slf4j
@Api(tags = "Hello 测试的接口")
@RestController
@RefreshScope
public class HelloController {

    @Value("${nacos.counts}")
    private Integer nacosCounts;

    @GetMapping("nacosCounts")
    public Object nacosCounts() {
        return GraceJSONResult.ok("nacosCounts的数值为：" + nacosCounts);
    }

    @ApiOperation(value = "hello-这是一个hello的测试路由")
    @GetMapping("/hello")
    public Object hello() {
        return GraceJSONResult.ok("Hello ,SpringBoot");
    }

    @Autowired
    private SMSUtils smsUtils;

    @GetMapping("sms")
    public Object sms() throws Exception {

        String code = "123456";
        smsUtils.sendSMS("19860917013", code);

        return GraceJSONResult.ok();
    }

    @Autowired
    public RabbitTemplate rabbitTemplate;

    @GetMapping("/produce")
    public Object produce() {

        // 向 EXCHANGE_MSG 发送一条消息，并且指定相应的路由规则
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_MSG,
                "sys.msg.send", "发了一个消息~~");

        return GraceJSONResult.ok();
    }

    @GetMapping("produce2")
    public Object produce2() throws Exception {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_MSG,
                "sys.msg.delete",
                "我删除了一个消息~~");

        return GraceJSONResult.ok();
    }

}
