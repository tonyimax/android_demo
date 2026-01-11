package com.example.demo33;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private EditText editText;
    private Button btnSubmit;
    private Button btnTime;
    private TextView tvWelcome;
    private TextView tvHistory;
    private StringBuilder historyLog;

    public HomeFragment() {
        // 必需的空构造函数
        historyLog = new StringBuilder();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        setupListeners();

        // 显示欢迎信息
        tvWelcome.setText("欢迎使用应用！\n当前时间：" + getCurrentTime());

        return view;
    }

    private void initViews(View view) {
        editText = view.findViewById(R.id.editText);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnTime = view.findViewById(R.id.btnTime);
        tvWelcome = view.findViewById(R.id.tvWelcome);
        tvHistory = view.findViewById(R.id.tvHistory);
    }

    private void setupListeners() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString().trim();
                if (!input.isEmpty()) {
                    String logEntry = getCurrentTime() + ": 用户输入 - " + input + "\n";
                    historyLog.append(logEntry);
                    tvHistory.setText("操作历史：\n" + historyLog.toString());
                    Toast.makeText(getActivity(), "已保存：" + input, Toast.LENGTH_SHORT).show();
                    editText.setText(""); // 清空输入框
                } else {
                    Toast.makeText(getActivity(), "请输入内容", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeLog = getCurrentTime() + ": 用户查看时间\n";
                historyLog.append(timeLog);
                tvHistory.setText("操作历史：\n" + historyLog.toString());
                Toast.makeText(getActivity(), "当前时间：" + getCurrentTime(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // 长按清除历史记录
        tvHistory.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                historyLog = new StringBuilder();
                tvHistory.setText("操作历史：\n");
                Toast.makeText(getActivity(), "历史记录已清除", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 清理资源
        editText = null;
        btnSubmit = null;
        tvWelcome = null;
        tvHistory = null;
    }
}
