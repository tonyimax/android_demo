package com.example.demo10;

import static com.example.demo10.R.*;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.demo10.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'demo10' library on application startup.
    static {
        System.loadLibrary("demo10");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        //TextView tv = binding.sampleText;
        //tv.setText(stringFromJNI());

        ListView lv1 = binding.lv1;
        ListView lv2 = binding.lv2;
        String[] s1 = new String[]{"lv1_item1","lv1_item2","lv1_item3"};
        String[] s2 = new String[]{"lv2_item1","lv2_item2","lv2_item3"};
        ArrayAdapter<String> ad1 = new ArrayAdapter<>(this,R.layout.array_item,s1);
        CheckboxArrayAdapter ad2 = new CheckboxArrayAdapter(this, s2);
        lv1.setAdapter(ad1);
        lv2.setAdapter(ad2);
    }

    /**
     * A native method that is implemented by the 'demo10' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}