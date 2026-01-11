package com.example.demo45;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String[] navItems = {"首页", "消息", "通知", "设置", "帮助"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 显示主页作为默认
        showFragment(new HomeFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // 设置下拉导航
        MenuItem item = menu.findItem(R.id.spinner);
        androidx.appcompat.widget.AppCompatSpinner spinner =
                (androidx.appcompat.widget.AppCompatSpinner) item.getActionView();

        if (spinner != null) {
            // 创建适配器
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    navItems
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(adapter);

            // 设置下拉项选中监听
            spinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view,
                                           int position, long id) {
                    onNavigationItemSelected(position);
                }

                @Override
                public void onNothingSelected(android.widget.AdapterView<?> parent) {
                }
            });
        }

        return true;
    }

    /**
     * 处理下拉项选择
     */
    private void onNavigationItemSelected(int position) {
        Fragment fragment;
        String title = navItems[position];

        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new MessagesFragment();
                break;
            case 2:
                fragment = new NotificationsFragment();
                break;
            case 3:
                fragment = new SettingsFragment();
                break;
            case 4:
                fragment = new HelpFragment();
                break;
            default:
                fragment = new HomeFragment();
        }

        // 更新Toolbar标题
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        // 显示对应的Fragment
        showFragment(fragment);

        // 显示Toast提示
        Toast.makeText(this, "切换到: " + title, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示Fragment
     */
    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}