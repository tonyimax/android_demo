package com.example.demo7;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.Toast;

import com.example.demo7.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'demo7' library on application startup.
    static {
        System.loadLibrary("demo7");
    }

    private ActivityMainBinding binding;
    private CountDownTimer countDownTimer;
    private long timeRemaining;
    private boolean isTimerRunning = false;
    private final long totalTime = 10 * 60 * 1000L; // 10分钟

    private TextView tvTimer;
    private Button btnStart;
    private Button btnPause;
    private Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        //TextView tv = binding.sampleText;
        //tv.setText(stringFromJNI());

        tvTimer = findViewById(R.id.tv_timer);
        btnStart = findViewById(R.id.btn_start);
        btnPause = findViewById(R.id.btn_pause);
        btnReset = findViewById(R.id.btn_reset);

        setupTimer();
        setupClickListeners();
    }

    private void setupTimer() {
        countDownTimer = new CountDownTimer(totalTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                updateButtonStates();
                showTimerComplete();
            }
        };

        timeRemaining = totalTime;
        updateTimerText();
    }

    private void updateTimerText() {
        long minutes = (timeRemaining / 1000) / 60;
        long seconds = (timeRemaining / 1000) % 60;
        String timeText = String.format("%02d:%02d", minutes, seconds);
        tvTimer.setText(timeText);
    }

    private void setupClickListeners() {
        btnStart.setOnClickListener(v -> startTimer());
        btnPause.setOnClickListener(v -> pauseTimer());
        btnReset.setOnClickListener(v -> resetTimer());
    }

    private void startTimer() {
        if (!isTimerRunning) {
            countDownTimer.start();
            isTimerRunning = true;
            updateButtonStates();
        }
    }

    private void pauseTimer() {
        if (isTimerRunning) {
            countDownTimer.cancel();
            isTimerRunning = false;
            updateButtonStates();
        }
    }

    private void resetTimer() {
        countDownTimer.cancel();
        timeRemaining = totalTime;
        isTimerRunning = false;
        updateTimerText();
        updateButtonStates();
    }

    private void updateButtonStates() {
        btnStart.setEnabled(!isTimerRunning);
        btnPause.setEnabled(isTimerRunning);
    }

    private void showTimerComplete() {
        Toast.makeText(this, "计时结束！", Toast.LENGTH_SHORT).show();

        // 震动提示
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(500);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isTimerRunning) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    /**
     * A native method that is implemented by the 'demo7' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}