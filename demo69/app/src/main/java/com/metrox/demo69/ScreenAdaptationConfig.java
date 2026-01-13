package com.metrox.demo69;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * 屏幕适配配置类
 * 在Application中初始化
 */
public class ScreenAdaptationConfig {

    private static float sNonCompatDensity;
    private static float sNonCompatScaledDensity;

    /**
     * 基于宽度进行适配（默认设计图宽度为375dp）
     */
    public static void setCustomDensity(Activity activity, Application application) {
        setCustomDensity(activity, application, 375);
    }

    /**
     * 基于宽度进行适配
     * @param designWidthDp 设计图宽度（单位：dp）
     */
    public static void setCustomDensity(Activity activity, Application application, float designWidthDp) {
        final Application app = application;
        final DisplayMetrics appDisplayMetrics = app.getResources().getDisplayMetrics();

        if (sNonCompatDensity == 0) {
            sNonCompatDensity = appDisplayMetrics.density;
            sNonCompatScaledDensity = appDisplayMetrics.scaledDensity;

            // 监听字体变化
            app.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNonCompatScaledDensity = app.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {
                }
            });
        }

        // 计算目标密度
        final float targetDensity = appDisplayMetrics.widthPixels / designWidthDp;
        final float targetScaledDensity = targetDensity * (sNonCompatScaledDensity / sNonCompatDensity);
        final int targetDensityDpi = (int) (160 * targetDensity);

        // 设置应用密度
        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        // 设置Activity密度
        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }

    /**
     * 重置屏幕适配
     */
    public static void resetDensity(Activity activity) {
        final DisplayMetrics appDisplayMetrics = activity.getApplication().getResources().getDisplayMetrics();
        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();

        appDisplayMetrics.density = sNonCompatDensity;
        appDisplayMetrics.scaledDensity = sNonCompatScaledDensity;
        appDisplayMetrics.densityDpi = (int) (sNonCompatDensity * 160);

        activityDisplayMetrics.density = sNonCompatDensity;
        activityDisplayMetrics.scaledDensity = sNonCompatScaledDensity;
        activityDisplayMetrics.densityDpi = (int) (sNonCompatDensity * 160);
    }

    /**
     * 检查设备是否为平板
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 获取设备屏幕分类
     */
    public static String getScreenCategory(Context context) {
        int screenLayout = context.getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                return "小屏幕手机";
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return "普通手机";
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                return "大屏幕设备";
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                return "平板设备";
            default:
                return "未知设备";
        }
    }
}
