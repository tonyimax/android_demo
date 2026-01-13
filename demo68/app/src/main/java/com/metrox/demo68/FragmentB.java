package com.metrox.demo68;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentB extends Fragment {

    private TextView textViewB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_b, container, false);

        textViewB = view.findViewById(R.id.textViewB);
        textViewB.setText("This is Fragment B");

        Button button = view.findViewById(R.id.buttonB);
        button.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).goBackToFragmentA();
            }
        });

        Button removeButton = view.findViewById(R.id.btnRemove);
        removeButton.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).removeFragmentB();
            }
        });

        return view;
    }

    /**
     * 更新消息（用于通信）
     */
    public void updateMessage(String newMessage) {
        if (textViewB != null) {
            textViewB.setText("Message received: " + newMessage);
        }
    }
}
