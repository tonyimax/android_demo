package com.example.demo31;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvEmptyState;
    private DataAdapter adapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initData();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        tvEmptyState = findViewById(R.id.tv_empty_state);

        // 设置RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 添加列表为空时的监听
        adapter = new DataAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // 设置点击监听
        adapter.setOnItemClickListener(item -> {
            Toast.makeText(MainActivity.this,
                    "点击了: " + item.getName(), Toast.LENGTH_SHORT).show();
        });

        // 监听适配器数据变化，显示/隐藏空状态
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkEmptyState();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkEmptyState();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkEmptyState();
            }

            private void checkEmptyState() {
                boolean isEmpty = adapter.getItemCount() == 0;
                tvEmptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
                recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
            }
        });
    }

    private void initData() {
        List<DataItem> items = new ArrayList<>();
        items.add(new DataItem("苹果", "一种常见的水果，富含维生素C"));
        items.add(new DataItem("香蕉", "热带水果，富含钾元素"));
        items.add(new DataItem("橙子", "柑橘类水果，富含维生素C"));
        items.add(new DataItem("葡萄", "浆果类水果，可以制作葡萄酒"));
        items.add(new DataItem("西瓜", "夏季消暑水果，水分充足"));
        items.add(new DataItem("草莓", "红色浆果，富含抗氧化剂"));
        items.add(new DataItem("芒果", "热带水果，口感香甜"));
        items.add(new DataItem("菠萝", "热带水果，含有菠萝蛋白酶"));
        items.add(new DataItem("桃子", "核果类水果，果肉多汁"));
        items.add(new DataItem("梨子", "常见水果，有润肺功效"));

        adapter.updateOriginalList(items);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // 获取SearchView
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();

        // 配置SearchView
        setupSearchView();

        return true;
    }

    private void setupSearchView() {
        if (searchView == null) return;

        // 设置搜索提示
        searchView.setQueryHint("输入水果名称搜索...");

        // 设置搜索监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 用户提交搜索（按回车键）
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 文本变化时实时搜索
                performSearch(newText);
                return true;
            }
        });

        // 设置搜索框展开/关闭监听
        searchView.setOnSearchClickListener(v -> {
            // 搜索框展开时的操作
        });

        searchView.setOnCloseListener(() -> {
            // 搜索框关闭时的操作
            return false;
        });

        // 设置搜索框默认展开（可选）
        // searchItem.expandActionView();

        // 自定义SearchView样式（可选）
        int searchPlateId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchView.findViewById(searchPlateId);
        if (searchPlate != null) {
            searchPlate.setBackgroundResource(android.R.color.transparent);
        }
    }

    private void performSearch(String query) {
        if (adapter != null) {
            adapter.getFilter().filter(query);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("GestureBackNavigation")
    @Override
    public void onBackPressed() {
        // 如果SearchView是展开的，先关闭它
        if (searchView != null && !searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}