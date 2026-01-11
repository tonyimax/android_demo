package com.example.demo12;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    // 数据定义
    private String[] names = {"张三", "李四", "王五", "赵六", "钱七"};
    private String[] emails = {"zhang@email.com", "li@email.com", "wang@email.com",
            "zhao@email.com", "qian@email.com"};
    private String[] ages = {"25", "30", "28", "35", "22"};
    private int[] avatars = {R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setupListView();
    }

    private void initView() {
        listView = findViewById(R.id.listView);
    }

    private void setupListView() {
        // 准备数据
        List<Map<String, Object>> data = new ArrayList<>();

        for (int i = 0; i < names.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", names[i]);
            item.put("email", emails[i]);
            item.put("age", ages[i]);
            item.put("avatar", avatars[i]);
            data.add(item);
        }

        // 定义适配器
        String[] from = {"avatar", "name", "email", "age"};
        int[] to = {R.id.ivAvatar, R.id.tvName, R.id.tvEmail, R.id.tvAge};

        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.list_item, from, to);

        // 设置适配器
        listView.setAdapter(adapter);

        // 设置点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedName = names[position];
                String selectedEmail = emails[position];

                Toast.makeText(MainActivity.this,
                        "点击了: " + selectedName + "\n邮箱: " + selectedEmail,
                        Toast.LENGTH_SHORT).show();
            }
        });

        // 设置长按事件
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedName = names[position];

                Toast.makeText(MainActivity.this,
                        "长按了: " + selectedName,
                        Toast.LENGTH_SHORT).show();

                return true; // 返回true表示消费了该事件
            }
        });
    }
}