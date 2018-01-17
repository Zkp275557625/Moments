package com.zhoukp.photo;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * auther：zhoukp
 * time：2018/1/15 15:05
 * mail：zhoukaiping@szy.cn
 * for：
 */

public class PhotoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
    }
}
