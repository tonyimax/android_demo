package com.example.demo52;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;
import java.util.Locale;

public class MainActivity extends Activity {

    private TextView statusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusTextView = findViewById(R.id.status_text);
        getDeviceStatus();
    }

    private void getDeviceStatus() {
        Configuration config = getResources().getConfiguration();
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        StringBuilder status = new StringBuilder();

        // 1. 屏幕方向
        status.append("=== 屏幕方向 ===\n");
        switch (config.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                status.append("竖屏模式\n");
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                status.append("横屏模式\n");
                break;
            case Configuration.ORIENTATION_UNDEFINED:
                status.append("未定义\n");
                break;
        }

        // 2. 屏幕尺寸类别
        status.append("\n=== 屏幕尺寸类别 ===\n");
        int screenLayout = config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                status.append("小屏幕\n");
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                status.append("正常屏幕\n");
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                status.append("大屏幕\n");
                break;
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                status.append("超大屏幕\n");
                break;
            case Configuration.SCREENLAYOUT_SIZE_UNDEFINED:
                status.append("未定义\n");
                break;
        }

        // 3. 屏幕长宽模式
        status.append("\n=== 屏幕长宽模式 ===\n");
        int longMode = config.screenLayout & Configuration.SCREENLAYOUT_LONG_MASK;
        switch (longMode) {
            case Configuration.SCREENLAYOUT_LONG_NO:
                status.append("非长屏\n");
                break;
            case Configuration.SCREENLAYOUT_LONG_YES:
                status.append("长屏\n");
                break;
            case Configuration.SCREENLAYOUT_LONG_UNDEFINED:
                status.append("未定义\n");
                break;
        }

        // 4. 屏幕像素密度
        status.append("\n=== 屏幕密度DPI ===\n");
        int densityDpi = config.densityDpi;
        status.append("密度DPI: ").append(densityDpi).append("\n");

        switch (metrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                status.append("低密度 (ldpi)\n");
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                status.append("中密度 (mdpi)\n");
                break;
            case DisplayMetrics.DENSITY_HIGH:
                status.append("高密度 (hdpi)\n");
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                status.append("超高密度 (xhdpi)\n");
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                status.append("超超高密度 (xxhdpi)\n");
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                status.append("超超超高密度 (xxxhdpi)\n");
                break;
        }

        // 5. 语言和区域设置
        status.append("\n=== 语言和区域 ===\n");
        Locale locale = config.locale;
        status.append("语言: ").append(locale.getLanguage()).append("\n");
        status.append("国家: ").append(locale.getCountry()).append("\n");
        status.append("显示语言: ").append(locale.getDisplayName()).append("\n");

        // 6. 键盘类型
        status.append("\n=== 键盘类型 ===\n");
        int keyboard = config.keyboard;
        switch (keyboard) {
            case Configuration.KEYBOARD_NOKEYS:
                status.append("无键盘\n");
                break;
            case Configuration.KEYBOARD_QWERTY:
                status.append("QWERTY键盘\n");
                break;
            case Configuration.KEYBOARD_12KEY:
                status.append("12键键盘\n");
                break;
            case Configuration.KEYBOARD_UNDEFINED:
                status.append("未定义\n");
                break;
        }

        // 7. 触摸屏类型
        status.append("\n=== 触摸屏类型 ===\n");
        int touchscreen = config.touchscreen;
        switch (touchscreen) {
            case Configuration.TOUCHSCREEN_NOTOUCH:
                status.append("无触摸屏\n");
                break;
            case Configuration.TOUCHSCREEN_STYLUS:
                status.append("手写笔触摸屏\n");
                break;
            case Configuration.TOUCHSCREEN_FINGER:
                status.append("手指触摸屏\n");
                break;
            case Configuration.TOUCHSCREEN_UNDEFINED:
                status.append("未定义\n");
                break;
        }

        // 8. 导航方式
        status.append("\n=== 导航方式 ===\n");
        int navigation = config.navigation;
        switch (navigation) {
            case Configuration.NAVIGATION_NONAV:
                status.append("无导航\n");
                break;
            case Configuration.NAVIGATION_DPAD:
                status.append("方向键导航\n");
                break;
            case Configuration.NAVIGATION_TRACKBALL:
                status.append("轨迹球\n");
                break;
            case Configuration.NAVIGATION_WHEEL:
                status.append("滚轮\n");
                break;
            case Configuration.NAVIGATION_UNDEFINED:
                status.append("未定义\n");
                break;
        }

        // 9. 屏幕分辨率
        status.append("\n=== 屏幕分辨率 ===\n");
        status.append("宽度: ").append(metrics.widthPixels).append("px\n");
        status.append("高度: ").append(metrics.heightPixels).append("px\n");
        status.append("DPI: ").append(metrics.densityDpi).append("\n");
        status.append("密度: ").append(metrics.density).append("\n");

        // 10. UI模式
        status.append("\n=== UI模式 ===\n");
        int uiMode = config.uiMode & Configuration.UI_MODE_TYPE_MASK;
        switch (uiMode) {
            case Configuration.UI_MODE_TYPE_NORMAL:
                status.append("正常模式\n");
                break;
            case Configuration.UI_MODE_TYPE_DESK:
                status.append("桌面模式\n");
                break;
            case Configuration.UI_MODE_TYPE_CAR:
                status.append("车载模式\n");
                break;
            case Configuration.UI_MODE_TYPE_TELEVISION:
                status.append("电视模式\n");
                break;
            case Configuration.UI_MODE_TYPE_APPLIANCE:
                status.append("家电模式\n");
                break;
            case Configuration.UI_MODE_TYPE_WATCH:
                status.append("手表模式\n");
                break;
            case Configuration.UI_MODE_TYPE_VR_HEADSET:
                status.append("VR头盔模式\n");
                break;
        }

        // 11. 夜间模式
        status.append("\n=== 夜间模式 ===\n");
        int nightMode = config.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                status.append("日间模式\n");
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                status.append("夜间模式\n");
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                status.append("未定义\n");
                break;
        }

        // 12. 硬键盘状态
        status.append("\n=== 硬键盘状态 ===\n");
        int hardKeyboardHidden = config.hardKeyboardHidden;
        switch (hardKeyboardHidden) {
            case Configuration.HARDKEYBOARDHIDDEN_NO:
                status.append("硬键盘可见\n");
                break;
            case Configuration.HARDKEYBOARDHIDDEN_YES:
                status.append("硬键盘隐藏\n");
                break;
            case Configuration.HARDKEYBOARDHIDDEN_UNDEFINED:
                status.append("未定义\n");
                break;
        }

        statusTextView.setText(status.toString());
    }

    // 监听配置变化
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 当设备配置发生变化时重新获取状态
        getDeviceStatus();
    }
}