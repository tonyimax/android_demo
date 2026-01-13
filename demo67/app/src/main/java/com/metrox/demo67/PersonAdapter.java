package com.metrox.demo67;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> implements Filterable {

    private List<Person> personList;
    private List<Person> personListFull; // 用于搜索过滤的完整列表
    private OnItemClickListener listener;

    // 点击监听器接口
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public PersonAdapter(List<Person> personList) {
        this.personList = personList;
        this.personListFull = new ArrayList<>(personList);
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_person, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        Person currentPerson = personList.get(position);

        holder.ivAvatar.setImageResource(currentPerson.getAvatarResId());
        holder.tvName.setText(currentPerson.getName());
        holder.tvEmail.setText(currentPerson.getEmail());
        holder.tvAge.setText("年龄: " + currentPerson.getAge());

        // 点击整个项
        holder.itemView.setOnClickListener(v -> {
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position);
            }
        });

        // 点击删除按钮
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    // 更新数据
    public void updateData(List<Person> newList) {
        personList.clear();
        personList.addAll(newList);
        personListFull = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    // 删除项目
    public void removeItem(int position) {
        personList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, personList.size());
    }

    // 添加项目
    public void addItem(Person person) {
        personList.add(person);
        notifyItemInserted(personList.size() - 1);
    }

    // ViewHolder类
    static class PersonViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvName;
        TextView tvEmail;
        TextView tvAge;
        ImageButton btnDelete;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvAge = itemView.findViewById(R.id.tvAge);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    // 实现搜索过滤功能
    @Override
    public Filter getFilter() {
        return personFilter;
    }

    private Filter personFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Person> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(personListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Person person : personListFull) {
                    if (person.getName().toLowerCase().contains(filterPattern) ||
                            person.getEmail().toLowerCase().contains(filterPattern)) {
                        filteredList.add(person);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            personList.clear();
            personList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
