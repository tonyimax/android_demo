package com.example.demo15;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class FruitAdapter extends ArrayAdapter<Fruit> {
    private Context context;
    private  LayoutInflater inflater;

    public FruitAdapter(Context context, List<Fruit> fruits) {
        super(context, 0, fruits);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_fruit_selected, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.tvFruitNameSelected);
        Fruit fruit = getItem(position);

        if (fruit != null) {
            textView.setText(fruit.getName());
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_fruit_dropdown, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.ivFruit);
            holder.textView = convertView.findViewById(R.id.tvFruitName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Fruit fruit = getItem(position);
        if (fruit != null) {
            holder.textView.setText(fruit.getName());
            holder.imageView.setImageResource(fruit.getImageResId());
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
