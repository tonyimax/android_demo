package com.example.demo24;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextSwitcher textSwitcher;
    private Button btnPrevious, btnNext;
    private List<String> textList;
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        initViews();

        // 初始化数据
        initData();

        // 设置监听器
        setupListeners();

        // 显示初始文本
        updateText();
    }

    private void initViews() {
        textSwitcher = findViewById(R.id.textSwitcher);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);

        // 设置 TextSwitcher 的工厂
        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                // 创建 TextView 实例
                TextView textView = new TextView(MainActivity.this);
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(24);
                textView.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                return textView;
            }
        });

        // 或者使用布局文件创建视图
        // textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
        //     @Override
        //     public View makeView() {
        //         return getLayoutInflater().inflate(R.layout.text_view_layout, null);
        //     }
        // });

        // 设置进入动画
        Animation inAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        inAnimation.setDuration(500);
        textSwitcher.setInAnimation(inAnimation);

        // 设置退出动画
        Animation outAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        outAnimation.setDuration(500);
        textSwitcher.setOutAnimation(outAnimation);

        // 也可以使用其他动画效果
        // Animation slideInLeft = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        // Animation slideOutRight = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        // textSwitcher.setInAnimation(slideInLeft);
        // textSwitcher.setOutAnimation(slideOutRight);
    }

    private void initData() {
        textList = new ArrayList<>();
        textList.add("第一句话");
        textList.add("第二句话");
        textList.add("第三句话");
        textList.add("第四句话");
        textList.add("第五句话");
    }

    private void setupListeners() {
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviousText();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextText();
            }
        });

        // 点击 TextSwitcher 本身也可以切换
        textSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextText();
            }
        });
    }

    private void showNextText() {
        currentIndex++;
        if (currentIndex >= textList.size()) {
            currentIndex = 0; // 循环到第一个
        }
        updateText();
    }

    private void showPreviousText() {
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex = textList.size() - 1; // 循环到最后一个
        }
        updateText();
    }

    private void updateText() {
        // 使用不同的动画方向
        if (currentIndex > getPreviousIndex()) {
            // 下一个：从右进入，从左退出
            textSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
            textSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_left));
        } else {
            // 上一个：从左进入，从右退出
            textSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_left));
            textSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_right));
        }

        textSwitcher.setText(textList.get(currentIndex));
    }

    private int getPreviousIndex() {
        int prev = currentIndex - 1;
        if (prev < 0) {
            prev = textList.size() - 1;
        }
        return prev;
    }
}