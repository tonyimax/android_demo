package com.example.demo21;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private RatingBar ratingBarDefault, ratingBarCustom, ratingBarSmall;
    private TextView tvRating, tvStarsCount;
    private Button btnSubmit, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        initViews();

        // 设置RatingBar监听器
        setupRatingBarListeners();

        // 设置按钮点击监听器
        setupButtonListeners();
    }

    private void initViews() {
        // 获取RatingBar引用
        ratingBarDefault = findViewById(R.id.ratingBarDefault);
        ratingBarCustom = findViewById(R.id.ratingBarCustom);
        ratingBarSmall = findViewById(R.id.ratingBarSmall);

        // 获取TextView引用
        tvRating = findViewById(R.id.tvRating);
        tvStarsCount = findViewById(R.id.tvStarsCount);

        // 获取Button引用
        btnSubmit = findViewById(R.id.btnSubmit);
        btnReset = findViewById(R.id.btnReset);

        // 初始化显示
        updateRatingDisplay();
    }

    private void setupRatingBarListeners() {
        // 为默认RatingBar设置监听
        ratingBarDefault.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    updateRatingDisplay();
                    showToast("默认评分已更改: " + rating);
                }
            }
        });

        // 为自定义RatingBar设置监听
        ratingBarCustom.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    updateRatingDisplay();
                    showToast("自定义评分已更改: " + rating);
                }
            }
        });

        // 为小型RatingBar设置监听
        ratingBarSmall.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    updateRatingDisplay();
                    showToast("小型评分已更改: " + rating);
                }
            }
        });
    }

    private void setupButtonListeners() {
        // 提交按钮点击事件
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float defaultRating = ratingBarDefault.getRating();
                float customRating = ratingBarCustom.getRating();
                float smallRating = ratingBarSmall.getRating();

                // 计算平均分
                float averageRating = (defaultRating + customRating + smallRating) / 3;

                String message = String.format("提交成功！\n默认评分: %.1f\n自定义评分: %.1f\n小型评分: %.1f\n平均分: %.1f",
                        defaultRating, customRating, smallRating, averageRating);

                showLongToast(message);

                // 可以在这里添加提交到服务器的代码
                // submitRatingToServer(averageRating);
            }
        });

        // 重置按钮点击事件
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAllRatings();
                updateRatingDisplay();
                showToast("所有评分已重置");
            }
        });
    }

    /**
     * 更新评分显示
     */
    private void updateRatingDisplay() {
        // 获取默认RatingBar的评分（作为主要评分）
        float currentRating = ratingBarDefault.getRating();
        int starsCount = Math.round(currentRating);

        // 更新显示
        tvRating.setText(String.format("当前评分: %.1f", currentRating));
        tvStarsCount.setText(String.format("选择的星星: %d", starsCount));

        // 根据评分改变文本颜色
        if (currentRating >= 4.0) {
            tvRating.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else if (currentRating >= 3.0) {
            tvRating.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
        } else {
            tvRating.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    /**
     * 重置所有评分
     */
    private void resetAllRatings() {
        ratingBarDefault.setRating(0);
        ratingBarCustom.setRating(0);
        ratingBarSmall.setRating(0);
    }

    /**
     * 显示短Toast消息
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示长Toast消息
     */
    private void showLongToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 获取评分描述
     */
    private String getRatingDescription(float rating) {
        if (rating >= 4.5) {
            return "非常好";
        } else if (rating >= 3.5) {
            return "好";
        } else if (rating >= 2.5) {
            return "一般";
        } else if (rating >= 1.5) {
            return "差";
        } else {
            return "非常差";
        }
    }
}