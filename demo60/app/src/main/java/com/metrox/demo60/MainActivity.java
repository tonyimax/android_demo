package com.metrox.demo60;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REGISTER_REQUEST_CODE = 100;
    private TextView tvUserInfo;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        tvUserInfo = findViewById(R.id.tv_user_info);
        btnRegister = findViewById(R.id.btn_register);

        // 注册按钮点击事件
        btnRegister.setOnClickListener(v -> {
            // 启动注册Activity
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivityForResult(intent, REGISTER_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 检查请求码和结果码
        if (requestCode == REGISTER_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                // 从返回数据中获取用户信息
                String username = data.getStringExtra("username");
                String email = data.getStringExtra("email");
                String phone = data.getStringExtra("phone");

                // 显示注册成功信息
                String userInfo = String.format(
                        "注册成功！\n用户名：%s\n邮箱：%s\n手机号：%s",
                        username, email, phone
                );
                tvUserInfo.setText(userInfo);

                // 显示成功提示
                Toast.makeText(this, "用户注册成功！", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "用户取消了注册", Toast.LENGTH_SHORT).show();
            }
        }
    }
}