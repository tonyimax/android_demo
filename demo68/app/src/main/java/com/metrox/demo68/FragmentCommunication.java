package com.metrox.demo68;

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

public class FragmentCommunication extends Fragment {

    public interface OnMessageListener {
        void onMessageSent(String message);
    }

    private OnMessageListener listener;
    private TextView tvReceived;

    public void setOnMessageListener(OnMessageListener listener) {
        this.listener = listener;
    }

    // 接收消息的方法
    public void receiveMessage(String message) {
        if (tvReceived != null) {
            tvReceived.setText("Received: " + message);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_communication, container, false);

        EditText editText = view.findViewById(R.id.editText);
        Button sendButton = view.findViewById(R.id.btnSend);
        tvReceived = view.findViewById(R.id.tvReceived);

        sendButton.setOnClickListener(v -> {
            String message = editText.getText().toString().trim();
            if (!message.isEmpty()) {
                if (listener != null) {
                    listener.onMessageSent(message);
                }
                editText.setText("");
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // 保存状态
        if (tvReceived != null) {
            outState.putString("received_text", tvReceived.getText().toString());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        // 恢复状态
        if (savedInstanceState != null && tvReceived != null) {
            tvReceived.setText(savedInstanceState.getString("received_text", "Received: "));
        }
    }
}