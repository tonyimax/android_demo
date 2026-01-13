package com.metrox.demo65.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.metrox.demo65.R;
import com.metrox.demo65.contract.OnMessageListener;
import com.metrox.demo65.model.User;

public class CommunicationFragment extends Fragment {

    private OnMessageListener messageListener;
    private EditText editTextMessage;
    private TextView textViewFromActivity;
    private Button btnSendToActivity;
    private Button btnSendData;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // 确保Activity实现了接口
        try {
            messageListener = (OnMessageListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "必须实现OnMessageListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_communication, container, false);

        // 初始化视图
        editTextMessage = view.findViewById(R.id.editTextMessage);
        textViewFromActivity = view.findViewById(R.id.textViewFromActivity);
        btnSendToActivity = view.findViewById(R.id.btnSendToActivity);
        btnSendData = view.findViewById(R.id.btnSendData);

        // 从Activity接收数据（通过Bundle）
        Bundle args = getArguments();
        if (args != null && args.containsKey("initialData")) {
            String initialData = args.getString("initialData");
            textViewFromActivity.setText("来自Activity: " + initialData);
        }

        setupClickListeners();

        return view;
    }

    private void setupClickListeners() {
        // 方式1：通过接口发送消息到Activity
        btnSendToActivity.setOnClickListener(v -> {
            String message = editTextMessage.getText().toString();
            if (!message.isEmpty() && messageListener != null) {
                messageListener.onMessageReceived(message);
            }
        });

        // 方式2：发送复杂对象到Activity
        btnSendData.setOnClickListener(v -> {
            User user = new User("张三", 25);
            if (messageListener != null) {
                messageListener.onDataReceived(user);
            }
        });
    }

    // 方式3：Activity直接调用Fragment的方法
    public void updateFragmentText(String text) {
        if (textViewFromActivity != null) {
            textViewFromActivity.setText("Activity直接调用: " + text);
        }
    }

    // 方式4：通过ViewModel共享数据（需要在Activity中设置）
    public void setSharedData(String data) {
        if (textViewFromActivity != null) {
            textViewFromActivity.setText("共享数据: " + data);
        }
    }
}
