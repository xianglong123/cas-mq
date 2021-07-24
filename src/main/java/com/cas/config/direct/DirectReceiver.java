package com.cas.config.direct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;

import java.util.Map;

/**
 * 消费者
 */
//@Component
//@RabbitListener(queues = "TestDirectQueue")//监听的队列名称 TestDirectQueue
public class DirectReceiver {

    private static Logger log = LoggerFactory.getLogger(DirectReceiver.class);

    @RabbitHandler
    public void process(Map testMessage) {
        log.info("DirectReceiver消费者收到消息  : " + testMessage.toString());
    }

}
