package com.example.demo54;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ScrollView;
import androidx.appcompat.app.AppCompatActivity;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "LooperDemo";
    private static final int MSG_UPDATE_TEXT = 1;
    private static final int MSG_SHOW_TOAST = 2;
    private static final int MSG_BACKGROUND_TASK = 3;

    private TextView tvStatus;
    private TextView tvLogOutput;
    private Button btnStart;
    private Button btnStop;
    private CustomHandlerThread customThread;
    private BackgroundThread backgroundThread;
    private LooperMonitor looperMonitor;

    // 消息计数器
    private AtomicInteger messageCounter = new AtomicInteger(0);

    // 线程状态
    private String mainThreadStatus = "空闲";
    private String customThreadStatus = "未启动";
    private String backgroundThreadStatus = "未启动";

    // 主线程Handler
    private Handler mainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_TEXT:
                    String text = (String) msg.obj;
                    tvStatus.setText(text);
                    appendLog("主线程更新UI: " + text);
                    break;
                case MSG_SHOW_TOAST:
                    // 可以添加Toast显示
                    break;
            }
        }
    };

    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());

    // 单例模式支持
    private static WeakReference<MainActivity> instanceRef;

    public static MainActivity getInstance() {
        return instanceRef != null ? instanceRef.get() : null;
    }

    private static void setInstance(MainActivity activity) {
        instanceRef = new WeakReference<>(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupClickListeners();

        // 初始化Looper监控
        //looperMonitor = LooperMonitor.getInstance();
        //looperMonitor.monitorMainLooper();

        // 设置静态实例
        CustomHandlerThread.setMainActivity(this);
        setInstance(this);

        appendLog("应用启动完成");
        updateThreadInfo();
    }

    private void initViews() {
        tvStatus = findViewById(R.id.tv_status);
        tvLogOutput = findViewById(R.id.tv_log_output);
        btnStart = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);
    }

    private void setupClickListeners() {
        // 启动自定义HandlerThread
        btnStart.setOnClickListener(v -> {
            appendLog("启动线程...");
            startCustomThread();
            startBackgroundThread();
        });

        // 停止线程
        btnStop.setOnClickListener(v -> {
            appendLog("停止线程...");
            stopThreads();
        });

        // 演示Handler.post()
        findViewById(R.id.btn_post).setOnClickListener(v -> {
            appendLog("开始演示Handler.post()...");
            demoHandlerPost();
        });

        // 演示MessageQueue.IdleHandler
        findViewById(R.id.btn_idle).setOnClickListener(v -> {
            appendLog("开始演示IdleHandler...");
            demoIdleHandler();
        });

        // 发送多个消息
        findViewById(R.id.btn_multi_message).setOnClickListener(v -> {
            appendLog("开始演示多消息处理...");
            demoMultipleMessages();
        });

        // 清空日志
        findViewById(R.id.btn_clear_log).setOnClickListener(v -> {
            onClearLogClick(v);
        });
    }

    // 更新线程信息的方法
    public void updateThreadInfo() {
        runOnUiThread(() -> {
            TextView tvThreadInfo = findViewById(R.id.tv_thread_info);
            String info = String.format(Locale.getDefault(),
                    "主线程: %s\n自定义线程: %s\n后台线程: %s",
                    mainThreadStatus, customThreadStatus, backgroundThreadStatus);
            tvThreadInfo.setText(info);
        });
    }

    // 更新特定线程状态的方法
    public void updateThreadStatus(String threadName, String status) {
        runOnUiThread(() -> {
            switch (threadName) {
                case "MainThread":
                    mainThreadStatus = status;
                    break;
                case "CustomHandlerThread":
                    customThreadStatus = status;
                    break;
                case "BackgroundThread":
                    backgroundThreadStatus = status;
                    break;
            }
            updateThreadInfo();
        });
    }

    // 更新消息计数
    private void updateMessageCount() {
        runOnUiThread(() -> {
            TextView tvCount = findViewById(R.id.tv_message_count);
            tvCount.setText(String.format(Locale.getDefault(),
                    "消息统计: %d", messageCounter.get()));
        });
    }

    private void startCustomThread() {
        customThread = new CustomHandlerThread("CustomHandlerThread");
        customThread.start();
        updateThreadStatus("CustomHandlerThread", "启动中...");

        // 等待Looper初始化完成
        new Thread(() -> {
            customThread.waitForReady();
            runOnUiThread(() -> {
                updateThreadStatus("CustomHandlerThread", "运行中");

                // 向自定义线程发送消息
                Message msg = Message.obtain();
                msg.what = CustomHandlerThread.MSG_DO_TASK;
                msg.obj = "初始任务数据";
                customThread.getHandler().sendMessage(msg);
                messageCounter.incrementAndGet();
                updateMessageCount();
                appendLog("发送MSG_DO_TASK到自定义线程");

                // 延迟发送消息
                customThread.getHandler().sendEmptyMessageDelayed(
                        CustomHandlerThread.MSG_DELAYED_TASK, 3000);
                messageCounter.incrementAndGet();
                updateMessageCount();
                appendLog("发送延迟3秒的MSG_DELAYED_TASK");

                // 发送重复任务
                customThread.getHandler().sendEmptyMessage(
                        CustomHandlerThread.MSG_REPEAT_TASK);
                messageCounter.incrementAndGet();
                updateMessageCount();
                appendLog("发送重复任务MSG_REPEAT_TASK");
            });
        }).start();
    }

    private void startBackgroundThread() {
        backgroundThread = new BackgroundThread();
        backgroundThread.start();
        updateThreadStatus("BackgroundThread", "启动中...");

        // 等待线程启动
        mainHandler.postDelayed(() -> {
            updateThreadStatus("BackgroundThread", "运行中");

            // 向后台线程发送任务
            backgroundThread.executeTask(() -> {
                updateThreadStatus("BackgroundThread", "执行任务中");
                appendLog("后台任务开始执行");
                try {
                    Thread.sleep(2000);
                    // 通过主线程Handler更新UI
                    Message msg = mainHandler.obtainMessage(
                            MSG_UPDATE_TEXT, "后台任务完成");
                    mainHandler.sendMessage(msg);
                    updateThreadStatus("BackgroundThread", "运行中");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            appendLog("已提交后台任务");
        }, 100);
    }

    private void demoHandlerPost() {
        updateThreadStatus("MainThread", "处理post任务");

        // 使用post()方法
        mainHandler.post(() -> {
            tvStatus.setText("通过post()执行的任务");
            appendLog("post()任务执行");
        });
        messageCounter.incrementAndGet();
        updateMessageCount();

        // 延迟执行
        mainHandler.postDelayed(() -> {
            tvStatus.setText("延迟2秒执行的任务");
            appendLog("延迟2秒的postDelayed任务执行");
            updateThreadStatus("MainThread", "空闲");
        }, 2000);
        messageCounter.incrementAndGet();
        updateMessageCount();

        // 在自定义线程中post任务
        if (customThread != null) {
            customThread.getHandler().post(() -> {
                updateThreadStatus("CustomHandlerThread", "处理post任务");
                appendLog("在自定义线程中执行post任务");
                // 这里不能直接更新UI
                runOnUiThread(() -> {
                    tvStatus.setText("从自定义线程更新UI");
                    updateThreadStatus("CustomHandlerThread", "运行中");
                });
            });
            messageCounter.incrementAndGet();
            updateMessageCount();
        }
    }

    private void demoIdleHandler() {
        updateThreadStatus("MainThread", "设置IdleHandler");

        // 添加IdleHandler
        Looper.myQueue().addIdleHandler(() -> {
            appendLog("主线程空闲，执行IdleHandler任务");
            updateThreadStatus("MainThread", "执行空闲任务");
            return false; // 返回false表示只执行一次
        });

        // 执行一个耗时操作，让主线程忙碌
        mainHandler.post(() -> {
            updateThreadStatus("MainThread", "执行耗时操作");
            try {
                appendLog("开始模拟耗时操作...");
                Thread.sleep(3000);
                appendLog("耗时操作完成");
                updateThreadStatus("MainThread", "空闲");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        messageCounter.incrementAndGet();
        updateMessageCount();
    }

    private void demoMultipleMessages() {
        if (customThread == null) {
            appendLog("请先启动线程");
            return;
        }

        // 重置消息计数
        messageCounter.set(0);
        updateMessageCount();
        updateThreadStatus("CustomHandlerThread", "接收多消息");

        Handler handler = customThread.getHandler();

        // 发送不同类型的消息
        sendImmediateMessages(handler);
        sendDelayedMessages(handler);
        sendTimedMessages(handler);
        sendSpecialMessages(handler);

        // 演示消息队列状态
        showQueueStatus();
    }

    private void sendImmediateMessages(Handler handler) {
        // 立即发送的消息
        for (int i = 1; i <= 3; i++) {
            messageCounter.incrementAndGet();
            Message msg = Message.obtain();
            msg.what = CustomHandlerThread.MSG_DO_TASK;
            msg.obj = "立即消息-" + i;
            handler.sendMessage(msg);
            appendLog("发送立即消息-" + i);
        }
        updateMessageCount();
    }

    private void sendDelayedMessages(Handler handler) {
        // 延迟发送的消息
        for (int i = 1; i <= 3; i++) {
            messageCounter.incrementAndGet();
            Message msg = Message.obtain();
            msg.what = CustomHandlerThread.MSG_DO_TASK;
            msg.obj = "延迟消息-" + i;
            long delay = i * 1000L; // 1秒递增
            handler.sendMessageDelayed(msg, delay);
            appendLog("发送延迟消息-" + i + " (" + delay + "ms后)");
        }
        updateMessageCount();
    }

    private void sendTimedMessages(Handler handler) {
        // 在特定时间发送的消息
        long baseTime = SystemClock.uptimeMillis() + 5000; // 5秒后
        for (int i = 1; i <= 2; i++) {
            messageCounter.incrementAndGet();
            Message msg = Message.obtain();
            msg.what = CustomHandlerThread.MSG_DO_TASK;
            msg.obj = "定时消息-" + i;
            long when = baseTime + (i * 1500L);
            handler.sendMessageAtTime(msg, when);
            appendLog("发送定时消息-" + i + " (在" + (when - SystemClock.uptimeMillis()) + "ms后)");
        }
        updateMessageCount();
    }

    private void sendSpecialMessages(Handler handler) {
        // 特殊消息 - 插入队列头部
        messageCounter.incrementAndGet();
        Message frontMsg = Message.obtain();
        frontMsg.what = CustomHandlerThread.MSG_DO_TASK;
        frontMsg.obj = "紧急消息(插队)";
        handler.sendMessageAtFrontOfQueue(frontMsg);
        appendLog("发送紧急消息(插队到队列头部)");

        // 异步消息（需要API 22+）
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            messageCounter.incrementAndGet();
            Message asyncMsg = Message.obtain();
            asyncMsg.what = CustomHandlerThread.MSG_DO_TASK;
            asyncMsg.obj = "异步消息";
            asyncMsg.setAsynchronous(true);
            handler.sendMessage(asyncMsg);
            appendLog("发送异步消息");
        }
        updateMessageCount();
    }

    private void showQueueStatus() {
        // 延迟检查消息队列状态
        mainHandler.postDelayed(() -> {
            if (customThread != null && customThread.getHandler() != null) {
                Looper looper = customThread.getHandler().getLooper();
                //执行多消息时闪退
                //String snapshot = looperMonitor.getMessageQueueSnapshot(looper);
                //appendLog("\n" + snapshot);
            }
        }, 1000);

        // 演示removeMessages
        mainHandler.postDelayed(() -> {
            if (customThread != null && customThread.getHandler() != null) {
                Handler handler = customThread.getHandler();
                handler.removeMessages(CustomHandlerThread.MSG_DO_TASK);
                messageCounter.set(0);
                updateMessageCount();
                updateThreadStatus("CustomHandlerThread", "运行中(消息已移除)");
                appendLog("已移除所有MSG_DO_TASK类型的未处理消息");
            }
        }, 4000);

        // 演示hasMessages
        mainHandler.postDelayed(() -> {
            if (customThread != null && customThread.getHandler() != null) {
                Handler handler = customThread.getHandler();
                boolean hasMsg = handler.hasMessages(CustomHandlerThread.MSG_DELAYED_TASK);
                appendLog("检查是否有MSG_DELAYED_TASK消息: " + hasMsg);
            }
        }, 5000);
    }

    public void appendLog(String message) {
        String time = sdf.format(new Date());
        String log = time + " - " + message + "\n";

        runOnUiThread(() -> {
            String currentText = tvLogOutput.getText().toString();
            tvLogOutput.setText(currentText + log);

            // 查找ScrollView并滚动到底部
            try {
                // 方法1：通过ID直接查找ScrollView
                ScrollView scrollView = findViewById(R.id.scroll_view);
                if (scrollView != null) {
                    scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
                } else {
                    // 方法2：遍历父视图查找ScrollView
                    View view = tvLogOutput;
                    while (view.getParent() != null && view.getParent() instanceof View) {
                        if (view.getParent() instanceof ScrollView) {
                            ((ScrollView) view.getParent()).fullScroll(View.FOCUS_DOWN);
                            break;
                        }
                        view = (View) view.getParent();
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "滚动日志失败: " + e.getMessage());
                // 忽略滚动错误，不影响主要功能
            }
        });

        Log.d(TAG, message);
    }

    // 清空日志按钮点击事件
    public void onClearLogClick(View view) {
        tvLogOutput.setText("");
        messageCounter.set(0);
        updateMessageCount();
        if (looperMonitor != null) {
            looperMonitor.resetStatistics();
        }
        updateThreadStatus("MainThread", "空闲");
        updateThreadStatus("CustomHandlerThread", customThread != null ? "运行中" : "未启动");
        updateThreadStatus("BackgroundThread", backgroundThread != null ? "运行中" : "未启动");
        appendLog("日志已清空");
    }

    private void stopThreads() {
        if (customThread != null) {
            customThread.quitSafely();
            customThread = null;
            updateThreadStatus("CustomHandlerThread", "已停止");
            appendLog("自定义线程已停止");
        }
        if (backgroundThread != null) {
            backgroundThread.quit();
            backgroundThread = null;
            updateThreadStatus("BackgroundThread", "已停止");
            appendLog("后台线程已停止");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopThreads();
        instanceRef = null;
    }
}