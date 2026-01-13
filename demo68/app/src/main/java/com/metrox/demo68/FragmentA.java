package com.metrox.demo68;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentA extends Fragment {

    private static final String ARG_MESSAGE = "message";
    private String message;
    private TextView textView;

    // 移除原有的 OnFragmentInteractionListener
    // 使用我们自己的接口
    public interface OnFragmentInteractionListener {
        void onSwitchToFragmentB();
        void onFragmentAction(String action, Bundle data);
    }

    private OnFragmentInteractionListener listener;

    // 工厂方法
    public static FragmentA newInstance(String message) {
        FragmentA fragment = new FragmentA();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // 尝试绑定监听器，但不强制要求
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        }
        // 注意：这里移除了抛异常的逻辑！
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            message = getArguments().getString(ARG_MESSAGE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a, container, false);

        textView = view.findViewById(R.id.textView);
        if (message != null) {
            textView.setText(message);
        } else {
            textView.setText("This is Fragment A");
        }

        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(v -> {
            // 方法1：使用接口回调
            if (listener != null) {
                Bundle data = new Bundle();
                data.putString("action", "switch_to_fragment_b");
                data.putString("fragment", "FragmentA");
                listener.onFragmentAction("navigation", data);
                listener.onSwitchToFragmentB();
            }
            // 方法2：直接调用 Activity 方法（备用）
            else if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).switchToFragmentB();
            }
        });

        return view;
    }

    public void updateMessage(String newMessage) {
        if (textView != null) {
            textView.setText("Updated: " + newMessage);
        }
        message = newMessage;
    }
}