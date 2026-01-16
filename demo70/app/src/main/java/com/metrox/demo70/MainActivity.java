package com.metrox.demo70;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.metrox.demo70.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'demo70' library on application startup.
    static {
        System.loadLibrary("demo70");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());

        Button btnStartIntent = binding.btnStartIntent;
        btnStartIntent.setOnClickListener(v -> {
            ComponentName cn = new ComponentName(MainActivity.this,SecondActivity.class);//创建组件名
            Intent intent = new Intent();//创建Intent
            intent.setComponent(cn);//设置Intent对应组件名
            startActivity(intent);//启动Activity并传入intent
        });
    }

    /**
     * A native method that is implemented by the 'demo70' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}