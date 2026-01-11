package com.metrox.demo56;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * 自定义列表项布局的 LauncherActivity
 */
public class CustomItemLauncherActivity extends LauncherActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置自定义的适配器
        CustomAdapter adapter = new CustomAdapter(
                this,
                android.R.layout.simple_list_item_1,
                new java.util.ArrayList<ResolveInfo>()
        );
        setListAdapter(adapter);
    }

    @Override
    protected Intent getTargetIntent() {
        // 只显示特定类别的 Activity
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        // 添加自定义过滤（可选）
        // intent.putExtra("com.example.FILTER", true);

        return intent;
    }

    @Override
    protected void onSetContentView() {
        // 使用自定义布局
        setContentView(R.layout.custom_launcher_layout);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        // 获取 ListView 并进行自定义
        android.widget.ListView listView = getListView();
        if (listView != null) {
            // 设置列表属性
            listView.setDivider(getResources().getDrawable(R.drawable.list_divider));
            listView.setDividerHeight(1);
            listView.setSelector(R.drawable.list_selector);

            // 设置空视图
            TextView emptyView = findViewById(R.id.empty_view);
            if (emptyView != null) {
                listView.setEmptyView(emptyView);
            }
        }
    }

    /**
     * 自定义适配器
     */
    private class CustomAdapter extends ArrayAdapter<ResolveInfo> {

        public CustomAdapter(Context context, int resource, List<ResolveInfo> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.custom_list_item, parent, false);

                holder = new ViewHolder();
                holder.icon = convertView.findViewById(R.id.item_icon);
                holder.title = convertView.findViewById(R.id.item_title);
                holder.packageName = convertView.findViewById(R.id.item_package);
                holder.version = convertView.findViewById(R.id.item_version);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ResolveInfo info = getItem(position);
            if (info != null) {
                holder.icon.setImageDrawable(info.loadIcon(getPackageManager()));
                holder.title.setText(info.loadLabel(getPackageManager()));
                holder.packageName.setText(info.activityInfo.packageName);

                try {
                    android.content.pm.PackageInfo pkgInfo =
                            getPackageManager().getPackageInfo(
                                    info.activityInfo.packageName,
                                    0
                            );
                    holder.version.setText("v" + pkgInfo.versionName);
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    holder.version.setText("");
                }
            }

            return convertView;
        }

        private class ViewHolder {
            ImageView icon;
            TextView title;
            TextView packageName;
            TextView version;
        }
    }

    @Override
    protected Intent intentForPosition(int position) {
        ResolveInfo info = (ResolveInfo) getListAdapter().getItem(position);

        Intent intent = new Intent();
        intent.setClassName(
                info.activityInfo.applicationInfo.packageName,
                info.activityInfo.name
        );

        // 添加额外的数据
        intent.putExtra("launched_from", "SystemLauncher");
        intent.putExtra("launch_time", System.currentTimeMillis());

        return intent;
    }
}