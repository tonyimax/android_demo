package com.metrox.demo69;

import android.app.Application;
import android.content.res.Configuration;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 全局初始化
        initScreenAdaptation();
    }

    private void initScreenAdaptation() {
        // 可以在这里初始化全局的屏幕适配配置
        // 例如：设置默认设计图尺寸、初始化第三方适配库等
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 处理全局配置变化
    }
}
