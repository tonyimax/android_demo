package com.example.demo51;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private BallView ballView;
    private TextView infoText;
    private SeekBar radiusSeekBar;
    private SeekBar speedSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 创建主布局
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        // 创建信息文本
        infoText = new TextView(this);
        infoText.setText("触摸屏幕移动小球");
        infoText.setTextSize(18);
        infoText.setPadding(20, 20, 20, 20);

        // 创建小球View
        ballView = new BallView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1.0f
        );
        ballView.setLayoutParams(params);

        // 创建控制面板
        LinearLayout controlLayout = new LinearLayout(this);
        controlLayout.setOrientation(LinearLayout.VERTICAL);
        controlLayout.setPadding(20, 10, 20, 20);

        // 半径控制
        TextView radiusText = new TextView(this);
        radiusText.setText("小球半径:");
        radiusText.setTextSize(16);

        radiusSeekBar = new SeekBar(this);
        radiusSeekBar.setMax(180); // 20-200像素
        radiusSeekBar.setProgress(40); // 默认60像素

        // 速度控制
        TextView speedText = new TextView(this);
        speedText.setText("移动速度:");
        speedText.setTextSize(16);
        speedText.setPadding(0, 20, 0, 0);

        speedSeekBar = new SeekBar(this);
        speedSeekBar.setMax(95); // 0.05-1.0
        speedSeekBar.setProgress(15); // 默认0.2

        // 颜色按钮布局
        LinearLayout colorLayout = new LinearLayout(this);
        colorLayout.setOrientation(LinearLayout.HORIZONTAL);
        colorLayout.setPadding(0, 20, 0, 0);

        // 创建颜色按钮
        String[] colors = {"红色", "蓝色", "绿色", "黄色", "紫色"};
        int[] colorValues = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA};

        for (int i = 0; i < colors.length; i++) {
            Button colorButton = new Button(this);
            colorButton.setText(colors[i]);
            colorButton.setTag(colorValues[i]);

            // 设置按钮颜色
            if (colors[i].equals("黄色")) {
                colorButton.setTextColor(Color.BLACK);
            }

            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            buttonParams.setMargins(2, 0, 2, 0);
            colorButton.setLayoutParams(buttonParams);

            final int color = colorValues[i];
            colorButton.setOnClickListener(v -> ballView.setBallColor(color));

            colorLayout.addView(colorButton);
        }

        // 添加控制项到控制面板
        controlLayout.addView(radiusText);
        controlLayout.addView(radiusSeekBar);
        controlLayout.addView(speedText);
        controlLayout.addView(speedSeekBar);
        controlLayout.addView(colorLayout);

        // 添加所有视图到主布局
        mainLayout.addView(infoText);
        mainLayout.addView(ballView);
        mainLayout.addView(controlLayout);

        // 设置内容视图
        setContentView(mainLayout);

        // 设置SeekBar监听器
        setupSeekBarListeners();

        // 更新信息文本
        updateInfoText();
    }

    private void setupSeekBarListeners() {
        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float radius = 20 + progress; // 20-200像素
                ballView.setBallRadius(radius);
                updateInfoText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float speed = 0.05f + (progress / 100f); // 0.05-1.0
                ballView.setAnimationSpeed(speed);
                updateInfoText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void updateInfoText() {
        // 现在我们可以通过getBallRadius()方法获取小球半径
        float radius = ballView.getBallRadius();
        String info = String.format("触摸屏幕移动小球\n当前半径: %.0f像素\n当前速度: %.2f",
                radius,
                getCurrentSpeed());
        infoText.setText(info);
    }

    // 辅助方法：获取当前速度值（根据SeekBar进度计算）
    private float getCurrentSpeed() {
        int progress = speedSeekBar.getProgress();
        return 0.05f + (progress / 100f);
    }
}