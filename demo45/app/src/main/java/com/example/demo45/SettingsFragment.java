package com.example.demo45;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

public class SettingsFragment extends Fragment {

    private static final String PREFS_NAME = "app_settings";
    private static final String KEY_NOTIFICATION = "notification_enabled";
    private static final String KEY_SOUND = "sound_enabled";
    private static final String KEY_VIBRATION = "vibration_enabled";

    private SwitchCompat switchNotification, switchSound, switchVibration;
    private MaterialButton btnSave, btnLogout;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        initViews(view);
        loadSettings();
        setupListeners(view);

        return view;
    }

    private void initViews(View view) {
        switchNotification = view.findViewById(R.id.switch_notification);
        switchSound = view.findViewById(R.id.switch_sound);
        switchVibration = view.findViewById(R.id.switch_vibration);
        btnSave = view.findViewById(R.id.btn_save);
        btnLogout = view.findViewById(R.id.btn_logout);

        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    private void loadSettings() {
        // 加载保存的设置，如果没有则使用默认值
        boolean notificationEnabled = sharedPreferences.getBoolean(KEY_NOTIFICATION, true);
        boolean soundEnabled = sharedPreferences.getBoolean(KEY_SOUND, true);
        boolean vibrationEnabled = sharedPreferences.getBoolean(KEY_VIBRATION, true);

        switchNotification.setChecked(notificationEnabled);
        switchSound.setChecked(soundEnabled);
        switchVibration.setChecked(vibrationEnabled);
    }

    private void setupListeners(View view) {
        // 保存按钮点击
        btnSave.setOnClickListener(v -> {
            saveSettings();
            Toast.makeText(getContext(), "设置已保存", Toast.LENGTH_SHORT).show();
        });

        // 退出登录按钮
        btnLogout.setOnClickListener(v -> showLogoutDialog());

        // 开关状态变化监听
        switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // 如果关闭通知，自动关闭声音和振动
            if (!isChecked) {
                switchSound.setChecked(false);
                switchVibration.setChecked(false);
            }
        });

        // 声音开关监听
        switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // 如果开启声音，确保通知已开启
            if (isChecked) {
                switchNotification.setChecked(true);
            }
        });

        // 振动开关监听
        switchVibration.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // 如果开启振动，确保通知已开启
            if (isChecked) {
                switchNotification.setChecked(true);
            }
        });

        // 设置点击事件
        setupSettingClicks(view);
    }

    private void setupSettingClicks(View view) {
        // 隐私设置
        view.findViewById(R.id.privacy_settings).setOnClickListener(v -> Toast.makeText(getContext(), "隐私设置", Toast.LENGTH_SHORT).show());

        // 账号安全
        view.findViewById(R.id.account_security).setOnClickListener(v -> Toast.makeText(getContext(), "账号安全", Toast.LENGTH_SHORT).show());

        // 位置权限
        view.findViewById(R.id.location_permission).setOnClickListener(v -> Toast.makeText(getContext(), "位置权限", Toast.LENGTH_SHORT).show());
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_NOTIFICATION, switchNotification.isChecked());
        editor.putBoolean(KEY_SOUND, switchSound.isChecked());
        editor.putBoolean(KEY_VIBRATION, switchVibration.isChecked());
        editor.apply();
    }

    private void showLogoutDialog() {
        Snackbar.make(getView(), "确定要退出登录吗？", Snackbar.LENGTH_LONG)
                .setAction("退出", v -> {
                    // 这里执行退出登录的逻辑
                    Toast.makeText(getContext(), "已退出登录", Toast.LENGTH_SHORT).show();
                    // 可以跳转到登录页面
                    // startActivity(new Intent(getContext(), LoginActivity.class));
                    // requireActivity().finish();
                })
                .show();
    }
}
