package com.example.demo33;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    private static final String PREFS_NAME = "AppSettings";

    private Switch switchNotification;
    private Switch switchDarkMode;
    private SeekBar seekBarVolume;
    private SeekBar seekBarBrightness;
    private RadioGroup radioGroupTheme;
    private RadioButton radioLight;
    private RadioButton radioDark;
    private RadioButton radioAuto;
    private TextView tvVolumeValue;
    private TextView tvBrightnessValue;
    private Button btnSave;
    private Button btnReset;

    private SharedPreferences sharedPreferences;

    public SettingsFragment() {
        // 必需的空构造函数
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initViews(view);
        loadSavedSettings();
        setupListeners();

        return view;
    }

    private void initViews(View view) {
        switchNotification = view.findViewById(R.id.switchNotification);
        switchDarkMode = view.findViewById(R.id.switchDarkMode);
        seekBarVolume = view.findViewById(R.id.seekBarVolume);
        seekBarBrightness = view.findViewById(R.id.seekBarBrightness);
        radioGroupTheme = view.findViewById(R.id.radioGroupTheme);
        radioLight = view.findViewById(R.id.radioLight);
        radioDark = view.findViewById(R.id.radioDark);
        radioAuto = view.findViewById(R.id.radioAuto);
        tvVolumeValue = view.findViewById(R.id.tvVolumeValue);
        tvBrightnessValue = view.findViewById(R.id.tvBrightnessValue);
        btnSave = view.findViewById(R.id.btnSave);
        btnReset = view.findViewById(R.id.btnReset);

        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, 0);
    }

    private void loadSavedSettings() {
        // 加载保存的设置
        boolean notificationEnabled = sharedPreferences.getBoolean("notification", true);
        boolean darkModeEnabled = sharedPreferences.getBoolean("darkMode", false);
        int volume = sharedPreferences.getInt("volume", 50);
        int brightness = sharedPreferences.getInt("brightness", 70);
        String theme = sharedPreferences.getString("theme", "light");

        switchNotification.setChecked(notificationEnabled);
        switchDarkMode.setChecked(darkModeEnabled);
        seekBarVolume.setProgress(volume);
        seekBarBrightness.setProgress(brightness);

        switch (theme) {
            case "light":
                radioLight.setChecked(true);
                break;
            case "dark":
                radioDark.setChecked(true);
                break;
            case "auto":
                radioAuto.setChecked(true);
                break;
        }

        updateValueDisplays();
    }

    private void setupListeners() {
        // 音量滑块监听
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvVolumeValue.setText("音量: " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getActivity(),
                        "音量设置为: " + seekBar.getProgress() + "%", Toast.LENGTH_SHORT).show();
            }
        });

        // 亮度滑块监听
        seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvBrightnessValue.setText("亮度: " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getActivity(),
                        "亮度设置为: " + seekBar.getProgress() + "%", Toast.LENGTH_SHORT).show();
            }
        });

        // 通知开关
        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String status = isChecked ? "开启" : "关闭";
                Toast.makeText(getActivity(),
                        "通知已" + status, Toast.LENGTH_SHORT).show();
            }
        });

        // 保存按钮
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
                Toast.makeText(getActivity(), "设置已保存", Toast.LENGTH_SHORT).show();
            }
        });

        // 重置按钮
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetToDefaults();
                Toast.makeText(getActivity(), "已恢复默认设置", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("notification", switchNotification.isChecked());
        editor.putBoolean("darkMode", switchDarkMode.isChecked());
        editor.putInt("volume", seekBarVolume.getProgress());
        editor.putInt("brightness", seekBarBrightness.getProgress());

        // 获取选中的主题
        String theme = "light";
        int checkedId = radioGroupTheme.getCheckedRadioButtonId();
        if (checkedId == R.id.radioDark) {
            theme = "dark";
        } else if (checkedId == R.id.radioAuto) {
            theme = "auto";
        }
        editor.putString("theme", theme);

        editor.apply();
    }

    private void resetToDefaults() {
        switchNotification.setChecked(true);
        switchDarkMode.setChecked(false);
        seekBarVolume.setProgress(50);
        seekBarBrightness.setProgress(70);
        radioLight.setChecked(true);

        updateValueDisplays();
    }

    private void updateValueDisplays() {
        tvVolumeValue.setText("音量: " + seekBarVolume.getProgress() + "%");
        tvBrightnessValue.setText("亮度: " + seekBarBrightness.getProgress() + "%");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 保存当前设置
        saveSettings();

        // 清理资源
        switchNotification = null;
        switchDarkMode = null;
        seekBarVolume = null;
        seekBarBrightness = null;
        radioGroupTheme = null;
    }
}
