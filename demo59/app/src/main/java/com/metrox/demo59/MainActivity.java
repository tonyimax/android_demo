package com.metrox.demo59;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SECOND_ACTIVITY = 1001;
    private EditText etMessage, etNumber;
    private CheckBox cbFlag;
    private TextView tvResult;
    private Button btnStartActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "MainActivity onCreate", Toast.LENGTH_SHORT).show();

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etMessage = findViewById(R.id.etMessage);
        etNumber = findViewById(R.id.etNumber);
        cbFlag = findViewById(R.id.cbFlag);
        tvResult = findViewById(R.id.tvResult);
        btnStartActivity = findViewById(R.id.btnStartActivity);
    }

    private void setupClickListeners() {
        btnStartActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSecondActivity();
            }
        });
    }

    private void startSecondActivity() {
        // 获取用户输入的数据
        String message = etMessage.getText().toString().trim();
        String numberStr = etNumber.getText().toString().trim();
        boolean flag = cbFlag.isChecked();

        // 创建Bundle对象
        Bundle bundle = new Bundle();

        // 添加不同类型的数据到Bundle
        bundle.putString("message_key", message);

        if (!numberStr.isEmpty()) {
            bundle.putInt("number_key", Integer.parseInt(numberStr));
        }

        bundle.putBoolean("flag_key", flag);
        bundle.putStringArray("array_key", new String[]{"Java", "Kotlin", "Android"});

        // 创建Intent并附加Bundle
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtras(bundle); // 或者直接使用 intent.putExtra("bundle_key", bundle);

        // 启动Activity并期待返回结果
        startActivityForResult(intent, REQUEST_CODE_SECOND_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SECOND_ACTIVITY) {
            if (resultCode == RESULT_OK && data != null) {
                // 从返回的Intent中获取数据
                Bundle resultBundle = data.getExtras();
                if (resultBundle != null) {
                    String response = resultBundle.getString("response_key", "无数据");
                    int resultNumber = resultBundle.getInt("result_number", 0);

                    tvResult.setText(String.format("返回结果：%s\n数字结果：%d",
                            response, resultNumber));
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "用户取消了操作", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "MainActivity onStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "MainActivity onResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "MainActivity onPause", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "MainActivity onStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "MainActivity onDestroy", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "MainActivity onRestart", Toast.LENGTH_SHORT).show();
    }
}