package com.metrox.demo58;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

public class MainSettingsFragment extends PreferenceFragmentCompat
        implements Preference.OnPreferenceChangeListener,
        Preference.OnPreferenceClickListener {

    private SwitchPreferenceCompat notificationsPref;
    private Preference resetPref;
    private Preference privacyPref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // 加载偏好设置XML
        setPreferencesFromResource(R.xml.pref_settings, rootKey);

        // 初始化偏好引用
        notificationsPref = findPreference("notifications_enabled");
        resetPref = findPreference("reset_settings");
        privacyPref = findPreference("privacy_policy");

        // 设置监听器
        if (notificationsPref != null) {
            notificationsPref.setOnPreferenceChangeListener(this);
        }

        if (resetPref != null) {
            resetPref.setOnPreferenceClickListener(this);
        }

        if (privacyPref != null) {
            privacyPref.setOnPreferenceClickListener(this);
        }

        // 设置版本信息
        Preference versionPref = findPreference("app_version");
        if (versionPref != null) {
            try {
                String versionName = requireContext().getPackageManager()
                        .getPackageInfo(requireContext().getPackageName(), 0).versionName;
                versionPref.setSummary("v" + versionName);
            } catch (Exception e) {
                versionPref.setSummary("未知版本");
            }
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();

        switch (key) {
            case "notifications_enabled":
                boolean enabled = (Boolean) newValue;
                // 根据开关状态执行操作
                if (enabled) {
                    Toast.makeText(getContext(), "通知已启用", Toast.LENGTH_SHORT).show();
                    // 这里可以启用通知服务
                } else {
                    Toast.makeText(getContext(), "通知已禁用", Toast.LENGTH_SHORT).show();
                    // 这里可以禁用通知服务
                }
                return true;

            case "update_interval":
                String interval = (String) newValue;
                // 更新间隔改变
                Toast.makeText(getContext(), "更新间隔已设置为: " + interval + "秒",
                        Toast.LENGTH_SHORT).show();
                return true;

            case "username":
                String username = (String) newValue;
                if (username != null && !username.trim().isEmpty()) {
                    preference.setSummary("当前: " + username);
                }
                return true;
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();

        switch (key) {
            case "reset_settings":
                // 重置设置对话框
                showResetDialog();
                return true;

            case "privacy_policy":
                // 打开隐私政策网页
                openPrivacyPolicy();
                return true;

            case "advanced_settings":
                // 这个会自动通过fragment属性打开
                return true;
        }
        return false;
    }

    private void showResetDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("重置设置")
                .setMessage("确定要恢复所有默认设置吗？")
                .setPositiveButton("确定", (dialog, which) -> {
                    // 重置所有设置
                    resetAllPreferences();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void resetAllPreferences() {
        // 获取默认的SharedPreferences
        androidx.preference.PreferenceManager
                .getDefaultSharedPreferences(requireContext())
                .edit()
                .clear()
                .apply();

        // 重新加载偏好设置
        getPreferenceScreen().removeAll();
        onCreatePreferences(null, null);

        Toast.makeText(getContext(), "设置已重置", Toast.LENGTH_SHORT).show();
    }

    private void openPrivacyPolicy() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.example.com/privacy"));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(), "无法打开链接", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // 可以在这里更新UI状态
        updatePreferenceSummaries();
    }

    private void updatePreferenceSummaries() {
        // 更新EditTextPreference的摘要显示当前值
        androidx.preference.EditTextPreference usernamePref =
                findPreference("username");
        if (usernamePref != null && usernamePref.getText() != null) {
            usernamePref.setSummary("当前: " + usernamePref.getText());
        }

        // 更新ListPreference的摘要显示当前选择
        androidx.preference.ListPreference intervalPref =
                findPreference("update_interval");
        if (intervalPref != null) {
            intervalPref.setSummary(intervalPref.getEntry());
        }
    }
}
