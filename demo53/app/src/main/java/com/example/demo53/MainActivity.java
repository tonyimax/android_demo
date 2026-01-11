package com.example.demo53;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    // 控件声明
    private ImageView imageView;
    private TextView tvImageInfo;
    private TextView tvStatus;
    private Button btnStart;
    private Button btnStop;

    // 图片资源数组
    private int[] imageResources = {
            R.drawable.m1,
            R.drawable.m2,
            R.drawable.m3,
            R.drawable.m4,
            R.drawable.m5
    };

    // 图片描述数组
    private String[] imageDescriptions = {
            "图片 1",
            "图片 2",
            "图片 3",
            "图片 4",
            "图片 5"
    };

    // 当前显示的图片索引
    private int currentImageIndex = 0;

    // Handler和Timer
    private Handler handler;
    private Timer timer;
    private TimerTask timerTask;

    // 轮播间隔时间（毫秒）
    private static final int INTERVAL = 2000; // 2秒

    // Handler消息类型
    private static final int MSG_UPDATE_IMAGE = 1;
    private static final int MSG_TIMER_STARTED = 2;
    private static final int MSG_TIMER_STOPPED = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化控件
        initViews();

        // 初始化Handler
        initHandler();

        // 设置按钮点击监听器
        setupButtonListeners();

        // 显示第一张图片
        updateImage();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        imageView = findViewById(R.id.imageView);
        tvImageInfo = findViewById(R.id.tvImageInfo);
        tvStatus = findViewById(R.id.tvStatus);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
    }

    /**
     * 初始化Handler
     */
    private void initHandler() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_UPDATE_IMAGE:
                        // 更新图片显示
                        updateImage();
                        return true;

                    case MSG_TIMER_STARTED:
                        // 更新状态显示
                        tvStatus.setText("状态: 正在轮播 (间隔: " + INTERVAL/1000 + "秒)");
                        return true;

                    case MSG_TIMER_STOPPED:
                        // 更新状态显示
                        tvStatus.setText("状态: 已停止");
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * 设置按钮点击监听器
     */
    private void setupButtonListeners() {
        // 开始按钮点击事件
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAutoPlay();
            }
        });

        // 停止按钮点击事件
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAutoPlay();
            }
        });

        // 图片点击事件 - 点击图片可以手动切换到下一张
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextImage();
            }
        });
    }

    /**
     * 开始自动轮播
     */
    private void startAutoPlay() {
        // 如果定时器已经在运行，先停止
        if (timer != null) {
            stopAutoPlay();
        }

        // 创建定时器
        timer = new Timer();

        // 创建定时任务
        timerTask = new TimerTask() {
            @Override
            public void run() {
                // 在子线程中计算下一张图片索引
                currentImageIndex = (currentImageIndex + 1) % imageResources.length;

                // 发送消息给Handler更新UI
                Message msg = handler.obtainMessage(MSG_UPDATE_IMAGE);
                handler.sendMessage(msg);
            }
        };

        // 启动定时器，立即开始，每隔INTERVAL毫秒执行一次
        timer.schedule(timerTask, 0, INTERVAL);

        // 发送定时器已启动的消息
        handler.sendEmptyMessage(MSG_TIMER_STARTED);

        // 更新按钮状态
        updateButtonState(true);
    }

    /**
     * 停止自动轮播
     */
    private void stopAutoPlay() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }

        // 发送定时器已停止的消息
        handler.sendEmptyMessage(MSG_TIMER_STOPPED);

        // 更新按钮状态
        updateButtonState(false);
    }

    /**
     * 更新按钮状态
     * @param isPlaying 是否正在播放
     */
    private void updateButtonState(boolean isPlaying) {
        btnStart.setEnabled(!isPlaying);
        btnStop.setEnabled(isPlaying);
    }

    /**
     * 更新图片显示
     */
    private void updateImage() {
        // 更新图片
        imageView.setImageResource(imageResources[currentImageIndex]);

        // 更新图片信息
        String info = "当前图片: " + (currentImageIndex + 1) + "/" + imageResources.length
                + " - " + imageDescriptions[currentImageIndex];
        tvImageInfo.setText(info);
    }

    /**
     * 手动切换到下一张图片
     */
    private void nextImage() {
        currentImageIndex = (currentImageIndex + 1) % imageResources.length;
        updateImage();
    }

    /**
     * 手动切换到上一张图片
     */
    private void previousImage() {
        currentImageIndex = (currentImageIndex - 1 + imageResources.length) % imageResources.length;
        updateImage();
    }

    /**
     * 生命周期方法 - 暂停时停止轮播
     */
    @Override
    protected void onPause() {
        super.onPause();
        stopAutoPlay();
    }

    /**
     * 生命周期方法 - 销毁时释放资源
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAutoPlay();
    }
}