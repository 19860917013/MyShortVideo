package com.imooc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author 包建丰
 */
@Slf4j
@Component
public class RabbitMQConsumer {

    @RabbitListener(queues = {RabbitMQConfig.QUEUE_SYS_MSG})
    public void watchQueue(String payload, Message message) {
        log.info(payload);

        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.info(routingKey);

    }


}

