package com.metrox.demo56;

import android.app.LauncherActivity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Android 系统 LauncherActivity 的基础实现
 * 注意：LauncherActivity 已弃用，但可以作为学习使用
 */
public class SystemLauncherActivity extends LauncherActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 可以在这里设置自定义布局（可选）
        setTitle("系统启动器");
    }

    @Override
    protected Intent getTargetIntent() {
        // 这个方法返回基础 Intent，LauncherActivity 会使用这个 Intent
        // 来查询 PackageManager 获取符合条件的 Activity
        Intent baseIntent = new Intent(Intent.ACTION_MAIN, null);
        baseIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        // 你可以添加额外的过滤条件
        // baseIntent.addCategory("com.example.CUSTOM_CATEGORY");

        return baseIntent;
    }

    @Override
    protected List<ResolveInfo> onQueryPackageManager(Intent queryIntent) {
        // 这个方法查询 PackageManager，获取所有匹配的 Activity
        // 默认实现会查询所有符合条件的 Activity
        List<ResolveInfo> activities = super.onQueryPackageManager(queryIntent);

        // 可以在这里进行过滤，比如排除自己
        List<ResolveInfo> filtered = new ArrayList<>();
        String myClassName = getClass().getName();

        for (ResolveInfo info : activities) {
            if (!info.activityInfo.name.equals(myClassName)) {
                filtered.add(info);
            }
        }

        return filtered;
    }

    @Override
    protected Intent intentForPosition(int position) {
        // 为指定位置创建 Intent
        // 默认实现会创建启动相应 Activity 的 Intent
        ResolveInfo info = (ResolveInfo) getListAdapter().getItem(position);

        Intent intent = new Intent(getTargetIntent());
        intent.setClassName(
                info.activityInfo.applicationInfo.packageName,
                info.activityInfo.name
        );

        // 添加额外的标记
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // 处理列表项点击事件
        ResolveInfo info = (ResolveInfo) getListAdapter().getItem(position);
        String appName = info.loadLabel(getPackageManager()).toString();

        // 显示 Toast
        android.widget.Toast.makeText(
                this,
                "启动: " + appName,
                android.widget.Toast.LENGTH_SHORT
        ).show();

        // 调用父类方法启动 Activity
        super.onListItemClick(l, v, position, id);
    }

    @Override
    protected void onSetContentView() {
        // 可以在这里设置自定义的布局
        // 默认会使用 ListActivity 的布局
        super.onSetContentView();

        // 可以在这里获取 ListView 并进行自定义
        ListView listView = getListView();
        listView.setDividerHeight(2);
        listView.setCacheColorHint(android.graphics.Color.TRANSPARENT);
    }
}