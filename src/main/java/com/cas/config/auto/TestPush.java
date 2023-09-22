package com.cas.config.auto;

import com.alibaba.fastjson.JSONObject;
import com.cas.config.auto.bo.SyncQyMessageBO;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class TestPush {

    @Resource
    private TestBusinessQueue testBusinessQueue;

    @PostConstruct
    public void init() {
        SyncQyMessageBO bo = new SyncQyMessageBO();
        bo.setId("123");
        bo.setName("456");
        testBusinessQueue.pushDelayMessage(() -> JSONObject.toJSONString(bo));
    }


}
