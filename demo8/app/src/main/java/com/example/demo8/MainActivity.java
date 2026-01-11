package com.example.demo8;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.example.demo8.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'demo8' library on application startup.
    static {
        System.loadLibrary("demo8");
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

        QuickContactBadge badge = binding.badge;
        badge.assignContactFromPhone("13342864161",true);
    }

    /**
     * A native method that is implemented by the 'demo8' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}