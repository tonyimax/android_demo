package com.example.demo14;

import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter adapter;
    private List<GroupItem> groupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
        setupExpandableListView();
    }

    private void initData() {
        groupList = new ArrayList<>();

        // 第一组数据
        List<ChildItem> children1 = new ArrayList<>();
        children1.add(new ChildItem("张三", 25, "Android工程师", true, R.drawable.ic_person));
        children1.add(new ChildItem("李四", 30, "后端工程师", true, R.drawable.ic_person));
        children1.add(new ChildItem("王五", 28, "前端工程师", false, R.drawable.ic_person));
        groupList.add(new GroupItem("技术部", children1, R.drawable.ic_tech, true));

        // 第二组数据
        List<ChildItem> children2 = new ArrayList<>();
        children2.add(new ChildItem("赵六", 26, "市场经理", true, R.drawable.ic_person));
        children2.add(new ChildItem("钱七", 32, "销售专员", false, R.drawable.ic_person));
        groupList.add(new GroupItem("市场部", children2, R.drawable.ic_market, true));

        // 第三组数据
        List<ChildItem> children3 = new ArrayList<>();
        children3.add(new ChildItem("孙八", 29, "HR经理", true, R.drawable.ic_person));
        children3.add(new ChildItem("周九", 27, "招聘专员", true, R.drawable.ic_person));
        children3.add(new ChildItem("吴十", 31, "培训师", true, R.drawable.ic_person));
        children3.add(new ChildItem("郑十一", 24, "实习生", false, R.drawable.ic_person));
        groupList.add(new GroupItem("人事部", children3, R.drawable.ic_hr, false));
    }

    private void initView() {
        expandableListView = findViewById(R.id.expandableListView);
        adapter = new ExpandableListAdapter(this, groupList);
        expandableListView.setAdapter(adapter);
    }

    private void setupExpandableListView() {
        // 设置子项点击监听
        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            ChildItem child = (ChildItem) adapter.getChild(groupPosition, childPosition);
            Toast.makeText(MainActivity.this,
                    "点击了: " + child.getName(), Toast.LENGTH_SHORT).show();
            return true;
        });

        // 设置父项点击监听
        expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            // 返回 false 允许展开/折叠，返回 true 则阻止
            return false;
        });

        // 设置分组展开监听
        expandableListView.setOnGroupExpandListener(groupPosition -> Toast.makeText(MainActivity.this,
                groupList.get(groupPosition).getGroupName() + " 展开",
                Toast.LENGTH_SHORT).show());

        // 设置分组折叠监听
        expandableListView.setOnGroupCollapseListener(groupPosition -> Toast.makeText(MainActivity.this,
                groupList.get(groupPosition).getGroupName() + " 折叠",
                Toast.LENGTH_SHORT).show());

        // 默认展开第一组
        expandableListView.expandGroup(0);
    }
}