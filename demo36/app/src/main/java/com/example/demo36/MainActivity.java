package com.example.demo36;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "my_channel_id";
    private static final int NOTIFICATION_ID = 1;
    private static final int REQUEST_CODE = 100;
    private static final int PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 创建通知渠道
        createNotificationChannel();

        // 发送不同类型的通知
        findViewById(R.id.btn_simple).setOnClickListener(v -> showSimpleNotification());
        findViewById(R.id.btn_expanded).setOnClickListener(v -> showExpandedNotification());
        findViewById(R.id.btn_action).setOnClickListener(v -> showNotificationWithActions());
        findViewById(R.id.btn_progress).setOnClickListener(v -> showProgressNotification());
        findViewById(R.id.btn_cancel).setOnClickListener(v -> cancelNotification());
    }

    private void showGroupedNotifications() {
        String groupKey = "group_key";

        // 创建摘要通知
        Notification summaryNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_group)
                .setContentTitle("2条新消息")
                .setContentText("来自多个聊天")
                .setGroup(groupKey)
                .setGroupSummary(true)
                .build();

        // 创建单个通知
        Notification notification1 = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_person)
                .setContentTitle("用户A")
                .setContentText("你好！")
                .setGroup(groupKey)
                .build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        NotificationManagerCompat.from(this)
                .notify(1, notification1);
        NotificationManagerCompat.from(this)
                .notify(2, summaryNotification);
    }

    // 创建通知渠道（Android 8.0+）
    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            android.app.NotificationChannel channel = new android.app.NotificationChannel(
                    CHANNEL_ID,
                    "My Channel",
                    android.app.NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Channel description");

            android.app.NotificationManager notificationManager =
                    getSystemService(android.app.NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // 1. 简单通知
    private void showSimpleNotification() {
        // 创建点击意图
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setContentTitle("简单通知")
                .setContentText("这是一条简单的通知消息")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)  // 点击事件
                .setAutoCancel(true)              // 点击后自动消失
                .setTicker("通知来了！")           // 状态栏提示文字
                .setWhen(System.currentTimeMillis()) // 设置时间
                .build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification);
    }

    // 2. 展开式通知（BigTextStyle）
    private void showExpandedNotification() {
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                .bigText("这是一条展开的详细通知内容。可以显示更多文字..." +
                        "这是第二行内容，当通知被展开时会显示更多详细信息。")
                .setBigContentTitle("展开式通知标题")
                .setSummaryText("摘要文字");

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setStyle(bigTextStyle)
                .setContentTitle("展开式通知")
                .setContentText("短内容预览")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID + 1, notification);
    }

    // 3. 带操作按钮的通知
    private void showNotificationWithActions() {
        // 点击意图
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // 操作1：回复
        Intent replyIntent = new Intent(this, NotificationReceiver.class);
        replyIntent.setAction("ACTION_REPLY");
        PendingIntent replyPendingIntent = PendingIntent.getBroadcast(
                this, 0, replyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // 操作2：删除
        Intent deleteIntent = new Intent(this, NotificationReceiver.class);
        deleteIntent.setAction("ACTION_DELETE");
        PendingIntent deletePendingIntent = PendingIntent.getBroadcast(
                this, 0, deleteIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("带操作的通知")
                .setContentText("点击按钮执行操作")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .addAction(android.R.drawable.ic_menu_send, "回复", replyPendingIntent)
                .addAction(android.R.drawable.ic_delete, "删除", deletePendingIntent)
                .setDeleteIntent(deletePendingIntent) // 滑动删除时的意图
                .build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID + 2, notification);
    }

    // 4. 进度条通知
    private void showProgressNotification() {
        final int maxProgress = 100;
        final int progress = 0;

        // 初始通知
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("下载中...")
                .setContentText("正在下载文件")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setProgress(maxProgress, progress, false)
                .setOngoing(true); // 设置为进行中，不可滑动删除

        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(NOTIFICATION_ID + 3, builder.build());

        // 模拟进度更新
        new Thread(() -> {
            for (int i = 0; i <= maxProgress; i += 10) {
                builder.setProgress(maxProgress, i, false);
                notificationManager.notify(NOTIFICATION_ID + 3, builder.build());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // 下载完成
            builder.setContentText("下载完成")
                    .setProgress(0, 0, false)
                    .setOngoing(false)
                    .setAutoCancel(true);
            notificationManager.notify(NOTIFICATION_ID + 3, builder.build());
        }).start();
    }

    // 5. 取消通知
    private void cancelNotification() {
        NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);
        NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID + 1);
        NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID + 2);
        NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID + 3);
    }

    private void showCustomNotification() {
        RemoteViews customView = new RemoteViews(getPackageName(), R.layout.custom_notification);
        customView.setTextViewText(R.id.notification_title, "自定义通知");
        customView.setTextViewText(R.id.notification_text, "这是自定义布局的通知");

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setCustomContentView(customView)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID + 4, notification);
    }

    // 检查通知权限
    private boolean checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this,
                    Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    // 请求通知权限
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    PERMISSION_REQUEST_CODE);
        }
    }
}