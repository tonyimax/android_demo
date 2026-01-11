package com.example.demo45;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notification> notificationList;
    private List<Notification> filteredList;
    private View emptyView;
    private MaterialButton btnMarkAllRead, btnClearAll;
    private ChipGroup chipGroup;
    private Chip chipAll, chipUnread, chipImportant;

    private static final int FILTER_ALL = 0;
    private static final int FILTER_UNREAD = 1;
    private static final int FILTER_IMPORTANT = 2;
    private int currentFilter = FILTER_ALL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        initViews(view);
        initData();
        setupRecyclerView();
        setupChipGroup();
        setupButtons();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_notifications);
        emptyView = view.findViewById(R.id.text_empty_view);
        btnMarkAllRead = view.findViewById(R.id.btn_mark_all_read);
        btnClearAll = view.findViewById(R.id.btn_clear_all);
        chipGroup = view.findViewById(R.id.chip_group);
        chipAll = view.findViewById(R.id.chip_all);
        chipUnread = view.findViewById(R.id.chip_unread);
        chipImportant = view.findViewById(R.id.chip_important);
    }

    private void initData() {
        notificationList = new ArrayList<>();

        // 添加示例通知
        notificationList.add(new Notification(
                "系统更新",
                "发现新版本v2.5.0，点击立即更新",
                "刚刚",
                Notification.TYPE_SYSTEM,
                true,
                true
        ));

        notificationList.add(new Notification(
                "安全提醒",
                "您的账号在异地登录，如非本人操作请立即修改密码",
                "10分钟前",
                Notification.TYPE_SECURITY,
                true,
                true
        ));

        notificationList.add(new Notification(
                "订单通知",
                "您的订单已发货，运单号：SF123456789",
                "1小时前",
                Notification.TYPE_ORDER,
                false,
                false
        ));

        notificationList.add(new Notification(
                "活动提醒",
                "周末大促活动即将开始，不要错过哦！",
                "3小时前",
                Notification.TYPE_PROMOTION,
                false,
                false
        ));

        notificationList.add(new Notification(
                "好友请求",
                "张三请求添加您为好友",
                "昨天",
                Notification.TYPE_SOCIAL,
                true,
                false
        ));

        filteredList = new ArrayList<>(notificationList);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NotificationAdapter(filteredList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new NotificationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Notification notification = filteredList.get(position);
                notification.setRead(true);
                adapter.notifyItemChanged(position);

                Toast.makeText(getContext(),
                        "打开通知: " + notification.getTitle(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(int position) {
                showDeleteDialog(position);
            }
        });

        updateEmptyView();
    }

    private void setupChipGroup() {
        chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                if (checkedIds.isEmpty()) return;

                int checkedId = checkedIds.get(0);
                if (checkedId == R.id.chip_all) {
                    currentFilter = FILTER_ALL;
                } else if (checkedId == R.id.chip_unread) {
                    currentFilter = FILTER_UNREAD;
                } else if (checkedId == R.id.chip_important) {
                    currentFilter = FILTER_IMPORTANT;
                }

                applyFilter();
            }
        });
    }

    private void setupButtons() {
        btnMarkAllRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markAllAsRead();
                Toast.makeText(getContext(), "所有通知已标记为已读", Toast.LENGTH_SHORT).show();
            }
        });

        btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllNotifications();
            }
        });
    }

    private void applyFilter() {
        filteredList.clear();

        for (Notification notification : notificationList) {
            switch (currentFilter) {
                case FILTER_ALL:
                    filteredList.add(notification);
                    break;
                case FILTER_UNREAD:
                    if (!notification.isRead()) {
                        filteredList.add(notification);
                    }
                    break;
                case FILTER_IMPORTANT:
                    if (notification.isImportant()) {
                        filteredList.add(notification);
                    }
                    break;
            }
        }

        adapter.notifyDataSetChanged();
        updateEmptyView();
    }

    private void markAllAsRead() {
        for (Notification notification : notificationList) {
            notification.setRead(true);
        }
        applyFilter();
    }

    private void clearAllNotifications() {
        Snackbar.make(getView(), "确定要清空所有通知吗？", Snackbar.LENGTH_LONG)
                .setAction("清空", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notificationList.clear();
                        filteredList.clear();
                        adapter.notifyDataSetChanged();
                        updateEmptyView();
                        Toast.makeText(getContext(), "已清空所有通知", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void showDeleteDialog(int position) {
        Notification notification = filteredList.get(position);
        Snackbar.make(getView(), "删除这条通知？", Snackbar.LENGTH_LONG)
                .setAction("删除", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 从原始列表也删除
                        notificationList.remove(notification);
                        filteredList.remove(position);
                        adapter.notifyItemRemoved(position);
                        updateEmptyView();
                    }
                })
                .show();
    }

    private void updateEmptyView() {
        if (filteredList.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 通知数据模型
     */
    public static class Notification {
        public static final int TYPE_SYSTEM = 1;
        public static final int TYPE_SECURITY = 2;
        public static final int TYPE_ORDER = 3;
        public static final int TYPE_PROMOTION = 4;
        public static final int TYPE_SOCIAL = 5;

        private String title;
        private String content;
        private String time;
        private int type;
        private boolean read;
        private boolean important;

        public Notification(String title, String content, String time,
                            int type, boolean read, boolean important) {
            this.title = title;
            this.content = content;
            this.time = time;
            this.type = type;
            this.read = read;
            this.important = important;
        }

        // Getter 和 Setter
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }

        public String getTime() { return time; }
        public void setTime(String time) { this.time = time; }

        public int getType() { return type; }
        public void setType(int type) { this.type = type; }

        public boolean isRead() { return read; }
        public void setRead(boolean read) { this.read = read; }

        public boolean isImportant() { return important; }
        public void setImportant(boolean important) { this.important = important; }
    }
}
