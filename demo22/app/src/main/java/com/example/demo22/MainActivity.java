package com.example.demo22;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ViewSwitcher viewSwitcher;
    private GridView gridViewPage1, gridViewPage2;
    private LinearLayout dockLayout;
    private TextView pageIndicator;
    private int currentPage = 0;
    private static final int PAGE_COUNT = 2;

    // 手势检测器
    private GestureDetector gestureDetector;

    // 应用图标数据
    private String[] appNamesPage1 = {"电话", "信息", "相机", "相册",
            "浏览器", "音乐", "设置", "计算器"};
    private String[] appNamesPage2 = {"地图", "日历", "邮箱", "文件",
            "时钟", "录音", "天气", "笔记"};
    private String[] dockAppNames = {"电话", "浏览器", "相机", "设置"};

    // 第一页图标 - 使用系统图标
    private int[] appIconsPage1 = {
            android.R.drawable.ic_menu_call,          // 电话
            android.R.drawable.ic_dialog_email,       // 信息
            android.R.drawable.ic_menu_camera,        // 相机
            android.R.drawable.ic_menu_gallery,       // 相册
            android.R.drawable.ic_menu_search,        // 浏览器
            android.R.drawable.ic_media_play,         // 音乐
            android.R.drawable.ic_menu_preferences,   // 设置
            android.R.drawable.ic_menu_agenda         // 计算器
    };

    // 第二页图标 - 使用系统图标
    private int[] appIconsPage2 = {
            android.R.drawable.ic_menu_mapmode,       // 地图
            android.R.drawable.ic_menu_today,         // 日历
            android.R.drawable.ic_dialog_email,       // 邮箱
            android.R.drawable.ic_menu_save,          // 文件
            android.R.drawable.ic_lock_idle_alarm,    // 时钟
            android.R.drawable.ic_btn_speak_now,      // 录音
            android.R.drawable.ic_menu_day,           // 天气
            android.R.drawable.ic_menu_edit           // 笔记
    };

    // Dock图标
    private int[] dockIcons = {
            android.R.drawable.ic_menu_call,
            android.R.drawable.ic_menu_search,
            android.R.drawable.ic_menu_camera,
            android.R.drawable.ic_menu_preferences
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化手势检测器
        gestureDetector = new GestureDetector(this, new MyGestureListener());

        initViews();
        setupViewSwitcher();
        setupPage1();
        setupPage2();
        setupDock();
        setupPageIndicator();

        // 为ViewSwitcher设置触摸监听
        //viewSwitcher.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
    }

    private void initViews() {
        viewSwitcher = findViewById(R.id.viewSwitcher);
        dockLayout = findViewById(R.id.dockLayout);
        pageIndicator = findViewById(R.id.pageIndicator);
    }

    private void setupViewSwitcher() {
        // 设置默认动画
        Animation inAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        Animation outAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        viewSwitcher.setInAnimation(inAnimation);
        viewSwitcher.setOutAnimation(outAnimation);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void attachGesture(GridView gridView) {
        gridView.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return false; // ⚠️ 一定要 false
        });
    }


    private void setupPage1() {
        gridViewPage1 = new GridView(this);
        gridViewPage1.setNumColumns(4);
        gridViewPage1.setVerticalSpacing(30);
        gridViewPage1.setHorizontalSpacing(20);
        gridViewPage1.setPadding(40, 40, 40, 40);
        gridViewPage1.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        gridViewPage1.setAdapter(new AppAdapter(appNamesPage1, appIconsPage1));
        gridViewPage1.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(MainActivity.this,
                    "打开: " + appNamesPage1[position], Toast.LENGTH_SHORT).show();
        });

        // 设置GridView的背景和选择器
        gridViewPage1.setBackgroundColor(0xFF1A1A1A);
        gridViewPage1.setSelector(android.R.color.transparent);

        viewSwitcher.addView(gridViewPage1);

        attachGesture(gridViewPage1);
        //attachGesture(gridViewPage2);

    }

    private void setupPage2() {
        gridViewPage2 = new GridView(this);
        gridViewPage2.setNumColumns(4);
        gridViewPage2.setVerticalSpacing(30);
        gridViewPage2.setHorizontalSpacing(20);
        gridViewPage2.setPadding(40, 40, 40, 40);
        gridViewPage2.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        gridViewPage2.setAdapter(new AppAdapter(appNamesPage2, appIconsPage2));
        gridViewPage2.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(MainActivity.this,
                    "打开: " + appNamesPage2[position], Toast.LENGTH_SHORT).show();
        });

        // 设置GridView的背景和选择器
        gridViewPage2.setBackgroundColor(0xFF1A1A1A);
        gridViewPage2.setSelector(android.R.color.transparent);

        viewSwitcher.addView(gridViewPage2);

        //attachGesture(gridViewPage1);
        attachGesture(gridViewPage2);

    }

    private void setupDock() {
        for (int i = 0; i < dockAppNames.length; i++) {
            LinearLayout appItem = (LinearLayout) getLayoutInflater()
                    .inflate(R.layout.item_dock_app, null);

            ImageView icon = appItem.findViewById(R.id.dockIcon);
            TextView name = appItem.findViewById(R.id.dockName);

            icon.setImageResource(dockIcons[i]);
            icon.setColorFilter(0xFFFFFFFF); // 设置图标为白色
            name.setText(dockAppNames[i]);

            final int position = i;
            appItem.setOnClickListener(v -> {
                Toast.makeText(MainActivity.this,
                        "打开Dock应用: " + dockAppNames[position], Toast.LENGTH_SHORT).show();
            });

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            params.setMargins(10, 0, 10, 0);
            appItem.setLayoutParams(params);

            dockLayout.addView(appItem);
        }
    }

    private void setupPageIndicator() {
        updatePageIndicator();
    }

    @SuppressLint("SetTextI18n")
    private void updatePageIndicator() {
        pageIndicator.setText((currentPage + 1) + "/" + PAGE_COUNT);
    }

    // 手势监听器
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 120;
        private static final int SWIPE_VELOCITY_THRESHOLD = 120;

        @Override
        public boolean onDown(MotionEvent e) {
            return true; // ⚠️ 必须 true，否则 fling 不触发
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {

            float diffX = e2.getX() - e1.getX();

            if (Math.abs(diffX) > SWIPE_THRESHOLD
                    && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {

                if (diffX > 0) {
                    showPreviousPage();
                } else {
                    showNextPage();
                }
                return true;
            }
            return false;
        }
    }


    public void showNextPage() {
        if (currentPage < PAGE_COUNT - 1) {
            // 设置从左向右滑入的动画
            Animation inFromRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
            Animation outToLeft = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
            viewSwitcher.setInAnimation(inFromRight);
            viewSwitcher.setOutAnimation(outToLeft);

            viewSwitcher.showNext();
            currentPage++;
            updatePageIndicator();

            Log.d("ViewSwitcher", "切换到第" + (currentPage + 1) + "页");
        }
    }

    public void showPreviousPage() {
        if (currentPage > 0) {
            // 设置从右向左滑入的动画
            Animation inFromLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
            Animation outToRight = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
            viewSwitcher.setInAnimation(inFromLeft);
            viewSwitcher.setOutAnimation(outToRight);

            viewSwitcher.showPrevious();
            currentPage--;
            updatePageIndicator();

            Log.d("ViewSwitcher", "切换到第" + (currentPage + 1) + "页");
        }
    }

    // 自定义适配器
    private class AppAdapter extends BaseAdapter {
        private String[] appNames;
        private int[] appIcons;

        public AppAdapter(String[] names, int[] icons) {
            this.appNames = names;
            this.appIcons = icons;
        }

        @Override
        public int getCount() {
            return appNames.length;
        }

        @Override
        public Object getItem(int position) {
            return appNames[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_app, parent, false);
                holder = new ViewHolder();
                holder.icon = convertView.findViewById(R.id.appIcon);
                holder.name = convertView.findViewById(R.id.appName);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.icon.setImageResource(appIcons[position]);
            holder.icon.setColorFilter(0xFFFFFFFF); // 设置图标为白色
            holder.name.setText(appNames[position]);

            return convertView;
        }

        class ViewHolder {
            ImageView icon;
            TextView name;
        }
    }
}

