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
        for (int i = 0; i <= 10; i ++) {
            SyncQyMessageBO bo = new SyncQyMessageBO();
            bo.setId(String.valueOf(i));
            bo.setName("456");
            testBusinessQueue.pushMessage(() -> JSONObject.toJSONString(bo));
        }
    }


}
