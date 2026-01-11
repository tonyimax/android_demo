package com.example.demo10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class CheckboxArrayAdapter extends BaseAdapter {
    private Context context;
    private String[] data;
    private boolean[] checkedStates;

    public CheckboxArrayAdapter(Context context, String[] data) {
        this.context = context;
        this.data = data;
        this.checkedStates = new boolean[data.length];
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public String getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.check_item, parent, false);
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.tv_array_item);
            holder.checkBox = convertView.findViewById(R.id.cb_check);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(data[position]);
        holder.checkBox.setChecked(checkedStates[position]);

        holder.checkBox.setOnCheckedChangeListener(null); // 先清除监听器
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedStates[position] = isChecked;
            }
        });

        return convertView;
    }

    // 获取选中状态的方法
    public boolean isChecked(int position) {
        return checkedStates[position];
    }

    // 设置选中状态的方法
    public void setChecked(int position, boolean isChecked) {
        checkedStates[position] = isChecked;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView textView;
        CheckBox checkBox;
    }
}