package com.example.demo31;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> implements Filterable {

    private List<DataItem> originalList;
    private List<DataItem> filteredList;

    public DataAdapter(List<DataItem> items) {
        this.originalList = items;
        this.filteredList = new ArrayList<>(items);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataItem item = filteredList.get(position);
        holder.tvName.setText(item.getName());
        holder.tvDescription.setText(item.getDescription());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<DataItem> filtered = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filtered.addAll(originalList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (DataItem item : originalList) {
                        if (item.getName().toLowerCase().contains(filterPattern) ||
                                item.getDescription().toLowerCase().contains(filterPattern)) {
                            filtered.add(item);
                        }
                    }
                }

                results.values = filtered;
                results.count = filtered.size();
                return results;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList.clear();
                filteredList.addAll((List<DataItem>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public void updateOriginalList(List<DataItem> newList) {
        originalList = new ArrayList<>(newList);
        filteredList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvDescription;

        ViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_name);
            tvDescription = view.findViewById(R.id.tv_description);
        }
    }

    // 点击监听接口
    public interface OnItemClickListener {
        void onItemClick(DataItem item);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
