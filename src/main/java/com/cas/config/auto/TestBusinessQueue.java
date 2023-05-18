package com.cas.config.auto;

import org.springframework.stereotype.Service;

@Service
public class TestBusinessQueue extends AbstractAutoInitQueueBeanFactory{

    @Override
    public String getQueueName() {
        return "test";
    }

    @Override
    public String getExchangeName() {
        return "test";
    }

    @Override
    public String getRoutingKeyName() {
        return "test";
    }
}
