package com.metrox.demo62;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    // 定义回调接口
    public interface OnDataCallback {
        void onDataSent(String data);
    }

    private OnDataCallback dataCallback;

    private EditText etDataInput;
    private RadioGroup rgOptions;
    private Button btnSubmit;
    private Button btnCancel;
    private Button btnCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        initViews();
        setupListeners();

        // 获取传递过来的数据
        String requestType = getIntent().getStringExtra("REQUEST_TYPE");
        if (requestType != null) {
            Toast.makeText(this, "请求类型: " + requestType, Toast.LENGTH_SHORT).show();
        }

        // 设置回调接口（在实际应用中，可能通过其他方式设置）
        dataCallback = new OnDataCallback() {
            @Override
            public void onDataSent(String data) {
                // 这里通常需要将数据传回MainActivity
                // 由于Activity之间不能直接持有引用，这里只是演示接口定义
                // 实际应用中需要通过其他方式实现
            }
        };

        Toast.makeText(this, "SecondActivity onCreate()", Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        etDataInput = findViewById(R.id.et_data_input);
        rgOptions = findViewById(R.id.rg_options);
        btnSubmit = findViewById(R.id.btn_submit);
        btnCancel = findViewById(R.id.btn_cancel);
        btnCallback = findViewById(R.id.btn_callback);
    }

    private void setupListeners() {
        // 1. 提交数据并返回（使用Intent返回数据）
        btnSubmit.setOnClickListener(v -> {
            String inputData = etDataInput.getText().toString().trim();

            if (inputData.isEmpty()) {
                Toast.makeText(SecondActivity.this, "请输入数据", Toast.LENGTH_SHORT).show();
                return;
            }

            // 获取选中的单选按钮
            int selectedId = rgOptions.getCheckedRadioButtonId();
            String option = "无选择";

            if (selectedId == R.id.rb_option1) {
                option = "选项1";
            } else if (selectedId == R.id.rb_option2) {
                option = "选项2";
            } else if (selectedId == R.id.rb_option3) {
                option = "选项3";
            }

            // 创建返回的Intent
            Intent resultIntent = new Intent();
            resultIntent.putExtra("RESULT_DATA", inputData);
            resultIntent.putExtra("SELECTED_OPTION", option);

            // 设置结果并关闭Activity
            setResult(RESULT_OK, resultIntent);
            finish();

            Toast.makeText(SecondActivity.this, "数据已返回: " + inputData, Toast.LENGTH_SHORT).show();
        });

        // 2. 取消操作
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 设置取消结果
                setResult(RESULT_CANCELED);
                finish();

                Toast.makeText(SecondActivity.this, "操作已取消", Toast.LENGTH_SHORT).show();
            }
        });

        // 3. 模拟通过回调接口返回数据
        btnCallback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputData = etDataInput.getText().toString().trim();

                if (inputData.isEmpty()) {
                    Toast.makeText(SecondActivity.this, "请输入数据", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 在实际应用中，这里会通过接口回调将数据传回
                // 但由于Activity之间不能直接持有引用，这里用Intent模拟
                Intent callbackIntent = new Intent();
                callbackIntent.putExtra("SETTINGS_DATA", "通过回调接口: " + inputData);
                setResult(RESULT_OK, callbackIntent);
                finish();

                Toast.makeText(SecondActivity.this, "通过回调接口返回数据", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "SecondActivity onStart()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "SecondActivity onResume()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "SecondActivity onPause()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "SecondActivity onStop()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "SecondActivity onDestroy()", Toast.LENGTH_SHORT).show();
    }

    // 处理返回键
    @Override
    public void onBackPressed() {
        // 用户按返回键时，设置结果为取消
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}
