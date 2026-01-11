package com.example.demo20;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBarBasic, seekBarCustom, seekBarWithLabels, seekBarVolume, seekBarStep;
    private TextView tvProgress, tvMin, tvMax, tvVolumeValue, tvStepValue;
    private Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        initViews();

        // 设置 SeekBar 监听器
        setupSeekBarListeners();

        // 设置按钮点击事件
        setupButtonClick();
    }

    private void initViews() {
        // 查找视图
        tvProgress = findViewById(R.id.tvProgress);

        seekBarBasic = findViewById(R.id.seekBarBasic);
        seekBarCustom = findViewById(R.id.seekBarCustom);
        seekBarWithLabels = findViewById(R.id.seekBarWithLabels);
        seekBarVolume = findViewById(R.id.seekBarVolume);
        seekBarStep = findViewById(R.id.seekBarStep);

        tvMin = findViewById(R.id.tvMin);
        tvMax = findViewById(R.id.tvMax);
        tvVolumeValue = findViewById(R.id.tvVolumeValue);
        tvStepValue = findViewById(R.id.tvStepValue);

        btnReset = findViewById(R.id.btnReset);

        // 设置标签
        tvMin.setText(String.valueOf(seekBarWithLabels.getMin()));
        tvMax.setText(String.valueOf(seekBarWithLabels.getMax()));
        tvVolumeValue.setText("音量: " + seekBarVolume.getProgress());
        tvStepValue.setText("当前值: " + seekBarStep.getProgress());
    }

    private void setupSeekBarListeners() {
        // 基本 SeekBar 监听器
        seekBarBasic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 进度改变时调用
                tvProgress.setText("当前进度: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 开始拖动时调用
                Toast.makeText(MainActivity.this, "开始拖动", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 停止拖动时调用
                int progress = seekBar.getProgress();
                Toast.makeText(MainActivity.this, "停止拖动，最终值: " + progress, Toast.LENGTH_SHORT).show();
            }
        });

        // 自定义 SeekBar 监听器
        seekBarCustom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 这里可以添加自定义逻辑
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 开始拖动
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainActivity.this, "自定义 SeekBar 值: " + seekBar.getProgress(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // 带标签的 SeekBar 监听器
        seekBarWithLabels.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 更新标签或执行其他操作
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 开始拖动
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 停止拖动
            }
        });

        // 音量控制 SeekBar 监听器
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvVolumeValue.setText("音量: " + progress);

                // 根据进度改变图标或执行其他操作
                if (progress == 0) {
                    // 静音状态
                } else if (progress < 5) {
                    // 低音量
                } else {
                    // 正常音量
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 开始调整音量
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 停止调整音量
                Toast.makeText(MainActivity.this, "音量已设置: " + seekBar.getProgress(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // 步进控制的 SeekBar 监听器
        seekBarStep.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 实现步进控制：每10个单位一个步进
                int step = 10;
                int steppedValue = Math.round(progress / step) * step;

                // 如果值改变，则更新 SeekBar
                if (progress != steppedValue) {
                    seekBarStep.setProgress(steppedValue);
                }

                tvStepValue.setText("当前值: " + steppedValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 开始拖动
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 停止拖动
            }
        });
    }

    private void setupButtonClick() {
        btnReset.setOnClickListener(v -> {
            // 重置所有 SeekBar
            seekBarBasic.setProgress(50);
            seekBarCustom.setProgress(100);
            seekBarWithLabels.setProgress(25);
            seekBarVolume.setProgress(5);
            seekBarStep.setProgress(50);

            // 更新显示
            tvProgress.setText("当前进度: 50");
            tvVolumeValue.setText("音量: 5");
            tvStepValue.setText("当前值: 50");

            Toast.makeText(MainActivity.this, "已重置所有 SeekBar", Toast.LENGTH_SHORT).show();
        });
    }

    // 实用方法：以编程方式设置 SeekBar 值
    public void setSeekBarValue(SeekBar seekBar, int value) {
        if (value >= seekBar.getMin() && value <= seekBar.getMax()) {
            seekBar.setProgress(value);
        }
    }

    // 实用方法：获取 SeekBar 百分比
    public int getSeekBarPercentage(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        int max = seekBar.getMax();
        return (int) ((progress * 100.0) / max);
    }
}