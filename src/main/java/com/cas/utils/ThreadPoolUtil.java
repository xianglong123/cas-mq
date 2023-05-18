package com.cas.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 16:43 2020-05-11
 * @version: V1.0
 * @review: 线程池复用
 */
public class ThreadPoolUtil {

    private static ThreadPoolExecutor poolExecutor = null;

    {
        // 核心线程池5，最长闲置时间30秒，拒绝策略：忽略
        poolExecutor = new ThreadPoolExecutor(5, 5, 30L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(12), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * 调用线程池的方法添加任务
     * @param commond
     */
    public void execute(Runnable commond) {
        if (poolExecutor == null) {
            poolExecutor = new ThreadPoolExecutor(5, 5, 30L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(12), new ThreadPoolExecutor.CallerRunsPolicy());
        }
        poolExecutor.execute(commond);
    }


}
