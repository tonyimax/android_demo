package com.example.demo46;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
        implements OnCustomEventListener, View.OnClickListener {

    private CustomEventSource eventSource;
    private TextView textView;
    private ProgressBar progressBar;
    private Button btnStart, btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化UI组件
        textView = findViewById(R.id.textView);
        progressBar = findViewById(R.id.progressBar);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);

        // 设置按钮点击监听器
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);

        // 初始化事件源
        eventSource = new CustomEventSource();
        eventSource.setOnCustomEventListener(this);
    }

    // 实现自定义监听器接口的方法
    @Override
    public void onEventSuccess(String message) {
        runOnUiThread(() -> {
            textView.setText("✅ " + message);
            progressBar.setProgress(100);
            btnStart.setEnabled(true);
        });
    }

    @Override
    public void onEventFailed(String error) {
        runOnUiThread(() -> {
            textView.setText("❌ " + error);
            progressBar.setProgress(0);
            btnStart.setEnabled(true);
        });
    }

    @Override
    public void onEventProgress(int progress) {
        runOnUiThread(() -> {
            progressBar.setProgress(progress);
            textView.setText("处理中... " + progress + "%");
        });
    }

    // 实现View.OnClickListener接口
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnStart) {
            startEvent();
        } else if (v.getId() == R.id.btnStop) {
            stopEvent();
        }
    }

    private void startEvent() {
        btnStart.setEnabled(false);
        textView.setText("开始处理...");
        progressBar.setProgress(0);

        // 在新线程中执行耗时操作
        new Thread(() -> {
            eventSource.performAction();
        }).start();
    }

    private void stopEvent() {
        eventSource.removeListener();
        textView.setText("操作已停止");
        progressBar.setProgress(0);
        btnStart.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (eventSource != null) {
            eventSource.removeListener();
        }
    }
}