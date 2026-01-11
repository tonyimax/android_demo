package com.example.demo41;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // 定义菜单项ID常量
    private static final int MENU_DYNAMIC_ITEM = 1001;
    private static final int MENU_DELETE = 1002;
    private static final int SUBMENU_SORT_ASC = 2001;
    private static final int SUBMENU_SORT_DESC = 2002;

    // 用于记录动态菜单状态
    private boolean isDynamicMenuAdded = false;
    private boolean isToolbarVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 启用ActionBar（如果使用）
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("菜单示例");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 1. 从XML文件加载菜单
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // 2. 动态添加菜单项（编程方式）
        // 添加一个动态菜单项
        menu.add(Menu.NONE, MENU_DELETE, 2, "删除")
                .setIcon(android.R.drawable.ic_menu_delete)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        // 3. 动态添加子菜单
        SubMenu sortSubMenu = menu.addSubMenu("排序")
                .setIcon(android.R.drawable.ic_menu_sort_by_size);

        // 设置子菜单项
        sortSubMenu.add(Menu.NONE, SUBMENU_SORT_ASC, 1, "升序");
        sortSubMenu.add(Menu.NONE, SUBMENU_SORT_DESC, 2, "降序");

        // 设置子菜单头的图标
        sortSubMenu.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        // 4. 动态修改已存在的菜单项
        MenuItem aboutItem = menu.findItem(R.id.menu_about);
        if (aboutItem != null) {
            aboutItem.setTitle("关于我们");
            aboutItem.setIcon(android.R.drawable.ic_dialog_info);
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // 每次显示菜单前调用，可以动态更新菜单项

        // 动态添加/移除菜单项
        if (!isDynamicMenuAdded) {
            menu.add(Menu.NONE, MENU_DYNAMIC_ITEM, 100, "动态菜单项")
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            isDynamicMenuAdded = true;
        }

        // 更新复选框状态
        MenuItem toolbarItem = menu.findItem(R.id.menu_show_toolbar);
        if (toolbarItem != null) {
            toolbarItem.setChecked(isToolbarVisible);
        }

        // 根据条件启用/禁用菜单项
        MenuItem editItem = menu.findItem(R.id.menu_edit);
        if (editItem != null) {
            // 假设某些条件下禁用编辑菜单
            editItem.setEnabled(true); // 或 false
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_settings) {
            showToast("打开设置");
            return true;
        } else if (id == R.id.menu_help) {
            showToast("打开帮助");
            return true;
        } else if (id == R.id.menu_about) {
            showToast("关于我们");
            return true;
        } else if (id == R.id.menu_view_list) {
            item.setChecked(true);
            showToast("切换到列表视图");
            return true;
        } else if (id == R.id.menu_view_grid) {
            item.setChecked(true);
            showToast("切换到网格视图");
            return true;
        } else if (id == R.id.menu_view_details) {
            item.setChecked(true);
            showToast("切换到详细视图");
            return true;
        } else if (id == R.id.menu_show_toolbar) {
            isToolbarVisible = !item.isChecked();
            item.setChecked(isToolbarVisible);
            showToast(isToolbarVisible ? "显示工具栏" : "隐藏工具栏");
            return true;
        } else if (id == MENU_DELETE) {
            showToast("删除项目");
            return true;
        } else if (id == MENU_DYNAMIC_ITEM) {
            showToast("动态菜单项被点击");
            return true;
        } else if (id == SUBMENU_SORT_ASC) {
            showToast("按升序排序");
            return true;
        } else if (id == SUBMENU_SORT_DESC) {
            showToast("按降序排序");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onOptionsMenuClosed(Menu menu) {
        // 菜单关闭时调用
        super.onOptionsMenuClosed(menu);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}