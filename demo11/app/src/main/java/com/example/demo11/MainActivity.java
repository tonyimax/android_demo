package com.example.demo11;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.demo11.databinding.ActivityMainBinding;

public class MainActivity extends ListActivity {

    // Used to load the 'demo11' library on application startup.
    static {
        System.loadLibrary("demo11");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //给ListView填充的Item数据
        String[] strs = new String[] {"one","tow","three"};
        //ListView的集合数据适配器
        ArrayAdapter<String> ad = new ArrayAdapter<>(
                this,//当前上下文
                R.layout.custom_single_choice_item,
                R.id.textView,
                strs);//填充的集合数据
        setListAdapter(ad);//指定ListView的数据适配器
    }
}