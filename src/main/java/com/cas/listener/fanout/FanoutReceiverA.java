package com.cas.listener.fanout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author : JCccc
 * @CreateTime : 2019/9/3
 * @Description :
 **/
//@Component
//@RabbitListener(queues = "fanout.A")
public class FanoutReceiverA {
    private static Logger log = LoggerFactory.getLogger(FanoutReceiverA.class);

    @RabbitHandler
    public void process(Map testMessage) {
        log.info("FanoutReceiverA消费者收到消息  : " +testMessage.toString());
    }

}
