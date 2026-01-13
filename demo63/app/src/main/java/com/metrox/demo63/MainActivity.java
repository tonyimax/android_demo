package com.metrox.demo63;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LifecycleDemo_MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate called - MainActivity");

        // 加载模式测试按钮
        findViewById(R.id.standard_btn).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StandardModeActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.singletop_btn).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SingleTopActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.singletask_btn).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SingleTaskActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.singleinstance_btn).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SingleInstanceActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called - MainActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called - MainActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called - MainActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called - MainActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called - MainActivity");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart called - MainActivity");
    }
}