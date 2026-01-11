package com.example.demo45;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<NotificationsFragment.Notification> notificationList;
    private OnItemClickListener listener;

    public NotificationAdapter(List<NotificationsFragment.Notification> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationsFragment.Notification notification = notificationList.get(position);

        // 设置基本信息
        holder.textTitle.setText(notification.getTitle());
        holder.textContent.setText(notification.getContent());
        holder.textTime.setText(notification.getTime());

        // 设置图标
        int iconRes = getIconForType(notification.getType());
        holder.imageIcon.setImageResource(iconRes);

        // 设置类型标签
        String typeText = getTypeText(notification.getType());
        if (!typeText.isEmpty()) {
            holder.textType.setText(typeText);
            holder.textType.setVisibility(View.VISIBLE);
        } else {
            holder.textType.setVisibility(View.GONE);
        }

        // 设置重要标记
        if (notification.isImportant()) {
            holder.imageImportant.setVisibility(View.VISIBLE);
        } else {
            holder.imageImportant.setVisibility(View.GONE);
        }

        // 设置未读状态
        if (notification.isRead()) {
            holder.viewUnread.setVisibility(View.GONE);
            holder.itemView.setAlpha(0.7f);
        } else {
            holder.viewUnread.setVisibility(View.VISIBLE);
            holder.itemView.setAlpha(1.0f);

            // 未读通知添加视觉强调
            holder.textTitle.setTextColor(Color.parseColor("#000000"));
            holder.textTitle.setTypeface(holder.textTitle.getTypeface(), android.graphics.Typeface.BOLD);
        }

        // 设置点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(holder.getAdapterPosition());
                }
            }
        });

        // 设置长按事件
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) {
                    listener.onItemLongClick(holder.getAdapterPosition());
                }
                return true;
            }
        });
    }

    private int getIconForType(int type) {
        switch (type) {
            case NotificationsFragment.Notification.TYPE_SYSTEM:
                return android.R.drawable.ic_dialog_info;
            case NotificationsFragment.Notification.TYPE_SECURITY:
                return android.R.drawable.ic_lock_lock;
            case NotificationsFragment.Notification.TYPE_ORDER:
                return android.R.drawable.ic_menu_agenda;
            case NotificationsFragment.Notification.TYPE_PROMOTION:
                return android.R.drawable.star_big_on;
            case NotificationsFragment.Notification.TYPE_SOCIAL:
                return android.R.drawable.ic_menu_myplaces;
            default:
                return android.R.drawable.ic_dialog_info;
        }
    }

    private String getTypeText(int type) {
        switch (type) {
            case NotificationsFragment.Notification.TYPE_SYSTEM:
                return "系统";
            case NotificationsFragment.Notification.TYPE_SECURITY:
                return "安全";
            case NotificationsFragment.Notification.TYPE_ORDER:
                return "订单";
            case NotificationsFragment.Notification.TYPE_PROMOTION:
                return "活动";
            case NotificationsFragment.Notification.TYPE_SOCIAL:
                return "社交";
            default:
                return "";
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public void updateList(List<NotificationsFragment.Notification> newList) {
        notificationList = newList;
        notifyDataSetChanged();
    }

    public void markAsRead(int position) {
        if (position >= 0 && position < notificationList.size()) {
            notificationList.get(position).setRead(true);
            notifyItemChanged(position);
        }
    }

    public void removeItem(int position) {
        if (position >= 0 && position < notificationList.size()) {
            notificationList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, notificationList.size());
        }
    }

    /**
     * ViewHolder 类
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageIcon;
        TextView textTitle;
        TextView textContent;
        TextView textTime;
        View viewUnread;
        ImageView imageImportant;
        TextView textType;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageIcon = itemView.findViewById(R.id.image_icon);
            textTitle = itemView.findViewById(R.id.text_title);
            textContent = itemView.findViewById(R.id.text_content);
            textTime = itemView.findViewById(R.id.text_time);
            viewUnread = itemView.findViewById(R.id.view_unread);
            imageImportant = itemView.findViewById(R.id.image_important);
            textType = itemView.findViewById(R.id.text_type);
        }
    }

    /**
     * 点击事件接口
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }

    /**
     * 设置点击监听器
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
