package com.example.demo32;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

    private TabHost tabHost;
    private EditText editTextTab1;
    private Button btnTab1;
    private ListView listViewTab2;
    private Switch switchNotification;
    private SeekBar seekBarVolume;
    private TextView tvVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 TabHost
        tabHost = getTabHost();

        // 初始化各个Tab中的控件
        initializeTab1();
        initializeTab2();
        initializeTab3();

        // 设置Tab标签
        setupTabs();

        // 默认选择第一个Tab
        tabHost.setCurrentTab(0);
    }

    private void initializeTab1() {
        editTextTab1 = findViewById(R.id.editTextTab1);
        btnTab1 = findViewById(R.id.btnTab1);

        btnTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editTextTab1.getText().toString();
                if (!input.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            "你输入了: " + input, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this,
                            "请输入内容", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initializeTab2() {
        listViewTab2 = findViewById(R.id.listViewTab2);

        // 示例数据
        String[] messages = {
                "欢迎消息",
                "系统通知",
                "用户留言",
                "更新提醒",
                "活动通知",
                "好友请求",
                "系统公告",
                "版本更新"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                messages
        );
        listViewTab2.setAdapter(adapter);
    }

    private void initializeTab3() {
        switchNotification = findViewById(R.id.switchNotification);
        seekBarVolume = findViewById(R.id.seekBarVolume);
        tvVolume = findViewById(R.id.tvVolume);

        switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String status = isChecked ? "开启" : "关闭";
            Toast.makeText(MainActivity.this,
                    "通知已" + status, Toast.LENGTH_SHORT).show();
        });

        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvVolume.setText("音量: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 开始拖动时调用
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 停止拖动时调用
                Toast.makeText(MainActivity.this,
                        "音量设置完成: " + seekBar.getProgress(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupTabs() {
        Resources res = getResources();

        // Tab 1
        TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1");
        tab1.setIndicator("首页", res.getDrawable(android.R.drawable.ic_menu_edit));
        tab1.setContent(R.id.tab1);
        tabHost.addTab(tab1);

        // Tab 2
        TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2");
        tab2.setIndicator("消息", res.getDrawable(android.R.drawable.ic_dialog_email));
        tab2.setContent(R.id.tab2);
        tabHost.addTab(tab2);

        // Tab 3
        TabHost.TabSpec tab3 = tabHost.newTabSpec("tab3");
        tab3.setIndicator("设置", res.getDrawable(android.R.drawable.ic_menu_preferences));
        tab3.setContent(R.id.tab3);
        tabHost.addTab(tab3);

        // 设置Tab切换监听器
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                String tabName = "";
                switch (tabId) {
                    case "tab1":
                        tabName = "首页";
                        break;
                    case "tab2":
                        tabName = "消息";
                        break;
                    case "tab3":
                        tabName = "设置";
                        break;
                }
                Toast.makeText(MainActivity.this,
                        "切换到: " + tabName, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清理资源
        if (tabHost != null) {
            tabHost.clearAllTabs();
        }
    }
}