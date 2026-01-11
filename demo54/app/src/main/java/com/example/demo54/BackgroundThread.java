package com.example.demo54;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class BackgroundThread extends Thread {
    private static final String TAG = "BackgroundThread";

    private Handler handler;
    private Looper looper;

    @Override
    public void run() {
        Looper.prepare();

        looper = Looper.myLooper();

        handler = new Handler(looper) {
            @Override
            public void handleMessage(Message msg) {
                // 处理消息
                if (msg.obj instanceof Runnable) {
                    Runnable task = (Runnable) msg.obj;
                    task.run();
                }
            }
        };

        Log.d(TAG, "BackgroundThread Looper准备就绪");

        Looper.loop();

        Log.d(TAG, "BackgroundThread Looper退出");
    }

    public void executeTask(Runnable task) {
        if (handler != null) {
            Message msg = Message.obtain();
            msg.obj = task;
            handler.sendMessage(msg);
        }
    }

    public <T> void executeCallable(Callable<T> callable, Callback<T> callback) {
        if (handler != null) {
            handler.post(() -> {
                try {
                    T result = callable.call();
                    callback.onResult(result);
                } catch (Exception e) {
                    callback.onError(e);
                }
            });
        }
    }

    public void quit() {
        if (looper != null) {
            looper.quit();
        }
    }

    public void quitSafely() {
        if (looper != null) {
            looper.quitSafely();
        }
    }

    public interface Callback<T> {
        void onResult(T result);
        void onError(Exception e);
    }
}
