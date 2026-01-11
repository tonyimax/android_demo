package com.example.demo54;

import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.os.SystemClock;
import android.util.Log;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LooperMonitor {
    private static final String TAG = "LooperMonitor";
    private static LooperMonitor instance;
    private SimpleDateFormat sdf;
    private long lastMessageTime = 0;
    private int processedMessageCount = 0;

    private LooperMonitor() {
        sdf = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());
    }

    public static LooperMonitor getInstance() {
        if (instance == null) {
            synchronized (LooperMonitor.class) {
                if (instance == null) {
                    instance = new LooperMonitor();
                }
            }
        }
        return instance;
    }

    /**
     * 监控主线程Looper
     */
    public void monitorMainLooper() {
        monitorLooper(Looper.getMainLooper(), "MainLooper");
    }

    /**
     * 监控指定线程的Looper
     */
    public void monitorLooper(Looper looper, final String looperName) {
        if (looper == null) {
            Log.e(TAG, "Looper不能为null");
            return;
        }

        looper.setMessageLogging(new android.util.Printer() {
            private static final String START = ">>>>> Dispatching";
            private static final String END = "<<<<< Finished";
            private long startTime;
            private long startUptime;

            @Override
            public void println(String log) {
                if (log.startsWith(START)) {
                    startTime = System.currentTimeMillis();
                    startUptime = SystemClock.uptimeMillis();
                    lastMessageTime = startTime;

                    // 提取消息信息
                    String messageInfo = extractMessageInfo(looper, looperName);
                    String logMsg = String.format(Locale.getDefault(),
                            "[%s] 开始处理消息%s 时间: %s",
                            looperName, messageInfo, sdf.format(new Date(startTime)));

                    Log.d(TAG, logMsg);

                    if (MainActivity.getInstance() != null) {
                        MainActivity.getInstance().appendLog("监控: " + logMsg);
                        MainActivity.getInstance().updateThreadStatus(looperName, "处理消息中");
                    }

                } else if (log.startsWith(END)) {
                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;
                    long totalDuration = endTime - lastMessageTime;
                    processedMessageCount++;

                    String logMsg = String.format(Locale.getDefault(),
                            "[%s] 消息处理完成 耗时: %dms, 总消息数: %d, 距上个消息间隔: %dms",
                            looperName, duration, processedMessageCount, totalDuration);

                    Log.d(TAG, logMsg);

                    if (duration > 16) { // 超过16ms可能掉帧
                        Log.w(TAG, "警告: " + looperName + " 消息处理耗时过长: " + duration + "ms");
                        if (MainActivity.getInstance() != null) {
                            MainActivity.getInstance().appendLog("警告: " + looperName + " 处理超时(" + duration + "ms)");
                        }
                    }

                    if (MainActivity.getInstance() != null) {
                        MainActivity.getInstance().appendLog("监控: " + logMsg);
                        MainActivity.getInstance().updateThreadStatus(looperName, "空闲");
                    }
                }
            }
        });

        Log.d(TAG, "已开始监控Looper: " + looperName);
    }

    /**
     * 提取消息队列信息
     */
    private String extractMessageInfo(Looper looper, String looperName) {
        try {
            MessageQueue queue = looper.getQueue();
            if (queue == null) {
                return " (队列为空)";
            }

            // 使用反射获取消息队列内容
            Field messagesField = MessageQueue.class.getDeclaredField("mMessages");
            messagesField.setAccessible(true);
            Message msg = (Message) messagesField.get(queue);

            int pendingCount = 0;
            while (msg != null) {
                pendingCount++;
                // 获取下一个消息（使用反射访问Message的next字段）
                Field nextField = Message.class.getDeclaredField("next");
                nextField.setAccessible(true);
                msg = (Message) nextField.get(msg);
            }

            return String.format(Locale.getDefault(),
                    " (待处理消息: %d)", pendingCount);

        } catch (Exception e) {
            Log.e(TAG, "获取消息队列信息失败: " + e.getMessage());
            return "";
        }
    }

    /**
     * 获取消息队列快照
     */
    public String getMessageQueueSnapshot(Looper looper) {
        StringBuilder snapshot = new StringBuilder();
        snapshot.append("消息队列快照:\n");

        try {
            MessageQueue queue = looper.getQueue();
            if (queue == null) {
                return "队列为空";
            }

            Field messagesField = MessageQueue.class.getDeclaredField("mMessages");
            messagesField.setAccessible(true);
            Message msg = (Message) messagesField.get(queue);

            int count = 0;
            long currentTime = SystemClock.uptimeMillis();

            while (msg != null) {
                count++;
                String messageInfo = formatMessageInfo(msg, currentTime);
                snapshot.append(String.format(Locale.getDefault(),
                        "  %d. %s\n", count, messageInfo));
                // 获取下一个消息（使用反射访问Message的next字段）
                Field nextField = Message.class.getDeclaredField("next");
                nextField.setAccessible(true);
                msg = (Message) nextField.get(msg);
            }

            if (count == 0) {
                snapshot.append("  无待处理消息\n");
            }

            snapshot.append(String.format(Locale.getDefault(),
                    "总计: %d 个消息\n", count));

        } catch (Exception e) {
            snapshot.append("获取队列信息失败: ").append(e.getMessage()).append("\n");
        }

        return snapshot.toString();
    }

    private String formatMessageInfo(Message msg, long currentTime) {
        StringBuilder info = new StringBuilder();
        info.append("what=").append(msg.what);

        if (msg.obj != null) {
            String objStr = msg.obj.toString();
            if (objStr.length() > 20) {
                objStr = objStr.substring(0, 17) + "...";
            }
            info.append(", obj=").append(objStr);
        }
        long delay = msg.getWhen() - currentTime;
        if (delay > 0) {
            info.append(String.format(Locale.getDefault(),
                    " (延迟: %dms)", delay));
        } else if (delay < 0) {
            info.append(String.format(Locale.getDefault(),
                    " (超时: %dms)", -delay));
        } else {
            info.append(" (立即执行)");
        }

        if (msg.getTarget() != null) {
            String handlerInfo = msg.getTarget().toString();
            // 提取简单的Handler信息
            int atIndex = handlerInfo.indexOf('@');
            if (atIndex != -1) {
                handlerInfo = handlerInfo.substring(atIndex + 1);
                if (handlerInfo.length() > 8) {
                    handlerInfo = handlerInfo.substring(0, 8);
                }
            }
            info.append(", handler=").append(handlerInfo);
        }

        return info.toString();
    }

    /**
     * 重置统计
     */
    public void resetStatistics() {
        processedMessageCount = 0;
        lastMessageTime = 0;
    }

    /**
     * 获取统计信息
     */
    public String getStatistics() {
        return String.format(Locale.getDefault(),
                "已处理消息: %d\n最后消息时间: %s",
                processedMessageCount,
                lastMessageTime > 0 ? sdf.format(new Date(lastMessageTime)) : "无");
    }
}
