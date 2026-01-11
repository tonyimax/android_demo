package com.example.demo23;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class MainActivity extends AppCompatActivity {

    private ImageSwitcher imageSwitcher;
    private Button btnPrevious, btnNext;
    private TextView tvImageIndex;

    private int[] imageResources = {
            R.drawable.m1,
            R.drawable.m2,
            R.drawable.m3,
            R.drawable.m4,
            R.drawable.m5
    };

    private int currentIndex = 0;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupImageSwitcher();
        setButtonListeners();
        setupGestureDetector();
    }

    private void initViews() {
        imageSwitcher = findViewById(R.id.imageSwitcher);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        tvImageIndex = findViewById(R.id.tvImageIndex);
    }

    private void setupImageSwitcher() {
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
                        ImageSwitcher.LayoutParams.MATCH_PARENT,
                        ImageSwitcher.LayoutParams.MATCH_PARENT));
                return imageView;
            }
        });

        // 设置自定义动画
        Animation slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        Animation slideOutRight = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
        Animation slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        Animation slideOutLeft = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);

        imageSwitcher.setInAnimation(slideInLeft);
        imageSwitcher.setOutAnimation(slideOutRight);

        imageSwitcher.setImageResource(imageResources[currentIndex]);
        updateImageIndex();
    }

    private void setupGestureDetector() {
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                try {
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > 100) {
                        if (diffX > 0) {
                            // 向右滑动 - 上一张
                            showPreviousImage();
                        } else {
                            // 向左滑动 - 下一张
                            showNextImage();
                        }
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        imageSwitcher.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    private void setButtonListeners() {
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviousImage();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextImage();
            }
        });
    }

    private void showPreviousImage() {
        if (currentIndex > 0) {
            currentIndex--;

            // 设置动画方向
            Animation slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
            Animation slideOutLeft = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
            imageSwitcher.setInAnimation(slideInRight);
            imageSwitcher.setOutAnimation(slideOutLeft);

            imageSwitcher.setImageResource(imageResources[currentIndex]);
            updateImageIndex();

            // 重置为默认动画方向
            Animation slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
            Animation slideOutRight = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
            imageSwitcher.setInAnimation(slideInLeft);
            imageSwitcher.setOutAnimation(slideOutRight);
        }
    }

    private void showNextImage() {
        if (currentIndex < imageResources.length - 1) {
            currentIndex++;
            imageSwitcher.setImageResource(imageResources[currentIndex]);
            updateImageIndex();
        }
    }

    private void updateImageIndex() {
        tvImageIndex.setText("图片 " + (currentIndex + 1) + "/" + imageResources.length);
        btnPrevious.setEnabled(currentIndex > 0);
        btnNext.setEnabled(currentIndex < imageResources.length - 1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
}

/*import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class MainActivity extends AppCompatActivity {

    private ImageSwitcher imageSwitcher;
    private Button btnPrevious, btnNext;
    private TextView tvImageIndex;

    // 图片资源数组
    private int[] imageResources = {
            R.drawable.m1,
            R.drawable.m2,
            R.drawable.m3,
            R.drawable.m4,
            R.drawable.m5
    };

    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupImageSwitcher();
        setButtonListeners();
    }

    private void initViews() {
        imageSwitcher = findViewById(R.id.imageSwitcher);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        tvImageIndex = findViewById(R.id.tvImageIndex);
    }

    private void setupImageSwitcher() {
        // 设置 ViewFactory
        imageSwitcher.setFactory(() -> {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
                    ImageSwitcher.LayoutParams.MATCH_PARENT,
                    ImageSwitcher.LayoutParams.MATCH_PARENT));
            return imageView;
        });

        // 设置动画效果
        Animation inAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation outAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        imageSwitcher.setInAnimation(inAnimation);
        imageSwitcher.setOutAnimation(outAnimation);

        // 设置初始图片
        imageSwitcher.setImageResource(imageResources[currentIndex]);
        updateImageIndex();
    }

    private void setButtonListeners() {
        btnPrevious.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                imageSwitcher.setImageResource(imageResources[currentIndex]);
                updateImageIndex();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentIndex < imageResources.length - 1) {
                currentIndex++;
                imageSwitcher.setImageResource(imageResources[currentIndex]);
                updateImageIndex();
            }
        });
    }

    private void updateImageIndex() {
        tvImageIndex.setText("图片 " + (currentIndex + 1) + "/" + imageResources.length);

        // 根据位置启用/禁用按钮
        btnPrevious.setEnabled(currentIndex > 0);
        btnNext.setEnabled(currentIndex < imageResources.length - 1);
    }
}*/