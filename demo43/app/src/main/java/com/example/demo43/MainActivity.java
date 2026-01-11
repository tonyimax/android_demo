package com.example.demo43;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 设置 ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 设置标题
            actionBar.setTitle("主界面");
            // 设置副标题
            actionBar.setSubtitle("欢迎使用");
            // 显示返回按钮（如果有父Activity）
            actionBar.setDisplayHomeAsUpEnabled(false);
            // 显示应用图标
            actionBar.setDisplayShowHomeEnabled(true);
            // 设置应用图标可点击
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 加载菜单
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_search) {
            Toast.makeText(this, "搜索功能", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_settings) {
            Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "分享内容");
            startActivity(Intent.createChooser(shareIntent, "分享到"));
            return true;
        } else if (id == R.id.action_about) {
            Toast.makeText(this, "关于应用", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_next) {
            Intent intent = new Intent(this, SecondActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // 修改标题按钮点击事件
    public void onChangeTitleClick(View view) {
        updateActionBarTitle("新标题 - " + System.currentTimeMillis());
    }

    // 切换ActionBar显示/隐藏
    public void onToggleActionBarClick(View view) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (actionBar.isShowing()) {
                actionBar.hide();
                Toast.makeText(this, "ActionBar已隐藏", Toast.LENGTH_SHORT).show();
            } else {
                actionBar.show();
                Toast.makeText(this, "ActionBar已显示", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 动态修改 ActionBar 标题
    public void updateActionBarTitle(String newTitle) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(newTitle);
        }
    }
}