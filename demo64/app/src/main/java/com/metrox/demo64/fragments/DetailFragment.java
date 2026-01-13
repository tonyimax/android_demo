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

import com.metrox.demo64.R;

public class DetailFragment extends Fragment {

    private static final String ARG_DATA = "data";

    public static DetailFragment newInstance(String data) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATA, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvDetailContent = view.findViewById(R.id.tv_detail_content);
        Bundle args = getArguments();
        if (args != null) {
            String data = args.getString(ARG_DATA);
            tvDetailContent.setText(data);
        }

        Button btnSendData = view.findViewById(R.id.btn_send_data);
        btnSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataBack();
            }
        });
    }

    private void sendDataBack() {
        // 方法1：使用Fragment Result API
        Bundle result = new Bundle();
        result.putString("data", "Result from Detail Fragment");
        getParentFragmentManager().setFragmentResult("requestKey", result);

        // 方法2：通过ViewModel通信
        // SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity())
        //         .get(SharedViewModel.class);
        // sharedViewModel.selectItem("Item from Detail");

        // 返回上一个Fragment
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
