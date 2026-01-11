package com.example.demo50;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo50.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'demo50' library on application startup.
    static {
        System.loadLibrary("demo50");
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
    }

    /**
     * A native method that is implemented by the 'demo50' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public void onClickHandle(View v){
        Toast.makeText(MainActivity.this,"通过xml绑定点击事件处理",Toast.LENGTH_LONG).show();
    }
}