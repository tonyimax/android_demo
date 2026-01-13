package com.metrox.demo65;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.metrox.demo65.contract.OnMessageListener;
import com.metrox.demo65.fragment.CommunicationFragment;
import com.metrox.demo65.fragment.ResultFragment;
import com.metrox.demo65.model.User;
import com.metrox.demo65.viewmodel.SharedViewModel;

public class MainActivity extends AppCompatActivity implements OnMessageListener {

    private TextView textViewMessage;
    private EditText editTextToFragment;
    private Button btnSendToFragment;
    private CommunicationFragment communicationFragment;
    private ResultFragment resultFragment;
    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        textViewMessage = findViewById(R.id.textViewMessage);
        editTextToFragment = findViewById(R.id.editTextToFragment);
        btnSendToFragment = findViewById(R.id.btnSendToFragment);

        // 初始化ViewModel（方式5：使用ViewModel共享数据）
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        // 观察ViewModel中的数据变化
        sharedViewModel.getSharedData().observe(this, data -> {
            textViewMessage.setText("ViewModel数据: " + data);
        });

        setupFragments();
        setupClickListeners();
    }

    private void setupFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // 创建CommunicationFragment并传递初始数据
        communicationFragment = new CommunicationFragment();
        Bundle args = new Bundle();
        args.putString("initialData", "这是初始数据");
        communicationFragment.setArguments(args);

        // 添加Fragment
        transaction.add(R.id.fragmentContainer, communicationFragment, "COMM_FRAGMENT");
        transaction.commit();
    }

    private void setupClickListeners() {
        // 方式1：直接调用Fragment的方法
        btnSendToFragment.setOnClickListener(v -> {
            String text = editTextToFragment.getText().toString();
            if (!text.isEmpty() && communicationFragment != null) {
                communicationFragment.updateFragmentText(text);
            }
        });

        // 方式5：通过ViewModel更新数据
        findViewById(R.id.btnUseViewModel).setOnClickListener(v -> {
            sharedViewModel.setSharedData("通过ViewModel更新的数据");
        });

        // 切换Fragment
        findViewById(R.id.btnSwitchFragment).setOnClickListener(v -> {
            switchToResultFragment();
        });
    }

    private void switchToResultFragment() {
        resultFragment = new ResultFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, resultFragment);
        transaction.addToBackStack(null); // 添加到返回栈
        transaction.commit();

        // 延迟一段时间后显示结果
        getWindow().getDecorView().postDelayed(() -> {
            if (resultFragment != null) {
                resultFragment.displayResult("操作成功完成");
            }
        }, 500);
    }

    // 实现接口方法（方式1：Fragment通过接口通知Activity）
    @Override
    public void onMessageReceived(String message) {
        textViewMessage.setText("来自Fragment: " + message);
        Toast.makeText(this, "收到消息: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDataReceived(User user) {
        String userInfo = "姓名: " + user.getName() + ", 年龄: " + user.getAge();
        textViewMessage.setText("用户数据: " + userInfo);
        Toast.makeText(this, "收到用户数据: " + userInfo, Toast.LENGTH_LONG).show();
    }
}