package com.zhys.whiteboard;

import android.app.Application;

/**
 * @author zhouys
 * @date 2023/4/3
 * @description
 */
public class WhiteBoardApplication extends Application {
    public static WhiteBoardApplication sContext;
    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }
}
