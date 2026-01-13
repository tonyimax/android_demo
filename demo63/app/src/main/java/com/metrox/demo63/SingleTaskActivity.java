package com.metrox.demo63;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SingleTaskActivity extends AppCompatActivity {

    private static final String TAG = "LaunchMode";
    private static int instanceCount = 0;
    private int instanceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_mode);

        instanceCount++;
        instanceId = instanceCount;

        Log.d(TAG, "SingleTaskActivity onCreate() - Instance: " + instanceId +
                ", Task ID: " + getTaskId());

        TextView textView = findViewById(R.id.mode_text);
        textView.setText("SingleTask Mode\nInstance: " + instanceId +
                "\nTask ID: " + getTaskId());

        Button newInstanceBtn = findViewById(R.id.new_instance_btn);
        Button standardBtn = findViewById(R.id.singletop_btn);
        Button backBtn = findViewById(R.id.back_btn);

        newInstanceBtn.setText("Start Same Activity");
        standardBtn.setText("Start Standard Activity");

        newInstanceBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, SingleTaskActivity.class);
            intent.putExtra("timestamp", System.currentTimeMillis());
            startActivity(intent);
        });

        standardBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, StandardModeActivity.class);
            startActivity(intent);
        });

        backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "SingleTaskActivity onNewIntent() - Instance: " + instanceId);

        if (intent.hasExtra("timestamp")) {
            long timestamp = intent.getLongExtra("timestamp", 0);
            TextView textView = findViewById(R.id.mode_text);
            textView.append("\n\nonNewIntent called!\nTimestamp: " + timestamp +
                    "\nClearing activities above this in task!");
        }
    }
}
