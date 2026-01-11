package com.example.demo25;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private Button btnPrevious, btnNext, btnAuto, btnStop;
    private TextView tvIndicator;

    // 动画
    private Animation animInFromRight, animOutToLeft;
    private Animation animInFromLeft, animOutToRight;

    // 手势检测
    private GestureDetector gestureDetector;

    // 是否自动播放
    private boolean isAutoPlaying = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initAnimations();
        setupViewFlipper();
        setupButtons();
        setupGestureDetector();
    }

    private void initViews() {
        viewFlipper = findViewById(R.id.viewFlipper);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnAuto = findViewById(R.id.btnAuto);
        btnStop = findViewById(R.id.btnStop);
        tvIndicator = findViewById(R.id.tvIndicator);
    }

    private void initAnimations() {
        // 从右进入动画
        animInFromRight = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        // 向左退出动画
        animOutToLeft = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);

        // 从左进入动画
        animInFromLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        // 向右退出动画
        animOutToRight = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
    }

    private void setupViewFlipper() {
        // 添加视图到 ViewFlipper
        viewFlipper.addView(getLayoutInflater().inflate(R.layout.layout_view1, null));
        viewFlipper.addView(getLayoutInflater().inflate(R.layout.layout_view2, null));
        viewFlipper.addView(getLayoutInflater().inflate(R.layout.layout_view3, null));

        // 设置自动切换间隔（3秒）
        viewFlipper.setFlipInterval(3000);

        // 设置进入和退出动画
        viewFlipper.setInAnimation(animInFromRight);
        viewFlipper.setOutAnimation(animOutToLeft);

        // 开始自动播放
        viewFlipper.startFlipping();

        updateIndicator();

        // 添加切换监听
        viewFlipper.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                updateIndicator();
            }
        });
    }

    private void setupButtons() {
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviousView();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextView();
            }
        });

        btnAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAutoPlay();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAutoPlay();
            }
        });
    }

    private void setupGestureDetector() {
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            private static final int SWIPE_MIN_DISTANCE = 120;
            private static final int SWIPE_THRESHOLD_VELOCITY = 200;

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE &&
                        Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    // 向左滑动 - 显示下一个
                    showNextView();
                    return true;
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE &&
                        Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    // 向右滑动 - 显示上一个
                    showPreviousView();
                    return true;
                }
                return false;
            }
        });

        // 为 ViewFlipper 设置触摸监听
        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    private void showPreviousView() {
        // 停止自动播放
        stopAutoPlay();

        // 设置反向动画
        viewFlipper.setInAnimation(animInFromLeft);
        viewFlipper.setOutAnimation(animOutToRight);

        // 显示上一个视图
        viewFlipper.showPrevious();

        // 重置为正向动画
        viewFlipper.setInAnimation(animInFromRight);
        viewFlipper.setOutAnimation(animOutToLeft);
    }

    private void showNextView() {
        // 停止自动播放
        stopAutoPlay();

        // 设置正向动画
        viewFlipper.setInAnimation(animInFromRight);
        viewFlipper.setOutAnimation(animOutToLeft);

        // 显示下一个视图
        viewFlipper.showNext();
    }

    private void startAutoPlay() {
        if (!isAutoPlaying) {
            viewFlipper.startFlipping();
            isAutoPlaying = true;
            btnAuto.setText("正在自动切换");
        }
    }

    private void stopAutoPlay() {
        if (isAutoPlaying) {
            viewFlipper.stopFlipping();
            isAutoPlaying = false;
            btnAuto.setText("自动切换");
        }
    }

    private void updateIndicator() {
        int currentIndex = viewFlipper.getDisplayedChild() + 1;
        int totalViews = viewFlipper.getChildCount();
        tvIndicator.setText(currentIndex + "/" + totalViews);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 暂停时停止自动播放
        viewFlipper.stopFlipping();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 恢复时重新开始自动播放
        if (isAutoPlaying) {
            viewFlipper.startFlipping();
        }
    }
}