package com.example.demo13;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView;

    // 数据源
    private String[] countries = {
            "中国", "美国", "英国", "法国", "德国", "日本", "韩国",
            "俄罗斯", "加拿大", "澳大利亚", "巴西", "印度"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        // 创建适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                countries
        );

        // 设置适配器
        autoCompleteTextView.setAdapter(adapter);

        // 设置点击监听器
        autoCompleteTextView.setOnItemClickListener(
                (parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);
            Toast.makeText(MainActivity.this,
                    "选择了: " + selectedItem, Toast.LENGTH_SHORT).show();
        });
    }
}