package com.metrox.demo64.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.metrox.demo64.R;

public class DetailFragmentWithCallback extends Fragment {

    // 定义回调接口
    public interface OnDataPassListener {
        void onDataPass(String data);
    }

    private OnDataPassListener dataPassListener;

    public void setOnDataPassListener(OnDataPassListener listener) {
        this.dataPassListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        Button btnSendData = view.findViewById(R.id.btn_send_data);
        btnSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataPassListener != null) {
                    dataPassListener.onDataPass("Data via callback");
                }
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dataPassListener = null; // 避免内存泄漏
    }
}
