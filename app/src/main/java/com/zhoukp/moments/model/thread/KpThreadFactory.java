package com.zhoukp.moments.model.thread;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadFactory;

/**
 * time：2018/1/16 9:26
 * mail：zhoukaiping@szy.cn
 * for：线程池
 *
 * @author zhoukp
 */

public class KpThreadFactory implements ThreadFactory {
    /**
     * 线程的名字
     */
    protected String name;
    /**
     * 已创建的线程的数量
     */
    private int count;
    /**
     * 线程的状态
     */
    private List<String> stats;

    /**
     * 获取线程的状态
     *
     * @return String
     */
    public String getStats() {
        StringBuilder stingBuider = new StringBuilder();
        for (String stat : stats) {
            stingBuider.append(stat);
            stingBuider.append("\n");
        }
        return stingBuider.toString();
    }

    public KpThreadFactory(String name) {
        this.name = name;
        this.stats = new ArrayList<>();
    }

    @Override
    public Thread newThread(@NonNull Runnable runnable) {
        Thread thread = new Thread(runnable, name + "-Thread_" + count);
        count++;
        stats.add(String.format("Create thread %d with name %s on %s\n",
                thread.getId(), thread.getName(), new Date()));

        return thread;
    }
}
