package com.cas.config.confirm;

import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @Author : JCccc
 * @CreateTime : 2019/9/3
 * @Description : 生产者用
 * 这一层走逻辑做补偿
 * 消息发送确认
 **/
@Configuration
public class RabbitConfig {

    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        //Mandatory为true时,消息通过交换器无法匹配到队列会返回给生产者
        //         为false时,匹配不到会直接被丢弃
        // true匹配不到队列时，会通过ReturnsCallback返回
        // flase匹配不到队列时，直接丢弃数据
        rabbitTemplate.setMandatory(true);

        /**
         * ConfirmCallback能确定到交换机
         */
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            System.out.println("ConfirmCallback:     "+"相关数据："+correlationData);
            System.out.println("ConfirmCallback:     "+"确认情况："+ack);
            System.out.println("ConfirmCallback:     "+"原因："+cause);
        });

        /**
         * ReturnCallback 能确定到队列
         * ①消息推送到server，但是在server里找不到交换机  ConfirmCallback
         * ②消息推送到server，找到交换机了，但是没找到队列  ConfirmCallback和 ReturnCallback
         * ③消息推送到sever，交换机和队列啥都没找到 ConfirmCallback
         * ④消息推送成功 ConfirmCallback
         */
        rabbitTemplate.setReturnsCallback(returned -> {
            // 消息到了交换机但没有到队列，回调此方法，要采取补偿机制。但一般情况下可以不设置，根据消息重要程度设置
            System.out.println(JSONObject.toJSONString(returned));
            System.out.println("ReturnsCallback:     "+"Exchange: "+returned.getExchange());
            System.out.println("ReturnsCallback:     "+"ReplyText: "+returned.getReplyText());
            System.out.println("ReturnsCallback:     "+"RoutingKey: "+returned.getRoutingKey());
            System.out.println("ReturnsCallback:     "+"Message: "+returned.getMessage());
            System.out.println("ReturnsCallback:     "+"ReplyCode: "+returned.getReplyCode());
        });

        return rabbitTemplate;
    }

}
