package com.metrox.demo70;

import android.content.ComponentName;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.metrox.demo70.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {

    private ActivitySecondBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView tv = binding.tvMsgShow;
        ComponentName cn = getIntent().getComponent();
        tv.setText("组件包名：" + cn.getPackageName() + "\n组件类名:"+cn.getClassName());

    }
}
