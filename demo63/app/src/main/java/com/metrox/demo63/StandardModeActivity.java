package com.metrox.demo63;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StandardModeActivity extends AppCompatActivity {

    private static final String TAG = "LaunchMode";
    private static int instanceCount = 0;
    private int instanceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_mode);

        instanceCount++;
        instanceId = instanceCount;

        Log.d(TAG, "StandardModeActivity onCreate() - Instance: " + instanceId +
                ", Task ID: " + getTaskId());

        TextView textView = findViewById(R.id.mode_text);
        textView.setText("Standard Mode\nInstance: " + instanceId +
                "\nTask ID: " + getTaskId());

        Button newInstanceBtn = findViewById(R.id.new_instance_btn);
        Button singleTopBtn = findViewById(R.id.singletop_btn);
        Button backBtn = findViewById(R.id.back_btn);

        newInstanceBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, StandardModeActivity.class);
            startActivity(intent);
        });

        singleTopBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, SingleTopActivity.class);
            startActivity(intent);
        });

        backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "StandardModeActivity onNewIntent() - Instance: " + instanceId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "StandardModeActivity onDestroy() - Instance: " + instanceId);
    }
}
