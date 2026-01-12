package com.metrox.demo59;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    private TextView tvReceivedData;
    private EditText etResponse;
    private Button btnReturnData, btnClose;

    private String receivedMessage;
    private int receivedNumber;
    private boolean receivedFlag;
    private String[] receivedArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Toast.makeText(this, "SecondActivity onCreate", Toast.LENGTH_SHORT).show();

        initViews();
        receiveDataFromBundle();
        setupClickListeners();
    }

    private void initViews() {
        tvReceivedData = findViewById(R.id.tvReceivedData);
        etResponse = findViewById(R.id.etResponse);
        btnReturnData = findViewById(R.id.btnReturnData);
        btnClose = findViewById(R.id.btnClose);
    }

    private void receiveDataFromBundle() {
        // 方式1：直接从Intent获取Bundle
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            // 获取数据（提供默认值）
            receivedMessage = bundle.getString("message_key", "默认消息");
            receivedNumber = bundle.getInt("number_key", 0);
            receivedFlag = bundle.getBoolean("flag_key", false);
            receivedArray = bundle.getStringArray("array_key");

            // 显示接收到的数据
            StringBuilder displayText = new StringBuilder();
            displayText.append("接收到的数据：\n");
            displayText.append("消息：").append(receivedMessage).append("\n");
            displayText.append("数字：").append(receivedNumber).append("\n");
            displayText.append("标志：").append(receivedFlag).append("\n");

            if (receivedArray != null) {
                displayText.append("数组：");
                for (String item : receivedArray) {
                    displayText.append(item).append(" ");
                }
            }

            tvReceivedData.setText(displayText.toString());
        } else {
            tvReceivedData.setText("未接收到数据");
        }
    }

    private void setupClickListeners() {
        btnReturnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnDataToMainActivity();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });
    }

    private void returnDataToMainActivity() {
        String response = etResponse.getText().toString().trim();

        if (response.isEmpty()) {
            Toast.makeText(this, "请输入返回数据", Toast.LENGTH_SHORT).show();
            return;
        }

        // 创建Bundle返回数据
        Bundle resultBundle = new Bundle();
        resultBundle.putString("response_key", response);
        resultBundle.putInt("result_number", receivedNumber * 2); // 示例计算

        // 创建Intent并设置结果
        Intent resultIntent = new Intent();
        resultIntent.putExtras(resultBundle);

        // 设置结果码和数据
        setResult(Activity.RESULT_OK, resultIntent);

        Toast.makeText(this, "数据已返回", Toast.LENGTH_SHORT).show();
        finish(); // 关闭当前Activity
    }

    private void closeActivity() {
        // 设置结果为取消
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        // 处理返回键，返回取消状态
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "SecondActivity onStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "SecondActivity onResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "SecondActivity onPause", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "SecondActivity onStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "SecondActivity onDestroy", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "SecondActivity onRestart", Toast.LENGTH_SHORT).show();
    }
}
