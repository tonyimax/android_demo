package com.example.demo42;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;
    private Button showPopupBtn;
    private ImageButton moreOptionsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        resultTextView = findViewById(R.id.result_text_view);
        showPopupBtn = findViewById(R.id.show_popup_btn);
        moreOptionsBtn = findViewById(R.id.more_options_btn);

        // 为按钮设置点击监听器
        showPopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        // 为图片按钮设置点击监听器
        moreOptionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreOptionsMenu(v);
            }
        });
    }

    // 显示基本 PopupMenu
    private void showPopupMenu(View view) {
        // 创建 PopupMenu 对象
        PopupMenu popupMenu = new PopupMenu(this, view);

        // 从 menu 资源文件加载菜单
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        // 为菜单项设置点击监听器
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.menu_item_1) {
                    handleMenuItemClick("选项 1 被选中");
                    return true;
                } else if (itemId == R.id.menu_item_2) {
                    handleMenuItemClick("选项 2 被选中");
                    return true;
                } else if (itemId == R.id.menu_item_3) {
                    handleMenuItemClick("选项 3 被选中");
                    return true;
                } else if (itemId == R.id.menu_item_sub) {
                    // 子菜单项处理
                    Toast.makeText(MainActivity.this, "点击了子菜单", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.sub_item_1) {
                    handleMenuItemClick("子选项 1 被选中");
                    return true;
                } else if (itemId == R.id.sub_item_2) {
                    handleMenuItemClick("子选项 2 被选中");
                    return true;
                }
                return false;
            }
        });

        // 设置 PopupMenu 关闭监听器
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                Toast.makeText(MainActivity.this, "菜单已关闭", Toast.LENGTH_SHORT).show();
            }
        });

        // 显示 PopupMenu
        popupMenu.show();
    }

    // 显示更多选项菜单（带图标）
    private void showMoreOptionsMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.more_options_menu, popupMenu.getMenu());

        // 使用 Lambda 表达式简化代码（需要 API 24+）
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.action_edit) {
                handleMenuItemClick("编辑操作");
                return true;
            } else if (itemId == R.id.action_delete) {
                handleMenuItemClick("删除操作");
                showDeleteConfirmation();
                return true;
            } else if (itemId == R.id.action_share) {
                handleMenuItemClick("分享操作");
                return true;
            } else if (itemId == R.id.action_settings) {
                handleMenuItemClick("设置操作");
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    // 处理菜单项点击
    private void handleMenuItemClick(String message) {
        resultTextView.setText(message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // 显示删除确认
    private void showDeleteConfirmation() {
        // 可以在这里添加删除确认对话框
        Toast.makeText(this, "确定要删除吗？", Toast.LENGTH_LONG).show();
    }
}