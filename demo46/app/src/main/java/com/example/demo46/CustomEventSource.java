package com.example.demo46;


public class CustomEventSource {
    private OnCustomEventListener listener;

    // 注册监听器
    public void setOnCustomEventListener(OnCustomEventListener listener) {
        this.listener = listener;
    }

    // 触发事件的方法
    public void performAction() {
        // 模拟一些操作
        for (int i = 1; i <= 10; i++) {
            if (listener != null) {
                listener.onEventProgress(i * 10);
            }

            try {
                Thread.sleep(200); // 模拟耗时操作
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 假设操作成功
        if (listener != null) {
            if (Math.random() > 0.3) {
                listener.onEventSuccess("操作成功完成！");
            } else {
                listener.onEventFailed("操作失败：随机错误");
            }
        }
    }

    // 移除监听器
    public void removeListener() {
        this.listener = null;
    }
}