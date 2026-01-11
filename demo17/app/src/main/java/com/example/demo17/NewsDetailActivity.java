package com.example.demo17;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NewsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        // 获取传递的数据
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        int imageRes = getIntent().getIntExtra("imageRes", R.drawable.m14);
        String date = getIntent().getStringExtra("date");
        String source = getIntent().getStringExtra("source");

        // 初始化视图
        ImageView imageView = findViewById(R.id.detail_image);
        TextView titleTextView = findViewById(R.id.detail_title);
        TextView descTextView = findViewById(R.id.detail_description);
        TextView dateTextView = findViewById(R.id.detail_date);
        TextView sourceTextView = findViewById(R.id.detail_source);

        // 设置数据
        imageView.setImageResource(imageRes);
        titleTextView.setText(title);
        descTextView.setText(description);
        dateTextView.setText("发布日期: " + date);
        sourceTextView.setText("新闻来源: " + source);

        // 设置返回按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("新闻详情");
        }

        Button ivBack = findViewById(R.id.btn_back);
        ivBack.setOnClickListener(v -> {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}