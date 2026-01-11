package com.example.demo26;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 各种Toast使用示例
        findViewById(R.id.btn_success).setOnClickListener(v -> {
            ToastUtil.showSuccess(this, "操作成功完成！");
        });

        findViewById(R.id.btn_error).setOnClickListener(v -> {
            ToastUtil.showError(this, "网络连接失败，请检查网络设置");
        });

        findViewById(R.id.btn_warning).setOnClickListener(v -> {
            ToastUtil.showWarning(this, "存储空间不足，请及时清理");
        });

        findViewById(R.id.btn_info).setOnClickListener(v -> {
            ToastUtil.showInfo(this, "新消息已收到");
        });

        findViewById(R.id.btn_center).setOnClickListener(v -> {
            ToastUtil.showCenter(this, "数据加载完成");
        });

        findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            ToastUtil.cancelCurrent();
        });

        // 5. 基本Toast按钮
        Button btnBasic = findViewById(R.id.btn_basic);
        btnBasic.setOnClickListener(v -> {
            ToastUtil.showShort(this, "这是一个基本Toast示例");
        });

        // 6. 长时间Toast按钮
        Button btnLong = findViewById(R.id.btn_long);
        btnLong.setOnClickListener(v -> {
            ToastUtil.showLong(this, "这是一个长时间显示的Toast消息，将持续3.5秒");
        });

        // 7. 顶部Toast按钮
        Button btnTop = findViewById(R.id.btn_top);
        btnTop.setOnClickListener(v -> {
            ToastUtil.showTop(this, "这是显示在屏幕顶部的Toast");
        });

        // 8. 中间Toast按钮
        Button btnCenter = findViewById(R.id.btn_center);
        btnCenter.setOnClickListener(v -> {
            ToastUtil.showCenter(this, "这是显示在屏幕中间的Toast");
        });

        // 9. 自定义Toast按钮
        Button btnCustom = findViewById(R.id.btn_custom);
        btnCustom.setOnClickListener(v -> {
            ToastUtil.showCustom(this, "自定义图标和颜色",
                    R.drawable.ic_check_circle,
                    android.R.color.holo_orange_dark,
                    Toast.LENGTH_LONG);
        });

        // 10. 取消Toast按钮
        Button btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(v -> {
            ToastUtil.cancelCurrent();
            ToastUtil.showShort(this, "已取消当前显示的Toast");
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ToastUtil.cancelCurrent();
    }

    // 方法1: 基本Toast
    private void showBasicToast() {
        Toast.makeText(this, "这是一个基本Toast", Toast.LENGTH_SHORT).show();
    }

    // 方法2: 设置位置的Toast
    private void showPositionToast() {
        Toast toast = Toast.makeText(this, "顶部居中显示的Toast", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.show();
    }

    // 方法3: 完全自定义布局的Toast
    private void showCustomToast() {
        // 加载自定义布局
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_custom,
                findViewById(R.id.custom_toast_container));

        // 设置文本
        TextView text = layout.findViewById(R.id.custom_toast_text);
        text.setText("这是自定义Toast");

        // 创建并显示Toast
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    // 方法4: 在Toast中添加图标
    private void showIconToast() {
        Toast toast = Toast.makeText(this, "带图标的Toast", Toast.LENGTH_SHORT);

        // 获取Toast的View
        View view = toast.getView();
        if (view != null) {
            // 将Toast的View包装在LinearLayout中
            LinearLayout toastLayout = (LinearLayout) view;

            // 创建ImageView
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.ic_info); // 确保有ic_info图片

            // 添加到Toast布局
            toastLayout.addView(imageView, 0); // 0表示添加到最前面
        }

        toast.show();
    }

    // 方法5: 长时间显示
    private void showLongToast() {
        Toast.makeText(this, "这是一个长时间显示的Toast", Toast.LENGTH_LONG).show();
    }

    // 工具方法：线程安全的Toast显示
    public void showToastOnUiThread(final String message) {
        runOnUiThread(() -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show());
    }

    // 工具方法：带回调的Toast
    public void showToastWithCallback(String message, Runnable callback) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);

        // 添加Toast显示完成的监听（需要API 30+）
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            toast.addCallback(new Toast.Callback() {
                @Override
                public void onToastShown() {
                    super.onToastShown();
                }

                @Override
                public void onToastHidden() {
                    super.onToastHidden();
                    if (callback != null) {
                        callback.run();
                    }
                }
            });
        }

        toast.show();
    }
}