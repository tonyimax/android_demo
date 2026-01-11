package com.example.demo35;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private HorizontalScrollView horizontalScrollView;
    private LinearLayout horizontalContent;
    private HorizontalScrollView imageScrollView;
    private LinearLayout imageContainer;

    private Button btnScrollLeft, btnScrollRight, btnAddItem, btnJumpToEnd;
    private Button btnPrevImage, btnNextImage;
    private TextView tvScrollInfo;

    private int itemCount = 3; // 初始项目计数
    private int imageCount = 0; // 图片计数
    private int scrollAmount = 200; // 每次滚动的像素数

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        initViews();

        // 设置点击监听器
        setupClickListeners();

        // 设置滚动监听器
        setupScrollListeners();

        // 初始化图片浏览器
        initializeImageBrowser();
    }

    private void initViews() {
        horizontalScrollView = findViewById(R.id.horizontalScrollView);
        horizontalContent = findViewById(R.id.horizontalContent);
        imageScrollView = findViewById(R.id.imageScrollView);
        imageContainer = findViewById(R.id.imageContainer);

        btnScrollLeft = findViewById(R.id.btnScrollLeft);
        btnScrollRight = findViewById(R.id.btnScrollRight);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnJumpToEnd = findViewById(R.id.btnJumpToEnd);
        btnPrevImage = findViewById(R.id.btnPrevImage);
        btnNextImage = findViewById(R.id.btnNextImage);

        tvScrollInfo = findViewById(R.id.tvScrollInfo);
    }

    private void setupClickListeners() {
        // 左滚动按钮
        btnScrollLeft.setOnClickListener(v -> scrollHorizontal(-scrollAmount));

        // 右滚动按钮
        btnScrollRight.setOnClickListener(v -> scrollHorizontal(scrollAmount));

        // 添加新项目按钮
        btnAddItem.setOnClickListener(v -> addNewItem());

        // 跳转到末尾按钮
        btnJumpToEnd.setOnClickListener(v -> scrollToEnd());

        // 上一张图片按钮
        btnPrevImage.setOnClickListener(v -> scrollToPreviousImage());

        // 下一张图片按钮
        btnNextImage.setOnClickListener(v -> scrollToNextImage());
    }

    private void setupScrollListeners() {
        // 设置水平滚动监听
        horizontalScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            updateScrollInfo();
        });

        // 设置图片滚动监听
        imageScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            updateImageNavigationButtons();
        });
    }

    private void initializeImageBrowser() {
        // 添加一些示例图片
        int[] imageResources = {
                android.R.drawable.ic_dialog_info,
                android.R.drawable.ic_dialog_alert,
                android.R.drawable.ic_dialog_email,
                android.R.drawable.ic_dialog_map,
                android.R.drawable.ic_dialog_dialer
        };

        for (int i = 0; i < imageResources.length; i++) {
            addImageItem(imageResources[i], "图片 " + (i + 1));
        }

        updateImageNavigationButtons();
    }

    // 水平滚动方法
    private void scrollHorizontal(int amount) {
        int currentScrollX = horizontalScrollView.getScrollX();
        int newScrollX = currentScrollX + amount;

        // 确保滚动范围有效
        int maxScroll = horizontalContent.getWidth() - horizontalScrollView.getWidth();
        newScrollX = Math.max(0, Math.min(newScrollX, maxScroll));

        // 平滑滚动到新位置
        horizontalScrollView.smoothScrollTo(newScrollX, 0);

        // 显示滚动方向提示
        String direction = amount > 0 ? "右" : "左";
        Toast.makeText(this, "向" + direction + "滚动 " + Math.abs(amount) + "像素",
                Toast.LENGTH_SHORT).show();
    }

    // 添加新项目
    private void addNewItem() {
        itemCount++;

        // 创建新的 CardView
        CardView newCard = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                150, // 宽度
                ViewGroup.LayoutParams.MATCH_PARENT // 高度
        );
        params.setMargins(8, 0, 8, 0);
        newCard.setLayoutParams(params);

        // 设置 CardView 属性
        newCard.setRadius(8);
        newCard.setCardElevation(4);

        // 创建内部布局
        LinearLayout innerLayout = new LinearLayout(this);
        innerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        innerLayout.setOrientation(LinearLayout.VERTICAL);
        innerLayout.setGravity(Gravity.CENTER);

        // 设置随机背景色
        int[] colors = {
                0xFFF44336, // 红色
                0xFF2196F3, // 蓝色
                0xFF4CAF50, // 绿色
                0xFFFFC107, // 黄色
                0xFF9C27B0  // 紫色
        };
        innerLayout.setBackgroundColor(colors[itemCount % colors.length]);

        // 添加文本
        TextView textView = new TextView(this);
        textView.setText("项目 " + itemCount);
        textView.setTextSize(18);
        textView.setTypeface(null, Typeface.BOLD);

        // 如果是深色背景，设置白色文字
        if (colors[itemCount % colors.length] == 0xFF9C27B0) {
            textView.setTextColor(0xFFFFFFFF);
        }

        // 添加描述文本
        TextView descView = new TextView(this);
        descView.setText("动态添加");
        descView.setTextSize(12);

        // 将视图添加到布局中
        innerLayout.addView(textView);
        innerLayout.addView(descView);
        newCard.addView(innerLayout);

        // 添加到内容容器中（在最后一个项目之前）
        View lastItem = findViewById(R.id.lastItem);
        int index = horizontalContent.indexOfChild(lastItem);
        horizontalContent.addView(newCard, index);

        // 设置点击事件
        newCard.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this,
                    "点击了: 项目 " + itemCount,
                    Toast.LENGTH_SHORT).show();
        });

        // 滚动到新添加的项目
        horizontalScrollView.postDelayed(() -> {
            horizontalScrollView.smoothScrollTo(newCard.getLeft(), 0);
        }, 100);
    }

    // 滚动到末尾
    private void scrollToEnd() {
        int maxScroll = horizontalContent.getWidth() - horizontalScrollView.getWidth();

        if (maxScroll > 0) {
            // 平滑滚动到末尾
            horizontalScrollView.post(() -> {
                horizontalScrollView.smoothScrollTo(maxScroll, 0);
            });

            // 或者使用 fullScroll
            // horizontalScrollView.post(() -> {
            //     horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
            // });
        } else {
            Toast.makeText(this, "内容不足以滚动", Toast.LENGTH_SHORT).show();
        }
    }

    // 更新滚动信息
    private void updateScrollInfo() {
        int scrollX = horizontalScrollView.getScrollX();
        int maxScroll = Math.max(0, horizontalContent.getWidth() - horizontalScrollView.getWidth());
        float percentage = maxScroll > 0 ? (float) scrollX / maxScroll * 100 : 0;

        String info = String.format("滚动位置: %d/%d (%.1f%%)",
                scrollX, maxScroll, percentage);
        tvScrollInfo.setText("滚动信息: " + info);
    }

    // 添加图片项目
    private void addImageItem(int imageResId, String title) {
        imageCount++;

        // 使用布局文件创建图片项
        View imageItem = LayoutInflater.from(this).inflate(
                R.layout.item_image, imageContainer, false);

        ImageView imageView = imageItem.findViewById(R.id.imageView);
        TextView textView = imageItem.findViewById(R.id.imageTitle);

        imageView.setImageResource(imageResId);
        textView.setText(title);

        // 设置点击事件
        imageItem.setOnClickListener(v -> {
            Toast.makeText(this, "点击了: " + title, Toast.LENGTH_SHORT).show();
        });

        // 添加到容器
        imageContainer.addView(imageItem);
    }

    // 滚动到上一张图片
    private void scrollToPreviousImage() {
        int currentScrollX = imageScrollView.getScrollX();
        int itemWidth = 250; // 每个图片项的宽度（包括边距）

        // 计算前一个位置
        int targetScrollX = currentScrollX - itemWidth;
        if (targetScrollX < 0) targetScrollX = 0;

        // 平滑滚动
        imageScrollView.smoothScrollTo(targetScrollX, 0);
    }

    // 滚动到下一张图片
    private void scrollToNextImage() {
        int currentScrollX = imageScrollView.getScrollX();
        int itemWidth = 250; // 每个图片项的宽度（包括边距）
        int maxScroll = imageContainer.getWidth() - imageScrollView.getWidth();

        // 计算下一个位置
        int targetScrollX = currentScrollX + itemWidth;
        if (targetScrollX > maxScroll) targetScrollX = maxScroll;

        // 平滑滚动
        imageScrollView.smoothScrollTo(targetScrollX, 0);
    }

    // 更新图片导航按钮状态
    private void updateImageNavigationButtons() {
        int currentScrollX = imageScrollView.getScrollX();
        int maxScroll = Math.max(0, imageContainer.getWidth() - imageScrollView.getWidth());

        // 启用/禁用按钮
        btnPrevImage.setEnabled(currentScrollX > 0);
        btnNextImage.setEnabled(currentScrollX < maxScroll);
    }

    // 自动滚动演示
    public void startAutoScroll() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (horizontalScrollView != null) {
                    int currentScrollX = horizontalScrollView.getScrollX();
                    int maxScroll = horizontalContent.getWidth() - horizontalScrollView.getWidth();

                    if (currentScrollX < maxScroll) {
                        // 继续向右滚动
                        horizontalScrollView.smoothScrollTo(currentScrollX + 50, 0);
                        handler.postDelayed(this, 50); // 每50毫秒滚动一次
                    } else {
                        // 滚动到底部后返回顶部
                        horizontalScrollView.postDelayed(() -> {
                            horizontalScrollView.smoothScrollTo(0, 0);
                        }, 1000);
                    }
                }
            }
        }, 50);
    }

    // 停止自动滚动
    public void stopAutoScroll() {
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清理资源
        stopAutoScroll();
        handler = null;
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_auto_scroll) {
            startAutoScroll();
            Toast.makeText(this, "开始自动滚动", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_reset) {
            // 重置滚动位置
            horizontalScrollView.scrollTo(0, 0);
            imageScrollView.scrollTo(0, 0);
            Toast.makeText(this, "已重置滚动位置", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}