package com.example.demo5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;

import com.example.demo5.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'demo5' library on application startup.
    static {
        System.loadLibrary("demo5");
    }

    //private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //主题跟随系统
        int mode = getSharedPreferences("settings", MODE_PRIVATE)
                .getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(mode);
        setContentView(R.layout.activity_main);

    }

    /**
     * A native method that is implemented by the 'demo5' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}