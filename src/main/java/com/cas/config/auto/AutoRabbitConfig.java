package com.cas.config.auto;

import com.cas.utils.ThreadPoolUtil;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;


/**
 * 自动创建队列、交换机、routingKey
 */
@Configuration
@ConditionalOnProperty(prefix = "dicp.market", name = "syncQueue", havingValue = "true")
@Import({ThreadPoolUtil.class})
public class AutoRabbitConfig implements EnvironmentAware {

    private static final String MQ_QUEUE_PREFIX = "mirror";

    private static final String MQ_QUEUE_DELAY = "-delay-";
    private static final String TIME_UNIT = "s";

    private static final String PARTITION = ".";

    private final AutoInitQueueBeanFactory[] beanFactories;

    private static String MQ_PREFIX;

    public AutoRabbitConfig(ObjectProvider<AutoInitQueueBeanFactory[]> beanFactories) {
        this.beanFactories = beanFactories.getIfAvailable();
    }

    @Bean
    public RabbitAdmin ampqManager(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        // 直连队列创建
        for (AutoInitQueueBeanFactory factory : beanFactories) {
            binding(rabbitAdmin, factory);
        }
        return rabbitAdmin;
    }

    /**
     * 暂时只支持Direct模式、其他模式请自己开发
     * @param rabbitAdmin
     * @param autoInitQueueBeanFactory
     */
    private void binding(RabbitAdmin rabbitAdmin, AutoInitQueueBeanFactory autoInitQueueBeanFactory) {
        String exchange = MQ_PREFIX +  autoInitQueueBeanFactory.getExchangeName();
        String queue = MQ_PREFIX + autoInitQueueBeanFactory.getQueueName();
        String routingKey = MQ_PREFIX + autoInitQueueBeanFactory.getRoutingKeyName();
        // 申明交换机
        rabbitAdmin.declareExchange(new DirectExchange(exchange));

        // 声明队列 direct
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-queue-master-locator", "random");
        rabbitAdmin.declareQueue(new Queue(queue, true, false, false, arguments));
        // 交换机和队列的绑定
        rabbitAdmin.declareBinding(new Binding(queue, Binding.DestinationType.QUEUE, exchange, routingKey, null));
        if (autoInitQueueBeanFactory.getDelayTime() != null) {
            // 有延迟需求
            Map<String, Object> args = new HashMap<>();
            Long delayTime = autoInitQueueBeanFactory.getDelayTime();
            args.put("x-message-ttl", delayTime);
            args.put("x-dead-letter-exchange", exchange);
            args.put("x-dead-letter-routing-key", routingKey);

            String delayExchange = exchange + MQ_QUEUE_DELAY + delayTime + TIME_UNIT;
            String delayQueue = queue + MQ_QUEUE_DELAY + delayTime + TIME_UNIT;
            String delayRoutingKey = routingKey + MQ_QUEUE_DELAY + delayTime + TIME_UNIT;

            // 申明交换机
            rabbitAdmin.declareExchange(new DirectExchange(delayExchange));
            // 声明延迟队列
            rabbitAdmin.declareQueue(new Queue(delayQueue, true, false, false, args));
            // 交换机和队列的绑定
            rabbitAdmin.declareBinding(new Binding(delayQueue, Binding.DestinationType.QUEUE, delayExchange, delayRoutingKey, null));
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        MQ_PREFIX = MQ_QUEUE_PREFIX + PARTITION + environment.getProperty("spring.application.name") + PARTITION;;
    }
}
