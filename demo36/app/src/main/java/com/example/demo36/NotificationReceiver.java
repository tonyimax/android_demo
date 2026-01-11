package com.example.demo36;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action != null) {
            switch (action) {
                case "ACTION_REPLY":
                    Toast.makeText(context, "回复按钮被点击", Toast.LENGTH_SHORT).show();
                    break;
                case "ACTION_DELETE":
                    Toast.makeText(context, "通知被删除", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
