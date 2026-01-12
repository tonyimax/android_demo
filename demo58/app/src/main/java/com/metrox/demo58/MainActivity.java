package com.metrox.demo58;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {

    private TextView tvSettingsInfo;
    private TextView tvUsername;
    private TextView tvUpdateInterval;
    private Switch switchNotification;
    private Switch switchAutoSync;
    private Button btnOpenSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        initViews();

        // 设置按钮点击事件
        btnOpenSettings.setOnClickListener(v -> {
            // 打开设置界面
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // 注册设置变化监听器
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(prefChangeListener);
    }

    private void initViews() {
        tvSettingsInfo = findViewById(R.id.tv_settings_info);
        tvUsername = findViewById(R.id.tv_username);
        tvUpdateInterval = findViewById(R.id.tv_update_interval);
        switchNotification = findViewById(R.id.switch_notification);
        switchAutoSync = findViewById(R.id.switch_auto_sync);
        btnOpenSettings = findViewById(R.id.btn_open_settings);
    }

    // 设置变化监听器
    private final SharedPreferences.OnSharedPreferenceChangeListener prefChangeListener =
            (sharedPreferences, key) -> {
                // 当设置变化时，更新UI
                runOnUiThread(this::updateUIFromPreferences);
            };

    @Override
    protected void onResume() {
        super.onResume();
        // 每次返回主界面时更新UI
        updateUIFromPreferences();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销监听器，避免内存泄漏
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.unregisterOnSharedPreferenceChangeListener(prefChangeListener);
    }

    private void updateUIFromPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // 获取所有设置值
        boolean notificationsEnabled = prefs.getBoolean("notifications_enabled", true);
        boolean autoSync = prefs.getBoolean("auto_sync", false);
        String username = prefs.getString("username", "未设置");
        String updateIntervalValue = prefs.getString("update_interval", "3600");
        boolean debugMode = prefs.getBoolean("debug_mode", false);

        // 更新UI
        switchNotification.setChecked(notificationsEnabled);
        switchAutoSync.setChecked(autoSync);
        tvUsername.setText("用户名: " + username);

        // 将秒数转换为可读格式
        String intervalText = formatInterval(updateIntervalValue);
        tvUpdateInterval.setText("更新间隔: " + intervalText);

        // 更新设置信息文本
        updateSettingsInfoText(prefs);

        // 如果有调试模式，可以显示调试信息
        if (debugMode) {
            Toast.makeText(this, "调试模式已启用", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSettingsInfoText(SharedPreferences prefs) {
        StringBuilder info = new StringBuilder();
        info.append("当前设置状态:\n\n");

        // 通知设置
        boolean notificationsEnabled = prefs.getBoolean("notifications_enabled", true);
        info.append("• 通知: ").append(notificationsEnabled ? "✅ 开启" : "❌ 关闭").append("\n");

        // 自动同步
        boolean autoSync = prefs.getBoolean("auto_sync", false);
        info.append("• 自动同步: ").append(autoSync ? "✅ 开启" : "❌ 关闭").append("\n");

        // 用户名
        String username = prefs.getString("username", "未设置");
        info.append("• 用户名: ").append(username).append("\n");

        // 更新间隔
        String interval = prefs.getString("update_interval", "3600");
        info.append("• 更新间隔: ").append(formatInterval(interval)).append("\n");

        // 调试模式
        boolean debugMode = prefs.getBoolean("debug_mode", false);
        info.append("• 调试模式: ").append(debugMode ? "✅ 开启" : "❌ 关闭").append("\n");

        // 服务器地址
        String serverUrl = prefs.getString("server_url", "默认地址");
        info.append("• 服务器: ").append(serverUrl).append("\n");

        // 日志级别
        String logLevel = prefs.getString("log_level", "INFO");
        info.append("• 日志级别: ").append(logLevel);

        tvSettingsInfo.setText(info.toString());
    }

    private String formatInterval(String secondsStr) {
        try {
            int seconds = Integer.parseInt(secondsStr);
            if (seconds < 60) {
                return seconds + "秒";
            } else if (seconds < 3600) {
                return (seconds / 60) + "分钟";
            } else if (seconds < 86400) {
                return (seconds / 3600) + "小时";
            } else {
                return (seconds / 86400) + "天";
            }
        } catch (NumberFormatException e) {
            return secondsStr + "秒";
        }
    }

    // 如果需要，可以在主界面提供快速操作按钮
    private void addQuickActions() {
        // 示例：添加一个快速切换通知的按钮
        findViewById(R.id.switch_notification).setOnClickListener(v -> {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            boolean current = prefs.getBoolean("notifications_enabled", true);
            prefs.edit().putBoolean("notifications_enabled", !current).apply();

            // 提示用户需要打开设置界面进行更详细的配置
            Toast.makeText(this,
                    !current ? "通知已开启" : "通知已关闭",
                    Toast.LENGTH_SHORT).show();
        });
    }
}