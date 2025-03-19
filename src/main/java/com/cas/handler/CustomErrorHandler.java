package com.cas.handler;

import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * 貌似只能针对 @RabbitListener 的异常处理
 * 有点傻逼的设计，只能监控异常。不能处理确认状态
 */
@Component
public class CustomErrorHandler implements RabbitListenerErrorHandler {

    @Override
    public Object handleError(org.springframework.amqp.core.Message message, Message<?> message1, ListenerExecutionFailedException exception) throws Exception {

        // 自定义异常处理逻辑
        System.err.println("Error processing message: " + message);
        exception.printStackTrace();
        System.out.println("++++++++++++自定义异常处理被触发++++++++++++");
        // 返回 null 表示不重新投递消息
        return null;
    }
}
