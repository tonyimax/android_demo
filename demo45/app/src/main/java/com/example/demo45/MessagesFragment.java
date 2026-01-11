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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private View emptyView;
    private FloatingActionButton fabNewMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        // 初始化视图
        recyclerView = view.findViewById(R.id.recycler_view_messages);
        emptyView = view.findViewById(R.id.text_empty_view);
        fabNewMessage = view.findViewById(R.id.fab_new_message);

        // 设置RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // 初始化数据
        initMessages();

        // 设置适配器
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);

        // 设置空视图
        updateEmptyView();

        // 设置FAB点击事件
        fabNewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "创建新消息", Toast.LENGTH_SHORT).show();
                // 这里可以添加创建新消息的逻辑
                Snackbar.make(v, "新消息功能开发中...", Snackbar.LENGTH_SHORT).show();
            }
        });

        // 设置消息项点击事件
        messageAdapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Message message = messageList.get(position);
                Toast.makeText(getContext(),
                        "打开与" + message.getSender() + "的对话",
                        Toast.LENGTH_SHORT).show();

                // 标记为已读
                message.setUnread(false);
                messageAdapter.notifyItemChanged(position);
            }

            @Override
            public void onItemLongClick(int position) {
                Message message = messageList.get(position);
                Snackbar.make(recyclerView,
                                "删除与" + message.getSender() + "的对话？",
                                Snackbar.LENGTH_LONG)
                        .setAction("删除", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                messageList.remove(position);
                                messageAdapter.notifyItemRemoved(position);
                                updateEmptyView();
                            }
                        })
                        .show();
            }
        });

        return view;
    }

    /**
     * 初始化消息数据
     */
    private void initMessages() {
        messageList = new ArrayList<>();

        // 添加一些示例消息
        messageList.add(new Message("张三", "你好，今天下午的会议安排在3点钟。", "10:30", true, R.drawable.m1));
        messageList.add(new Message("李四", "项目报告已经完成，请查收。", "09:15", true, R.drawable.m2));
        messageList.add(new Message("王五", "周末一起打篮球吗？", "昨天", false, R.drawable.m3));
        messageList.add(new Message("赵六", "Android开发学习资料已发送", "昨天", false, R.drawable.m4));
        messageList.add(new Message("系统通知", "您的账号已在其他设备登录", "2023-12-01", false, R.drawable.m8));
        messageList.add(new Message("孙七", "设计稿已经更新到最新版本", "2023-11-30", false, R.drawable.m5));
        messageList.add(new Message("周八", "咖啡厅见面讨论一下新需求", "2023-11-29", false, R.drawable.m6));
        messageList.add(new Message("吴九", "面试安排在下周二上午10点", "2023-11-28", false, R.drawable.m7));
    }

    /**
     * 更新空视图显示
     */
    private void updateEmptyView() {
        if (messageList.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 消息数据模型类
     */
    public static class Message {
        private String sender;
        private String message;
        private String time;
        private boolean unread;
        private int avatarResId;

        public Message(String sender, String message, String time, boolean unread, int avatarResId) {
            this.sender = sender;
            this.message = message;
            this.time = time;
            this.unread = unread;
            this.avatarResId = avatarResId;
        }

        // Getter 和 Setter 方法
        public String getSender() { return sender; }
        public void setSender(String sender) { this.sender = sender; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public String getTime() { return time; }
        public void setTime(String time) { this.time = time; }

        public boolean isUnread() { return unread; }
        public void setUnread(boolean unread) { this.unread = unread; }

        public int getAvatarResId() { return avatarResId; }
        public void setAvatarResId(int avatarResId) { this.avatarResId = avatarResId; }
    }
}