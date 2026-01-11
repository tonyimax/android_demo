package com.example.demo38;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnBasicPopup, btnListPopup, btnCustomPopup, btnInputPopup, btnFullscreenPopup;
    private TextView tvResult;

    private PopupWindow currentPopup;
    private View dimBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        btnBasicPopup = findViewById(R.id.btn_basic_popup);
        btnListPopup = findViewById(R.id.btn_list_popup);
        btnCustomPopup = findViewById(R.id.btn_custom_popup);
        btnInputPopup = findViewById(R.id.btn_input_popup);
        btnFullscreenPopup = findViewById(R.id.btn_fullscreen_popup);
        tvResult = findViewById(R.id.tv_result);
    }

    private void setupClickListeners() {
        btnBasicPopup.setOnClickListener(v -> showBasicPopup());
        btnListPopup.setOnClickListener(v -> showListPopup());
        btnCustomPopup.setOnClickListener(v -> showCustomPopup());
        btnInputPopup.setOnClickListener(v -> showInputPopup());
        btnFullscreenPopup.setOnClickListener(v -> showFullscreenPopup());
    }

    // 1. 基础PopupWindow示例
    private void showBasicPopup() {
        // 加载布局
        View popupView = LayoutInflater.from(this)
                .inflate(R.layout.popup_basic, null);

        // 创建PopupWindow
        currentPopup = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        // 设置背景
        currentPopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 设置动画
        currentPopup.setAnimationStyle(R.style.PopupAnimation);

        // 设置点击外部可关闭
        currentPopup.setOutsideTouchable(true);

        // 初始化控件
        TextView tvMessage = popupView.findViewById(R.id.tv_message);
        Button btnCancel = popupView.findViewById(R.id.btn_cancel);
        Button btnConfirm = popupView.findViewById(R.id.btn_confirm);

        // 设置点击事件
        btnCancel.setOnClickListener(v -> {
            tvResult.setText("点击了取消按钮");
            currentPopup.dismiss();
        });

        btnConfirm.setOnClickListener(v -> {
            tvResult.setText("点击了确定按钮");
            currentPopup.dismiss();
        });

        // 设置消失监听
        currentPopup.setOnDismissListener(() -> {
            tvResult.setText("Popup已关闭");
        });

        // 显示PopupWindow（在按钮下方）
        currentPopup.showAsDropDown(btnBasicPopup, 0, 10);
    }

    // 2. 列表PopupWindow示例
    private void showListPopup() {
        View popupView = LayoutInflater.from(this)
                .inflate(R.layout.popup_list, null);

        currentPopup = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        currentPopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        currentPopup.setOutsideTouchable(true);

        ListView listView = popupView.findViewById(R.id.list_view);

        // 创建数据
        List<String> items = new ArrayList<>();
        items.add("选项一");
        items.add("选项二");
        items.add("选项三");
        items.add("选项四");
        items.add("选项五");

        // 创建适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                items
        );
        listView.setAdapter(adapter);

        // 设置列表项点击事件
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = items.get(position);
            tvResult.setText("选择了: " + selectedItem);
            currentPopup.dismiss();
        });

        // 显示PopupWindow
        currentPopup.showAsDropDown(btnListPopup);
    }

    // 3. 自定义位置Popup示例
    private void showCustomPopup() {
        View popupView = LayoutInflater.from(this)
                .inflate(R.layout.popup_custom, null);

        currentPopup = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        currentPopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        currentPopup.setAnimationStyle(android.R.style.Animation_Dialog);

        // 设置关闭按钮点击事件
        Button btnClose = popupView.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(v -> {
            tvResult.setText("自定义Popup已关闭");
            currentPopup.dismiss();
        });

        // 获取按钮在屏幕中的位置
        int[] location = new int[2];
        btnCustomPopup.getLocationOnScreen(location);

        // 计算Popup显示位置
        int popupX = location[0] + btnCustomPopup.getWidth() / 2;
        int popupY = location[1] - dpToPx(200);

        // 显示在指定位置
        currentPopup.showAtLocation(
                btnCustomPopup,
                Gravity.NO_GRAVITY,
                popupX,
                popupY
        );
    }

    // 4. 带输入法的Popup示例
    private void showInputPopup() {
        // 创建包含EditText的自定义布局
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundResource(R.drawable.popup_rounded);
        layout.setPadding(dpToPx(20), dpToPx(20), dpToPx(20), dpToPx(20));

        // 创建EditText
        EditText editText = new EditText(this);
        editText.setHint("请输入内容");
        editText.setBackgroundResource(android.R.drawable.edit_text);
        editText.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));
        editText.setMinWidth(dpToPx(250));

        // 创建按钮
        Button btnSubmit = new Button(this);
        btnSubmit.setText("提交");
        btnSubmit.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark));
        btnSubmit.setTextColor(Color.WHITE);
        btnSubmit.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));

        layout.addView(editText);
        layout.addView(btnSubmit, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0
        ));

        currentPopup = new PopupWindow(
                layout,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        // 设置输入法模式
        currentPopup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        currentPopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        currentPopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 设置按钮点击事件
        btnSubmit.setOnClickListener(v -> {
            String input = editText.getText().toString().trim();
            if (!input.isEmpty()) {
                tvResult.setText("输入的内容: " + input);
                currentPopup.dismiss();
            } else {
                Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            }
        });

        // 显示在屏幕底部
        currentPopup.showAtLocation(
                btnInputPopup,
                Gravity.BOTTOM,
                0,
                dpToPx(50)
        );

        // 自动弹出输入法
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    // 5. 全屏背景Popup示例
    private void showFullscreenPopup() {
        View popupView = LayoutInflater.from(this)
                .inflate(R.layout.popup_basic, null);

        // 修改文本
        TextView tvMessage = popupView.findViewById(R.id.tv_message);
        tvMessage.setText("这是一个带半透明背景的PopupWindow\n点击背景区域可以关闭");

        currentPopup = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        currentPopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        currentPopup.setAnimationStyle(R.style.PopupAnimation);
        currentPopup.setOutsideTouchable(true);

        // 添加暗色背景
        addDimBackground();

        // 设置按钮事件
        Button btnCancel = popupView.findViewById(R.id.btn_cancel);
        Button btnConfirm = popupView.findViewById(R.id.btn_confirm);

        btnCancel.setOnClickListener(v -> {
            tvResult.setText("全屏Popup - 取消");
            removeDimBackground();
            currentPopup.dismiss();
        });

        btnConfirm.setOnClickListener(v -> {
            tvResult.setText("全屏Popup - 确定");
            removeDimBackground();
            currentPopup.dismiss();
        });

        // 设置消失监听
        currentPopup.setOnDismissListener(() -> {
            removeDimBackground();
        });

        // 显示在屏幕中间
        currentPopup.showAtLocation(
                btnFullscreenPopup,
                Gravity.CENTER,
                0,
                0
        );
    }

    // 添加暗色背景
    private void addDimBackground() {
        if (dimBackground == null) {
            dimBackground = new View(this);
            dimBackground.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            dimBackground.setBackgroundColor(Color.argb(0, 0, 0, 0));

            // 获取根视图
            ViewGroup rootView = findViewById(android.R.id.content);
            rootView.addView(dimBackground);

            // 添加点击事件
            dimBackground.setOnClickListener(v -> {
                if (currentPopup != null && currentPopup.isShowing()) {
                    currentPopup.dismiss();
                }
            });

            // 添加渐变动画
            ValueAnimator animator = ValueAnimator.ofObject(
                    new ArgbEvaluator(),
                    Color.argb(0, 0, 0, 0),
                    Color.argb(150, 0, 0, 0)
            );
            animator.setDuration(300);
            animator.addUpdateListener(animation -> {
                dimBackground.setBackgroundColor((int) animation.getAnimatedValue());
            });
            animator.start();
        }
    }

    // 移除暗色背景
    private void removeDimBackground() {
        if (dimBackground != null) {
            ViewGroup rootView = findViewById(android.R.id.content);
            rootView.removeView(dimBackground);
            dimBackground = null;
        }
    }

    // dp转px
    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清理资源
        if (currentPopup != null && currentPopup.isShowing()) {
            currentPopup.dismiss();
        }
        removeDimBackground();
    }
}