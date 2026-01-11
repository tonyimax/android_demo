package com.example.demo49;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo49.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static {
        System.loadLibrary("demo49");
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

        Button btn = binding.btnTest;
        btn.setOnClickListener(this);

        Button btn1 = binding.button;
        btn1.setOnClickListener(this);
    }

    /**
     * A native method that is implemented by the 'demo49' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id==R.id.btnTest){
            Toast.makeText(MainActivity.this,"TEST: Activity实现OnClickListener接口",Toast.LENGTH_LONG).show();
        }else if(id==R.id.button){
            Toast.makeText(MainActivity.this,"BUTTON: Activity实现OnClickListener接口",Toast.LENGTH_LONG).show();
        }

    }

}