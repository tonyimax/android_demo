package com.metrox.demo65.contract;

import com.metrox.demo65.model.User;

public interface OnMessageListener {
    void onMessageReceived(String message);
    void onDataReceived(User user);
}
