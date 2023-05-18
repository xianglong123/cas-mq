package com.cas.config.auto;

import java.util.function.Supplier;

public interface AutoInitQueueBeanFactory {

    /**
     * 队列名
     * 创建的时候会自动拼 mirror.${spring.application.name}. + getQueueName()
     * @return
     */
    String getQueueName();

    /**
     * 交换机
     * 创建的时候会自动拼 mirror.${spring.application.name}. + getExchangeName()
     * @return
     */
    String getExchangeName();

    /**
     * 路由key
     * 创建的时候会自动拼 mirror.${spring.application.name}. + getRoutingKeyName()
     * @return
     */
    String getRoutingKeyName();

    void pushMessage(Supplier<Object> supplier);

}
