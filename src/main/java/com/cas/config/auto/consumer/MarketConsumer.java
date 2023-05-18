package com.cas.config.auto.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * @author xiang_long
 * @version 1.0
 * @date 2022/4/12 10:36 上午
 * @desc 异步通知消费者
 */
@Component
public class MarketConsumer {

    @RabbitListener(queues = "mirror.cas-mq.test")
    public void handler(Message message, Channel channel) throws IOException {
        try {
            String id = new String(message.getBody());
            System.out.println(id);
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

}
