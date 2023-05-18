package com.cas.config.auto;

import com.cas.utils.ThreadPoolUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.util.function.Supplier;

public abstract class AbstractAutoInitQueueBeanFactory implements AutoInitQueueBeanFactory, EnvironmentAware {

    private static final String MQ_QUEUE_PREFIX = "mirror";

    private static final String PARTITION = ".";
    private static String MQ_PREFIX;

    @Resource
    private ThreadPoolUtil threadPoolUtil;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void pushMessage(Supplier<Object> supplier) {
        threadPoolUtil.execute(() -> rabbitTemplate.convertSendAndReceive(MQ_PREFIX + getExchangeName(), MQ_PREFIX + getRoutingKeyName(), supplier.get()));
    }

    @Override
    public void setEnvironment(Environment environment) {
        MQ_PREFIX = MQ_QUEUE_PREFIX + PARTITION + environment.getProperty("spring.application.name") + PARTITION;;
    }
}
