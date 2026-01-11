package com.example.demo16;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ImageDetailActivity extends AppCompatActivity {

    private ImageView detailImageView;
    private Button backButton;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        // 初始化视图
        detailImageView = findViewById(R.id.detailImageView);
        backButton = findViewById(R.id.backButton);

        // 获取传递的图片 URI
        String imageUriString = getIntent().getStringExtra("image_uri");
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            // 使用 Glide 加载图片
            Glide.with(this)
                    .load(imageUri)
                    .into(detailImageView);
        }

        // 返回按钮点击事件
        backButton.setOnClickListener(v -> finish());
    }
}