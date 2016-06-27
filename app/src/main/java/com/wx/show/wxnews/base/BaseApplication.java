package com.wx.show.wxnews.base;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Luka on 2016/6/27.
 * 397308937@qq.com
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
