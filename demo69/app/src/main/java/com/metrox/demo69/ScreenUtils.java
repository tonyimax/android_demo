package com.metrox.demo69;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ScreenUtils {

    /**
     * 获取屏幕宽度（像素）
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return 0;

        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度（像素）
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return 0;

        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取屏幕密度DPI
     */
    public static int getScreenDensityDPI(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    /**
     * 获取屏幕密度（比例）
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     */
    public static int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px
     */
    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 判断是否是横屏
     */
    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 判断是否是小屏幕设备
     */
    public static boolean isSmallScreen(Context context) {
        return context.getResources().getConfiguration().smallestScreenWidthDp < 320;
    }

    /**
     * 判断是否是大屏幕设备（平板）
     */
    public static boolean isLargeScreen(Context context) {
        return context.getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }

    /**
     * 判断是否是中等屏幕设备
     */
    public static boolean isMediumScreen(Context context) {
        Configuration config = context.getResources().getConfiguration();
        return config.smallestScreenWidthDp >= 320 && config.smallestScreenWidthDp < 600;
    }

    /**
     * 获取屏幕方向
     */
    public static String getScreenOrientationText(Context context) {
        return isLandscape(context) ? "横屏" : "竖屏";
    }

    /**
     * 获取设备类型描述
     */
    public static String getDeviceTypeText(Context context) {
        if (isSmallScreen(context)) return "小屏幕手机";
        if (isMediumScreen(context)) return "普通手机";
        if (isLargeScreen(context)) return "平板设备";
        return "未知设备";
    }
}