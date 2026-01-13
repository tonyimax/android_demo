package com.metrox.demo64.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.metrox.demo64.R;
import com.metrox.demo64.viewmodels.SharedViewModel;

public class HomeFragment extends Fragment {

    private static final String ARG_MESSAGE = "message";
    private TextView tvMessage;
    private SharedViewModel sharedViewModel;

    // 工厂方法创建Fragment实例
    public static HomeFragment newInstance(String message) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvMessage = view.findViewById(R.id.tv_message);

        // 接收参数
        Bundle args = getArguments();
        if (args != null) {
            String message = args.getString(ARG_MESSAGE);
            if (message != null) {
                tvMessage.setText(message);
            }
        }

        // 获取SharedViewModel
        sharedViewModel = new ViewModelProvider(requireActivity())
                .get(SharedViewModel.class);

        // 观察数据变化
        sharedViewModel.getSelectedItem().observe(getViewLifecycleOwner(), item -> {
            tvMessage.setText("Selected: " + item);
        });

        Button btnGoToDetail = view.findViewById(R.id.btn_go_to_detail);
        btnGoToDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToDetail();
            }
        });
    }

    private void navigateToDetail() {
        // 使用新的Fragment Result API进行通信
        DetailFragment detailFragment = DetailFragment.newInstance("Data from Home");

        // 设置Fragment结果监听器
        getParentFragmentManager().setFragmentResultListener(
                "requestKey",
                getViewLifecycleOwner(),
                (requestKey, result) -> {
                    String data = result.getString("data");
                    tvMessage.setText("Received: " + data);
                });

        // 执行Fragment事务
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack("home_to_detail")
                .commit();
    }
}