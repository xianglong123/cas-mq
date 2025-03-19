package com.cas.config.ack;

import com.cas.handler.CustomErrorHandler;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

import static com.cas.config.dealy.RabbitMQConfig.DEAD_LETTER_QUEUEA_NAME;

/**
 * @Author : JCccc
 * @CreateTime : 2019/9/4
 * @Description : 消费者用
 **/
@Configuration
public class MessageListenerConfig {

    @Autowired
    private CachingConnectionFactory connectionFactory;
    @Autowired
    private MyAckReceiver myAckReceiver;//消息接收处理类
    @Resource
    private CustomErrorHandler customErrorHandler;

    /**
     * 这里的配置类似于没有注解的情况下可以采用这类方式，创建消费容器。可以针对不同的容器采用更细粒度的配置
     * 注意：这里的配置，不适用于 @RabbitmqListener 配置的容器
     * 如果两者都配置，这个配置的优先级更高
     */
//    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setConcurrentConsumers(10);
        container.setMaxConcurrentConsumers(10);
        // RabbitMQ默认是自动确认，这里改为手动确认消息
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//        设置一个队列
        container.setQueueNames(DEAD_LETTER_QUEUEA_NAME);
        //如果同时设置多个如下： 前提是队列都是必须已经创建存在的
//        container.setQueueNames("TestDirectQueue", "fanout.A");


        //另一种设置队列的方法,如果使用这种情况,那么要设置多个,就使用addQueues
        //container.setQueues(new Queue("TestDirectQueue",true));
        //container.addQueues(new Queue("TestDirectQueue2",true));
        //container.addQueues(new Queue("TestDirectQueue3",true));
        container.setMessageListener(myAckReceiver);

        return container;
    }

    /**
     * 这里的配置主要应用于 @RabbitmqListener 的消费容器，全局可用
     * @param connectionFactory
     * @return
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
//        factory.createListenerContainer().shutdown();
        // 手动确认
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        // 下面这个信息转换器作用是主动帮你做序列化的操作， 不要和手动序列化公用，不然消费者那里会出问题，不推荐使用全局转换器
        // todo 不推荐使用下面的转换器
        // factory.setMessageConverter(new Jackson2JsonMessageConverter());
//        factory.setErrorHandler(null);
        return factory;
    }


}
