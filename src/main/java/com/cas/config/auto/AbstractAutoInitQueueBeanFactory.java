package com.cas.config.auto;

import com.cas.utils.ThreadPoolUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.util.function.Supplier;

public abstract class AbstractAutoInitQueueBeanFactory implements AutoInitQueueBeanFactory, EnvironmentAware {

    private static final String MQ_QUEUE_PREFIX = "mirror";
    private static final String MQ_QUEUE_DELAY = "-delay-";
    private static final String PARTITION = ".";
    private static final String TIME_UNIT = "s";
    private static String EXCHANGE;
    private static String DELAY_EXCHANGE;
    private static String ROUTINGKEY;
    private static String DELAY_ROUTINGKEY;

    @Resource
    private ThreadPoolUtil threadPoolUtil;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void pushMessage(Supplier<Object> supplier) {
        threadPoolUtil.execute(() -> rabbitTemplate.convertSendAndReceive(EXCHANGE, ROUTINGKEY, supplier.get()));
    }

    @Override
    public void pushDelayMessage(Supplier<Object> supplier) {
        if (getDelayTime() == null) {
            System.out.println("延迟队列未设置ttl，请设置ttl再调用");
            return ;
        }
        threadPoolUtil.execute(() -> rabbitTemplate.convertSendAndReceive(DELAY_EXCHANGE, DELAY_ROUTINGKEY, supplier.get()));
    }

    @Override
    public Long getDelayTime() {
        return null;
    }

    @Override
    public void setEnvironment(Environment environment) {
        String MQ_PREFIX = MQ_QUEUE_PREFIX + PARTITION + environment.getProperty("spring.application.name") + PARTITION;
        EXCHANGE = MQ_PREFIX + getExchangeName();
        ROUTINGKEY = MQ_PREFIX + getRoutingKeyName();
        DELAY_EXCHANGE = EXCHANGE + MQ_QUEUE_DELAY + getDelayTime() + TIME_UNIT;
        DELAY_ROUTINGKEY = ROUTINGKEY + MQ_QUEUE_DELAY + getDelayTime() + TIME_UNIT;
    }
}
