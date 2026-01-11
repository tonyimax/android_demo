package com.example.demo18;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class StackAdapter extends BaseAdapter {
    private Context context;
    private List<StackItem> items;
    private LayoutInflater inflater;

    public StackAdapter(Context context, List<StackItem> items) {
        this.context = context;
        this.items = items;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_stack, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.imageView);
            holder.textTitle = convertView.findViewById(R.id.textTitle);
            holder.textDescription = convertView.findViewById(R.id.textDescription);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StackItem item = items.get(position);
        holder.imageView.setImageResource(item.getImageResId());
        holder.textTitle.setText(item.getTitle());
        holder.textDescription.setText(item.getDescription());

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textTitle;
        TextView textDescription;
    }
}
