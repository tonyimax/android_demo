package com.metrox.demo72;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.metrox.demo72.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'demo72' library on application startup.
    static {
        System.loadLibrary("demo72");
    }

    private ActivityMainBinding binding;
    public final static String CUSTOM_ACTIVITY_ACTION = "com.metrox.demo72.CUSTOM_ACTIVITY_ACTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());

        Button btnStart = binding.btnStart;
        btnStart.setOnClickListener(v ->{
            Intent i = new Intent();
            i.setAction(MainActivity.CUSTOM_ACTIVITY_ACTION);//通过action启动新Activity
            startActivity(i);
        });
    }

    /**
     * A native method that is implemented by the 'demo72' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}