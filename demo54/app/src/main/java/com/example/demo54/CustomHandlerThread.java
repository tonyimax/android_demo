package com.example.demo54;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomHandlerThread extends HandlerThread {
    private static final String TAG = "CustomHandlerThread";

    public static final int MSG_DO_TASK = 100;
    public static final int MSG_DELAYED_TASK = 101;
    public static final int MSG_REPEAT_TASK = 102;

    private Handler handler;
    private SimpleDateFormat sdf;

    // MainActivity的弱引用
    private static WeakReference<MainActivity> mainActivityRef;

    public CustomHandlerThread(String name) {
        super(name);
        sdf = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();

        // 创建与当前线程Looper绑定的Handler
        handler = new Handler(getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String time = sdf.format(new Date());
                String threadName = Thread.currentThread().getName();

                switch (msg.what) {
                    case MSG_DO_TASK:
                        String data = (String) msg.obj;
                        String log = time + " - " + threadName +
                                " 处理MSG_DO_TASK: " + data;
                        Log.d(TAG, log);

                        // 更新线程状态
                        updateThreadStatus("处理任务中");

                        // 模拟耗时操作
                        try {
                            Thread.sleep(800); // 800ms模拟处理时间
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // 回调到主线程
                        if (getMainActivity() != null) {
                            getMainActivity().appendLog(log);
                            updateThreadStatus("运行中");
                        }
                        break;

                    case MSG_DELAYED_TASK:
                        log = time + " - " + threadName + " 处理MSG_DELAYED_TASK";
                        Log.d(TAG, log);
                        updateThreadStatus("处理延迟任务");

                        if (getMainActivity() != null) {
                            getMainActivity().appendLog(log);
                            updateThreadStatus("运行中");
                        }
                        break;

                    case MSG_REPEAT_TASK:
                        log = time + " - " + threadName + " 处理MSG_REPEAT_TASK";
                        Log.d(TAG, log);
                        updateThreadStatus("处理重复任务");

                        if (getMainActivity() != null) {
                            getMainActivity().appendLog(log);
                            updateThreadStatus("运行中");
                        }
                        // 每隔1秒重复执行
                        sendEmptyMessageDelayed(MSG_REPEAT_TASK, 1000);
                        break;
                }
            }
        };

        Log.d(TAG, "CustomHandlerThread Looper准备就绪");
    }

    public Handler getHandler() {
        return handler;
    }

    public void waitForReady() {
        // 等待Looper准备就绪
        while (handler == null) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 更新线程状态
    private void updateThreadStatus(String status) {
        MainActivity activity = getMainActivity();
        if (activity != null) {
            activity.updateThreadStatus("CustomHandlerThread", status);
        }
    }

    // 静态方法设置MainActivity
    public static void setMainActivity(MainActivity activity) {
        mainActivityRef = new WeakReference<>(activity);
    }

    // 获取MainActivity实例
    private static MainActivity getMainActivity() {
        return mainActivityRef != null ? mainActivityRef.get() : null;
    }
}