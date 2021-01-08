package com.cas.listener.topic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author : xl
 * @CreateTime : 20120/5/31
 * @Description :
 **/
//@Component
//@RabbitListener(queues = "topic.woman")
public class TopicTotalReceiver {

    private static Logger log = LoggerFactory.getLogger(TopicTotalReceiver.class);

    @RabbitHandler
    public void process(Map testMessage) {
        log.info("TopicTotalReceiver消费者收到消息  : " + testMessage.toString());
    }
}
