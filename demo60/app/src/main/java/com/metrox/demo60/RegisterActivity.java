package com.metrox.demo60;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etEmail, etPhone;
    private Button btnSubmit, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 初始化视图
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        btnSubmit = findViewById(R.id.btn_submit);
        btnCancel = findViewById(R.id.btn_cancel);

        // 提交按钮点击事件
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    // 获取用户输入
                    String username = etUsername.getText().toString().trim();
                    String email = etEmail.getText().toString().trim();
                    String phone = etPhone.getText().toString().trim();

                    // 创建返回的Intent
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("username", username);
                    resultIntent.putExtra("email", email);
                    resultIntent.putExtra("phone", phone);

                    // 设置结果码并返回数据
                    setResult(RESULT_OK, resultIntent);

                    // 显示成功消息
                    Toast.makeText(RegisterActivity.this,
                            "注册成功！", Toast.LENGTH_SHORT).show();

                    // 关闭当前Activity
                    finish();
                }
            }
        });

        // 取消按钮点击事件
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 设置取消结果码
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    /**
     * 验证用户输入
     */
    private boolean validateInput() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        // 检查用户名
        if (username.isEmpty()) {
            etUsername.setError("用户名不能为空");
            return false;
        }

        // 检查密码
        if (password.isEmpty()) {
            etPassword.setError("密码不能为空");
            return false;
        }

        if (password.length() < 6) {
            etPassword.setError("密码长度至少6位");
            return false;
        }

        // 检查邮箱格式
        if (email.isEmpty()) {
            etEmail.setError("邮箱不能为空");
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("邮箱格式不正确");
            return false;
        }

        // 检查手机号格式
        if (phone.isEmpty()) {
            etPhone.setError("手机号不能为空");
            return false;
        }

        if (phone.length() != 11) {
            etPhone.setError("手机号格式不正确");
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        // 返回键处理
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}