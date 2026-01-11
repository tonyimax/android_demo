package com.example.demo26;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

public class ToastUtil {

    private static Toast currentToast;

    private ToastUtil() {
        // 私有构造函数
    }

    /**
     * 显示成功Toast - 绿色图标
     */
    public static void showSuccess(Context context, String message) {
        showCustomToast(context, message,
                R.layout.toast_success,
                R.color.toast_success_bg,
                R.drawable.ic_check_circle,
                R.color.toast_icon_success,  // 成功图标颜色（绿色）
                Toast.LENGTH_SHORT);
    }

    /**
     * 显示错误Toast - 红色图标
     */
    public static void showError(Context context, String message) {
        showCustomToast(context, message,
                R.layout.toast_error,
                R.color.toast_error_bg,
                R.drawable.ic_error,
                R.color.toast_icon_error,    // 错误图标颜色（红色）
                Toast.LENGTH_LONG);
    }

    /**
     * 显示警告Toast - 金色/橙色图标
     */
    public static void showWarning(Context context, String message) {
        showCustomToast(context, message,
                R.layout.toast_warning,
                R.color.toast_warning_bg,
                R.drawable.ic_warning,
                R.color.toast_icon_warning,  // 警告图标颜色（金色/橙色）
                Toast.LENGTH_LONG);
    }

    /**
     * 显示信息Toast - 蓝色图标
     */
    public static void showInfo(Context context, String message) {
        showCustomToast(context, message,
                R.layout.toast_info,
                R.color.toast_info_bg,
                R.drawable.ic_info,
                R.color.toast_icon_info,     // 信息图标颜色（蓝色）
                Toast.LENGTH_SHORT);
    }

    /**
     * 显示短Toast
     */
    public static void showShort(Context context, String message) {
        cancelCurrent();
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示长Toast
     */
    public static void showLong(Context context, String message) {
        cancelCurrent();
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示在顶部的Toast - 使用蓝色图标
     */
    public static void showTop(Context context, String message) {
        showCustomToast(context, message,
                R.layout.toast_info,
                R.color.toast_info_bg,
                R.drawable.ic_info,
                R.color.toast_icon_info,     // 顶部Toast使用蓝色图标
                Toast.LENGTH_SHORT,
                Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 150); // 距离顶部150px
    }

    /**
     * 显示在中间的Toast - 使用蓝色图标
     */
    public static void showCenter(Context context, String message) {
        showCustomToast(context, message,
                R.layout.toast_info,
                R.color.toast_info_bg,
                R.drawable.ic_info,
                R.color.toast_icon_info,     // 中间Toast使用蓝色图标
                Toast.LENGTH_SHORT,
                Gravity.CENTER, 0, 0);
    }

    /**
     * 显示自定义Toast（支持位置参数，可指定图标颜色）
     */
    private static void showCustomToast(Context context, String message,
                                        int layoutResId, @ColorRes int backgroundColorRes,
                                        @DrawableRes int iconResId, @ColorRes int iconColorRes,
                                        int duration, int gravity, int xOffset, int yOffset) {
        cancelCurrent();

        try {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(layoutResId, null);

            // 设置文本
            TextView textView = view.findViewById(R.id.toast_text);
            if (textView != null) {
                textView.setText(message);
            }

            // 设置图标和颜色
            ImageView iconView = view.findViewById(R.id.toast_icon);
            if (iconView != null) {
                iconView.setImageResource(iconResId);
                // 设置图标颜色
                iconView.setColorFilter(ContextCompat.getColor(context, iconColorRes));
            }

            // 设置背景色
            View container = view.findViewById(R.id.custom_toast_container);
            if (container != null) {
                container.setBackgroundColor(ContextCompat.getColor(context, backgroundColorRes));
            }

            // 创建并显示Toast
            currentToast = new Toast(context);
            currentToast.setDuration(duration);
            currentToast.setView(view);
            currentToast.setGravity(gravity, xOffset, yOffset);
            currentToast.show();

        } catch (Exception e) {
            e.printStackTrace();
            // 如果自定义Toast失败，使用系统Toast
            Toast toast = Toast.makeText(context, message, duration);
            toast.setGravity(gravity, xOffset, yOffset);
            toast.show();
        }
    }

    /**
     * 重载方法：显示自定义Toast（底部位置）
     */
    private static void showCustomToast(Context context, String message,
                                        int layoutResId, @ColorRes int backgroundColorRes,
                                        @DrawableRes int iconResId, @ColorRes int iconColorRes,
                                        int duration) {
        showCustomToast(context, message, layoutResId, backgroundColorRes,
                iconResId, iconColorRes, duration,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 100);
    }

    /**
     * 完全自定义Toast（支持自定义图标和颜色）
     */
    public static void showCustom(Context context, String message,
                                  @DrawableRes int iconResId,
                                  @ColorRes int backgroundColorRes,
                                  @ColorRes int iconColorRes,
                                  int duration) {
        cancelCurrent();

        try {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.toast_info, null);

            // 设置文本
            TextView textView = view.findViewById(R.id.toast_text);
            if (textView != null) {
                textView.setText(message);
            }

            // 设置图标和颜色
            ImageView iconView = view.findViewById(R.id.toast_icon);
            if (iconView != null) {
                iconView.setImageResource(iconResId);
                // 设置图标颜色
                iconView.setColorFilter(ContextCompat.getColor(context, iconColorRes));
            }

            // 设置背景色
            View container = view.findViewById(R.id.custom_toast_container);
            if (container != null) {
                container.setBackgroundColor(ContextCompat.getColor(context, backgroundColorRes));
            }

            // 创建并显示Toast
            currentToast = new Toast(context);
            currentToast.setDuration(duration);
            currentToast.setView(view);
            currentToast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 100);
            currentToast.show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, message, duration).show();
        }
    }

    /**
     * 完全自定义Toast（兼容旧版本）
     */
    public static void showCustom(Context context, String message,
                                  @DrawableRes int iconResId,
                                  @ColorRes int backgroundColorRes,
                                  int duration) {
        // 默认使用白色图标，保持向后兼容
        showCustom(context, message, iconResId, backgroundColorRes,
                android.R.color.white, duration);
    }

    /**
     * 取消当前显示的Toast
     */
    public static void cancelCurrent() {
        if (currentToast != null) {
            currentToast.cancel();
            currentToast = null;
        }
    }
}