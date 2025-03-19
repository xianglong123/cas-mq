package com.cas.config.dealy;

import com.alibaba.fastjson.JSON;
import com.cas.config.auto.bo.SyncQyMessageBO;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

import static com.cas.config.dealy.RabbitMQConfig.DEAD_LETTER_QUEUEA_NAME;
import static com.cas.config.dealy.RabbitMQConfig.DEAD_LETTER_QUEUEB_NAME;

/**
 * @author xianglong
 */
@Component
public class DeadLetterQueueConsumer {
    private static final Logger log = LoggerFactory.getLogger(DeadLetterQueueConsumer.class);

    @RabbitListener(queues = DEAD_LETTER_QUEUEA_NAME)
    public void receiveA(Message message, Channel channel) throws IOException {
        try {
            String msg = new String(message.getBody());
            log.info("当前时间：{},死信队列A收到消息：{}", new Date().toString(), msg);
            SyncQyMessageBO syncQyMessageBO = JSON.parseObject(msg, SyncQyMessageBO.class);
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

    @RabbitListener(queues = DEAD_LETTER_QUEUEB_NAME)
    public void receiveB(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody());
        log.info("当前时间：{},死信队列B收到消息：{}", new Date().toString(), msg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}