package com.example.demo19;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ProgressBar circularProgressBar;
    private ProgressBar horizontalProgressBar;
    private ProgressBar customProgressBar;
    private TextView progressText;
    private TextView customProgressText;
    private Button btnStart;
    private Button btnReset;
    private Button btnCustomProgress;

    private int progress = 0;
    private Handler handler;
    private Runnable progressRunnable;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupClickListeners();
        handler = new Handler();
    }

    private void initViews() {
        circularProgressBar = findViewById(R.id.circularProgressBar);
        horizontalProgressBar = findViewById(R.id.horizontalProgressBar);
        customProgressBar = findViewById(R.id.customProgressBar);
        progressText = findViewById(R.id.progressText);
        customProgressText = findViewById(R.id.customProgressText);
        btnStart = findViewById(R.id.btnStart);
        btnReset = findViewById(R.id.btnReset);
        btnCustomProgress = findViewById(R.id.btnCustomProgress);
    }

    private void setupClickListeners() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    startProgress();
                } else {
                    stopProgress();
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetProgress();
            }
        });

        btnCustomProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCustomProgress();
            }
        });
    }

    private void startProgress() {
        isRunning = true;
        btnStart.setText("暂停");
        circularProgressBar.setVisibility(View.VISIBLE);

        progressRunnable = new Runnable() {
            @Override
            public void run() {
                if (progress < 100) {
                    progress += 2;
                    horizontalProgressBar.setProgress(progress);
                    progressText.setText(progress + "%");

                    if (isRunning) {
                        handler.postDelayed(this, 50);
                    }
                } else {
                    isRunning = false;
                    btnStart.setText("开始");
                    circularProgressBar.setVisibility(View.GONE);
                }
            }
        };

        handler.post(progressRunnable);
    }

    private void stopProgress() {
        isRunning = false;
        btnStart.setText("继续");
        handler.removeCallbacks(progressRunnable);
    }

    private void resetProgress() {
        isRunning = false;
        btnStart.setText("开始");
        progress = 0;
        horizontalProgressBar.setProgress(0);
        progressText.setText("0%");
        circularProgressBar.setVisibility(View.GONE);
        handler.removeCallbacks(progressRunnable);
    }

    private void animateCustomProgress() {
        ObjectAnimator animator = ObjectAnimator.ofInt(
                customProgressBar, "progress", 0, 100
        );
        animator.setDuration(3000);
        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            customProgressText.setText(value + "%");
        });
        animator.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && progressRunnable != null) {
            handler.removeCallbacks(progressRunnable);
        }
    }
}