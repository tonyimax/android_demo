package com.metrox.demo63;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SingleTopActivity extends AppCompatActivity {

    private static final String TAG = "LaunchMode";
    private static int instanceCount = 0;
    private int instanceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_mode);

        instanceCount++;
        instanceId = instanceCount;

        Log.d(TAG, "SingleTopActivity onCreate() - Instance: " + instanceId +
                ", Task ID: " + getTaskId());

        TextView textView = findViewById(R.id.mode_text);
        textView.setText("SingleTop Mode\nInstance: " + instanceId +
                "\nTask ID: " + getTaskId());

        Button sameActivityBtn = findViewById(R.id.new_instance_btn);
        Button otherActivityBtn = findViewById(R.id.singletop_btn);
        Button backBtn = findViewById(R.id.back_btn);

        sameActivityBtn.setText("Start Same Activity");
        otherActivityBtn.setText("Start Standard Activity");

        sameActivityBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, SingleTopActivity.class);
            intent.putExtra("timestamp", System.currentTimeMillis());
            startActivity(intent);
        });

        otherActivityBtn.setOnClickListener(v -> {
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
        Log.d(TAG, "SingleTopActivity onNewIntent() - Instance: " + instanceId);

        if (intent.hasExtra("timestamp")) {
            long timestamp = intent.getLongExtra("timestamp", 0);
            TextView textView = findViewById(R.id.mode_text);
            textView.append("\n\nonNewIntent called!\nTimestamp: " + timestamp);
        }
    }
}
