package com.metrox.demo57;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends ExpandableListActivity {

    private ExpandableListAdapter listAdapter;
    private List<GroupItem> groupItems;
    private HashMap<GroupItem, List<ChildItem>> childItemsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化数据
        prepareListData();

        // 创建适配器
        listAdapter = new ExpandableListAdapter(this, groupItems, childItemsMap);

        // 设置适配器
        setListAdapter(listAdapter);

        // 设置列表点击监听器
        getExpandableListView().setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // 返回false允许默认的展开/折叠行为
                // 返回true则阻止默认行为
                return false;
            }
        });

        getExpandableListView().setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                ChildItem childItem = (ChildItem) listAdapter.getChild(groupPosition, childPosition);
                Toast.makeText(MainActivity.this,
                        "点击了: " + childItem.getName(),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        getExpandableListView().setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(MainActivity.this,
                        groupItems.get(groupPosition).getGroupName() + " 展开",
                        Toast.LENGTH_SHORT).show();
            }
        });

        getExpandableListView().setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(MainActivity.this,
                        groupItems.get(groupPosition).getGroupName() + " 折叠",
                        Toast.LENGTH_SHORT).show();
            }
        });

        Button btnExpandAll = findViewById(R.id.btn_expand_all);
        Button btnCollapseAll = findViewById(R.id.btn_collapse_all);
        btnExpandAll.setOnClickListener(v -> expandAllGroups());
        btnCollapseAll.setOnClickListener(v -> collapseAllGroups());
    }

    private void prepareListData() {
        groupItems = new ArrayList<>();
        childItemsMap = new HashMap<>();

        // 添加第一个组
        List<ChildItem> fruits = new ArrayList<>();
        fruits.add(new ChildItem("苹果", "富含维生素C"));
        fruits.add(new ChildItem("香蕉", "富含钾元素"));
        fruits.add(new ChildItem("橙子", "富含维生素C"));
        fruits.add(new ChildItem("葡萄", "富含抗氧化剂"));

        GroupItem fruitGroup = new GroupItem("水果", fruits);
        groupItems.add(fruitGroup);
        childItemsMap.put(fruitGroup, fruits);

        // 添加第二个组
        List<ChildItem> vegetables = new ArrayList<>();
        vegetables.add(new ChildItem("胡萝卜", "富含维生素A"));
        vegetables.add(new ChildItem("西兰花", "富含纤维"));
        vegetables.add(new ChildItem("菠菜", "富含铁质"));

        GroupItem vegetableGroup = new GroupItem("蔬菜", vegetables);
        groupItems.add(vegetableGroup);
        childItemsMap.put(vegetableGroup, vegetables);

        // 添加第三个组
        List<ChildItem> drinks = new ArrayList<>();
        drinks.add(new ChildItem("水", "每天8杯水"));
        drinks.add(new ChildItem("茶", "绿茶有益健康"));
        drinks.add(new ChildItem("咖啡", "适量饮用"));
        drinks.add(new ChildItem("果汁", "新鲜果汁"));

        GroupItem drinkGroup = new GroupItem("饮品", drinks);
        groupItems.add(drinkGroup);
        childItemsMap.put(drinkGroup, drinks);

        // 添加更多组...
        List<ChildItem> snacks = new ArrayList<>();
        snacks.add(new ChildItem("坚果", "健康零食"));
        snacks.add(new ChildItem("酸奶", "富含益生菌"));

        GroupItem snackGroup = new GroupItem("零食", snacks);
        groupItems.add(snackGroup);
        childItemsMap.put(snackGroup, snacks);
    }

    // 添加一些辅助方法
    public void expandAllGroups() {
        ExpandableListView listView = getExpandableListView();
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            listView.expandGroup(i);
        }
    }

    public void collapseAllGroups() {
        ExpandableListView listView = getExpandableListView();
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            listView.collapseGroup(i);
        }
    }
}