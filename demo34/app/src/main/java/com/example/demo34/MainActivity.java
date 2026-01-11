package com.example.demo34;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ScrollView scrollView;
    private LinearLayout scrollContent;
    private Button btnAddItem, btnScrollToBottom, btnScrollToTop;
    private TextView tvLastItem;
    private int itemCount = 5; // 初始项目数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        initViews();

        // 设置按钮点击监听器
        setupClickListeners();

        // 设置 ScrollView 滚动监听器
        setupScrollListener();
    }

    private void initViews() {
        scrollView = findViewById(R.id.scrollView);
        scrollContent = findViewById(R.id.scrollContent);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnScrollToBottom = findViewById(R.id.btnScrollToBottom);
        btnScrollToTop = findViewById(R.id.btnScrollToTop);
        tvLastItem = findViewById(R.id.tvLastItem);
    }

    private void setupClickListeners() {
        // 添加新项目按钮
        btnAddItem.setOnClickListener(v -> addNewItem());

        // 滚动到底部按钮
        btnScrollToBottom.setOnClickListener(v -> scrollToBottom());

        // 滚动到顶部按钮
        btnScrollToTop.setOnClickListener(v -> scrollToTop());

        // 最后一项点击事件
        tvLastItem.setOnClickListener(v -> Toast.makeText(MainActivity.this, "点击了最后一项", Toast.LENGTH_SHORT).show());
    }

    private void setupScrollListener() {
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            // 检查是否滚动到底部
            if (isScrollViewAtBottom()) {
                // 可以在这里实现自动加载更多功能
                // loadMoreItems();
            }
        });
    }

    // 添加新项目到 ScrollView
    private void addNewItem() {
        itemCount++;

        TextView newItem = new TextView(this);
        newItem.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        newItem.setText("动态添加的第 " + itemCount + " 项");
        newItem.setTextSize(16);
        newItem.setPadding(16, 16, 16, 16);
        newItem.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));

        // 在最后一项之前添加新项目
        int insertPosition = scrollContent.indexOfChild(tvLastItem);
        scrollContent.addView(newItem, insertPosition);

        // 设置新项目的点击事件
        newItem.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this,
                    "点击了动态添加的项目: " + ((TextView)v).getText(),
                    Toast.LENGTH_SHORT).show();
        });

        // 平滑滚动到新添加的项目
        scrollView.post(() -> {
            scrollView.smoothScrollTo(0, newItem.getTop());
        });
    }

    // 滚动到底部
    private void scrollToBottom() {
        scrollView.post(() -> {
            // 使用 smoothScrollTo 实现平滑滚动
            scrollView.smoothScrollTo(0, scrollContent.getHeight());

            // 或者使用 fullScroll 方法
            // scrollView.fullScroll(View.FOCUS_DOWN);
        });
    }

    // 滚动到顶部
    private void scrollToTop() {
        scrollView.post(() -> {
            // 使用 smoothScrollTo 实现平滑滚动
            scrollView.smoothScrollTo(0, 0);

            // 或者使用 fullScroll 方法
            // scrollView.fullScroll(View.FOCUS_UP);
        });
    }

    // 检查 ScrollView 是否滚动到底部
    private boolean isScrollViewAtBottom() {
        View lastChild = scrollView.getChildAt(scrollView.getChildCount() - 1);
        int bottom = lastChild.getBottom() + scrollView.getPaddingBottom();
        int scrollY = scrollView.getScrollY();
        int height = scrollView.getHeight();
        int delta = bottom - (scrollY + height);

        return delta <= 0;
    }

    // 显示 ScrollView 当前滚动位置（可用于调试）
    private void showScrollPosition() {
        int scrollY = scrollView.getScrollY();
        int maxScroll = scrollContent.getHeight() - scrollView.getHeight();
        float percentage = maxScroll > 0 ? (float) scrollY / maxScroll * 100 : 0;

        String message = String.format("滚动位置: %d/%d (%.1f%%)",
                scrollY, maxScroll, percentage);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // 在活动中添加菜单项来显示滚动位置
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == R.id.action_show_position) {
            showScrollPosition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}