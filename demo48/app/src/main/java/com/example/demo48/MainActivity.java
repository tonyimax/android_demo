package com.example.demo48;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 100;

    private EditText phoneNumberEditText;
    private EditText messageEditText;
    private Button sendButton;
    private Button sendIntentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化UI组件
        phoneNumberEditText = findViewById(R.id.phone_number_edittext);
        messageEditText = findViewById(R.id.message_edittext);
        sendButton = findViewById(R.id.send_button);
        sendIntentButton = findViewById(R.id.send_intent_button);

        // 检查权限
        checkSmsPermission();

        // 发送按钮点击事件
        sendButton.setOnClickListener(v -> sendSMS());

        sendIntentButton.setOnClickListener(v -> sendSMSViaIntent());
    }

    /**
     * 检查短信发送权限
     */
    private void checkSmsPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            // 如果权限未授予，请求权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    SMS_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * 处理权限请求结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "短信权限已授予", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "需要短信权限才能发送短信", Toast.LENGTH_SHORT).show();
                sendButton.setEnabled(false);
            }
        }
    }

    /**
     * 发送短信的核心方法
     */
    private void sendSMS() {
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String message = messageEditText.getText().toString().trim();

        // 验证输入
        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (message.isEmpty()) {
            Toast.makeText(this, "请输入短信内容", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // 获取SmsManager实例
            SmsManager smsManager = SmsManager.getDefault();

            // 如果消息过长，分割发送
            if (message.length() > 70) {
                // 分割消息
                ArrayList<String> parts = smsManager.divideMessage(message);

                // 发送分割后的各部分
                smsManager.sendMultipartTextMessage(
                        phoneNumber,
                        null,  // 短信中心号码，null表示使用默认
                        parts,
                        null,  // 发送意图
                        null   // 送达意图
                );
            } else {
                // 发送单条短信
                smsManager.sendTextMessage(
                        phoneNumber,
                        null,  // 短信中心号码，null表示使用默认
                        message,
                        null,  // 发送意图
                        null   // 送达意图
                );
            }

            Toast.makeText(this, "短信发送成功", Toast.LENGTH_SHORT).show();
            messageEditText.setText(""); // 清空消息输入框

        } catch (Exception e) {
            Toast.makeText(this, "短信发送失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 使用Intent发送短信（跳转到系统短信应用）
     */
    private void sendSMSViaIntent() {
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String message = messageEditText.getText().toString().trim();

        if (phoneNumber.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, "请填写手机号和短信内容", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // 创建Intent
            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_SENDTO);

            // 设置数据URI
            intent.setData(android.net.Uri.parse("smsto:" + phoneNumber));

            // 添加短信内容
            intent.putExtra("sms_body", message);

            // 启动Activity
            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(this, "无法打开短信应用", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}