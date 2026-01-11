package com.example.demo46;

public interface OnCustomEventListener {
    void onEventSuccess(String message);
    void onEventFailed(String error);
    void onEventProgress(int progress);
}
