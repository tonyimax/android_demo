package com.metrox.demo58;

import android.os.Bundle;
import android.widget.Toast;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class AdvancedSettingsFragment extends PreferenceFragmentCompat
        implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_advanced, rootKey);

        // 为所有偏好设置监听器
        findPreference("debug_mode").setOnPreferenceChangeListener(this);
        findPreference("log_level").setOnPreferenceChangeListener(this);
        findPreference("server_url").setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();

        switch (key) {
            case "debug_mode":
                boolean debug = (Boolean) newValue;
                Toast.makeText(getContext(),
                        debug ? "调试模式已启用" : "调试模式已禁用",
                        Toast.LENGTH_SHORT).show();
                return true;

            case "log_level":
                String level = (String) newValue;
                preference.setSummary("当前: " + level);
                return true;

            case "server_url":
                String url = (String) newValue;
                if (url != null && !url.isEmpty()) {
                    preference.setSummary("当前: " + url);
                }
                return true;
        }
        return true;
    }
}