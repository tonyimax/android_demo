package com.metrox.demo62;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SECOND_ACTIVITY = 1001;
    private static final int REQUEST_CODE_SETTINGS = 1002;

    private TextView tvResult;
    private TextView tvSettingsResult;
    private Button btnOpenSecondActivity;
    private Button btnOpenSettings;
    private Button btnOpenForResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupListeners();

        Toast.makeText(this, "MainActivity onCreate()", Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        tvResult = findViewById(R.id.tv_result);
        tvSettingsResult = findViewById(R.id.tv_settings_result);
        btnOpenSecondActivity = findViewById(R.id.btn_open_second);
        btnOpenSettings = findViewById(R.id.btn_open_settings);
        btnOpenForResult = findViewById(R.id.btn_open_for_result);
    }

    private void setupListeners() {
        // 1. 普通方式启动Activity（无回调）
        btnOpenSecondActivity.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        });

        // 2. 使用startActivityForResult启动（传统回调方式）
        btnOpenForResult.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra("REQUEST_TYPE", "FOR_RESULT");
            startActivityForResult(intent, REQUEST_CODE_SECOND_ACTIVITY);
        });

        // 3. 使用Activity Result API（新式回调方式）
        btnOpenSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra("REQUEST_TYPE", "SETTINGS");
            startActivityForResult(intent, REQUEST_CODE_SETTINGS);
        });
    }

    // 传统方式：处理Activity回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SECOND_ACTIVITY) {
                if (data != null && data.hasExtra("RESULT_DATA")) {
                    String result = data.getStringExtra("RESULT_DATA");
                    tvResult.setText("回调结果: " + result);
                    Toast.makeText(this, "收到SecondActivity返回的数据: " + result, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_CODE_SETTINGS) {
                if (data != null && data.hasExtra("SETTINGS_DATA")) {
                    String settings = data.getStringExtra("SETTINGS_DATA");
                    tvSettingsResult.setText("设置结果: " + settings);
                    Toast.makeText(this, "收到设置数据: " + settings, Toast.LENGTH_SHORT).show();
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "用户取消了操作", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "MainActivity onStart()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "MainActivity onResume()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "MainActivity onPause()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "MainActivity onStop()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "MainActivity onDestroy()", Toast.LENGTH_SHORT).show();
    }

    // 处理从SecondActivity通过接口回调传回的数据
    public void onDataReceivedFromSecondActivity(String data) {
        Toast.makeText(this, "通过接口回调接收的数据: " + data, Toast.LENGTH_LONG).show();
    }
}