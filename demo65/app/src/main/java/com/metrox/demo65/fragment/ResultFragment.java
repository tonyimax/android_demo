package com.metrox.demo65.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.metrox.demo65.R;

public class ResultFragment extends Fragment {

    private TextView textViewResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        textViewResult = view.findViewById(R.id.textViewResult);
        return view;
    }

    public void displayResult(String result) {
        if (textViewResult != null) {
            textViewResult.setText("结果: " + result);
        }
    }
}
