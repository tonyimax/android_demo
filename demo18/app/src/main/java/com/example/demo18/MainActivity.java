package com.example.demo18;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.StackView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private StackView stackView;
    private Button btnPrevious, btnNext;
    private StackAdapter adapter;
    private List<StackItem> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initData();
        setupAdapter();
        setupListeners();
    }

    private void initViews() {
        stackView = findViewById(R.id.stackView);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
    }

    private void initData() {
        itemList = new ArrayList<>();

        // 添加示例数据
        itemList.add(new StackItem("自然风景", "美丽的山脉和湖泊", R.drawable.m1)); // 需要准备图片资源
        itemList.add(new StackItem("城市夜景", "璀璨的城市灯光", R.drawable.m2));
        itemList.add(new StackItem("海滩日落", "金色的沙滩和夕阳", R.drawable.m3));
        itemList.add(new StackItem("森林小路", "宁静的森林小径", R.drawable.m4));
        itemList.add(new StackItem("星空摄影", "浩瀚的星空", R.drawable.m5));

        // 如果没有图片资源，可以使用默认颜色
        // 在 res/values/colors.xml 中添加颜色定义
    }

    private void setupAdapter() {
        adapter = new StackAdapter(this, itemList);
        stackView.setAdapter(adapter);

        // 设置点击监听
        stackView.setOnItemClickListener((parent, view, position, id) -> {
            StackItem selectedItem = itemList.get(position);
            Toast.makeText(MainActivity.this,
                    "点击了: " + selectedItem.getTitle(),
                    Toast.LENGTH_SHORT).show();
        });
    }

    private void setupListeners() {
        btnPrevious.setOnClickListener(v -> {
            // 显示前一个视图
            stackView.showPrevious();

            // 或者使用 setDisplayedChild 方法
            // int current = stackView.getDisplayedChild();
            // if (current > 0) {
            //     stackView.setDisplayedChild(current - 1);
            // } else {
            //     stackView.setDisplayedChild(itemList.size() - 1);
            // }
        });

        btnNext.setOnClickListener(v -> {
            // 显示下一个视图
            stackView.showNext();

            // 或者使用 setDisplayedChild 方法
            // int current = stackView.getDisplayedChild();
            // if (current < itemList.size() - 1) {
            //     stackView.setDisplayedChild(current + 1);
            // } else {
            //     stackView.setDisplayedChild(0);
            // }
        });
    }

    // 可选：动态添加和移除项目
    public void addItem(View view) {
        StackItem newItem = new StackItem("新项目", "动态添加的项目", R.drawable.m6);
        itemList.add(newItem);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "添加了新项目", Toast.LENGTH_SHORT).show();
    }

    public void removeItem(View view) {
        if (!itemList.isEmpty()) {
            int currentPosition = stackView.getDisplayedChild();
            if (currentPosition < itemList.size()) {
                itemList.remove(currentPosition);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "移除了当前项目", Toast.LENGTH_SHORT).show();
            }
        }
    }
}