package com.me.caec.activity;

import android.app.Application;

import org.xutils.BuildConfig;
import org.xutils.x;

/**
 * application
 * Created by yin on 2016/8/29.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }
}
