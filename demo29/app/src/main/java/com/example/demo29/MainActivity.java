package com.example.demo29;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private NumberPicker numberPicker;
    private TextView tvSelectedNumber;
    private Button btnGetValue, btnSetValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        tvSelectedNumber = findViewById(R.id.tvSelectedNumber);
        numberPicker = findViewById(R.id.numberPicker);
        btnGetValue = findViewById(R.id.btnGetValue);
        btnSetValue = findViewById(R.id.btnSetValue);

        // 配置 NumberPicker
        setupNumberPicker();

        // 设置按钮点击事件
        setupButtonListeners();
    }

    private void setupNumberPicker() {
        // 设置最小值
        numberPicker.setMinValue(0);
        // 设置最大值
        numberPicker.setMaxValue(100);
        // 设置当前值
        numberPicker.setValue(25);
        // 设置是否循环
        numberPicker.setWrapSelectorWheel(true);

        // 设置值改变监听器
        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            // 当值改变时更新TextView
            tvSelectedNumber.setText("Selected: " + newVal);
            // 显示Toast提示
            Toast.makeText(MainActivity.this,
                    "Value changed from " + oldVal + " to " + newVal,
                    Toast.LENGTH_SHORT).show();
        });

        // 初始显示当前值
        tvSelectedNumber.setText("Selected: " + numberPicker.getValue());
    }

    private void setupButtonListeners() {
        btnGetValue.setOnClickListener(v -> {
            int currentValue = numberPicker.getValue();
            Toast.makeText(MainActivity.this,
                    "Current value: " + currentValue,
                    Toast.LENGTH_SHORT).show();
        });

        btnSetValue.setOnClickListener(v -> {
            numberPicker.setValue(50);
            tvSelectedNumber.setText("Selected: " + numberPicker.getValue());
            Toast.makeText(MainActivity.this,
                    "Value set to 50",
                    Toast.LENGTH_SHORT).show();
        });
    }
}