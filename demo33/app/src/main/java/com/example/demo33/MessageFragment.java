package com.example.demo33;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MessageFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> messageList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    // 模拟消息数据
    private final String[] sampleMessages = {
            "系统更新：版本 2.0.1 已发布",
            "用户 Alice：你好！最近怎么样？",
            "通知：您的订单已发货",
            "提醒：下午3点有会议",
            "安全提示：请及时修改密码",
            "好友请求：Bob 想添加您为好友",
            "系统公告：服务器维护通知",
            "活动通知：新用户注册有奖",
            "消息：张三回复了您的评论",
            "提醒：别忘了今晚的聚餐",
            "系统：备份已完成",
            "用户李四：文件已收到，谢谢",
            "通知：您的订阅即将到期",
            "系统：成功同步到云端"
    };

    public MessageFragment() {
        // 必需的空构造函数
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        initViews(view);
        setupMessageList();
        setupListeners();

        return view;
    }

    private void initViews(View view) {
        listView = view.findViewById(R.id.listView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        progressBar = view.findViewById(R.id.progressBar);

        // 初始化下拉刷新
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
    }

    private void setupMessageList() {
        messageList = new ArrayList<>();
        // 初始加载一些消息
        loadInitialMessages();

        adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                messageList
        );
        listView.setAdapter(adapter);

        // 设置列表项点击事件
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String message = messageList.get(position);
            Toast.makeText(getActivity(),
                    "点击了：" + message.split("：")[0], Toast.LENGTH_SHORT).show();
        });

        // 长按删除消息
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            String removedMessage = messageList.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(getActivity(),
                    "已删除：" + removedMessage.split("：")[0], Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void setupListeners() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 模拟加载新数据
                loadNewMessages();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void loadInitialMessages() {
        progressBar.setVisibility(View.VISIBLE);

        // 模拟网络延迟
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                messageList.clear();
                // 添加前5条消息
                for (int i = 0; i < Math.min(5, sampleMessages.length); i++) {
                    messageList.add(sampleMessages[i]);
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
        }, 1000);
    }

    private void loadNewMessages() {
        if (messageList.size() >= sampleMessages.length) {
            Toast.makeText(getActivity(), "没有更多消息了", Toast.LENGTH_SHORT).show();
            return;
        }

        Random random = new Random();
        // 随机添加1-3条新消息
        int newCount = random.nextInt(3) + 1;
        int added = 0;

        for (int i = 0; i < newCount && messageList.size() < sampleMessages.length; i++) {
            // 查找还没有添加的消息
            for (String message : sampleMessages) {
                if (!messageList.contains(message)) {
                    messageList.add(0, message); // 添加到顶部
                    added++;
                    break;
                }
            }
        }

        adapter.notifyDataSetChanged();
        Toast.makeText(getActivity(),
                "加载了 " + added + " 条新消息", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 清理资源
        listView = null;
        adapter = null;
        messageList = null;
    }
}