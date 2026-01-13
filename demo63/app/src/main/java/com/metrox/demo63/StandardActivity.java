package com.metrox.demo63;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StandardActivity extends AppCompatActivity {

    private static final String TAG = "LaunchMode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard);

        TextView textView = findViewById(R.id.text_view);
        textView.setText("Standard Activity, Task ID: " + getTaskId());

        Button buttonOpenStandard = findViewById(R.id.button_open_standard);
        buttonOpenStandard.setOnClickListener(v -> {
            Intent intent = new Intent(StandardActivity.this, StandardActivity.class);
            startActivity(intent);
        });

        Button buttonOpenSingleTop = findViewById(R.id.button_open_single_top);
        buttonOpenSingleTop.setOnClickListener(v -> {
            Intent intent = new Intent(StandardActivity.this, SingleTopActivity.class);
            startActivity(intent);
        });

        Button buttonOpenSingleTask = findViewById(R.id.button_open_single_task);
        buttonOpenSingleTask.setOnClickListener(v -> {
            Intent intent = new Intent(StandardActivity.this, SingleTaskActivity.class);
            startActivity(intent);
        });

        Button buttonOpenSingleInstance = findViewById(R.id.button_open_single_instance);
        buttonOpenSingleInstance.setOnClickListener(v -> {
            Intent intent = new Intent(StandardActivity.this, SingleInstanceActivity.class);
            startActivity(intent);
        });

        Log.d(TAG, "StandardActivity onCreate, Task ID: " + getTaskId());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "StandardActivity onNewIntent");
    }
}
