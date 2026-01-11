package com.example.demo45;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class HelpFragment extends Fragment {

    private RecyclerView recyclerViewFaq;
    private FAQAdapter adapter;
    private List<FAQItem> faqList;
    private EditText editSearch;
    private LinearLayout layoutOnlineSupport, layoutPhoneSupport, layoutFeedback;
    private TextInputLayout textInputLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        initViews(view);
        initFAQData();
        setupRecyclerView();
        setupSearch();
        setupClickListeners();

        return view;
    }

    private void initViews(View view) {
        recyclerViewFaq = view.findViewById(R.id.recycler_view_faq);
        editSearch = view.findViewById(R.id.edit_search);

        // 修正：这些是 LinearLayout，不是 MaterialCardView
        layoutOnlineSupport = view.findViewById(R.id.layout_online_support);
        layoutPhoneSupport = view.findViewById(R.id.layout_phone_support);
        layoutFeedback = view.findViewById(R.id.layout_feedback);

        // 获取 TextInputLayout（如果布局中有 id）
        textInputLayout = view.findViewById(R.id.text_input_layout);
        // 如果没有设置 id，尝试通过其他方式获取
        if (textInputLayout == null && editSearch != null && editSearch.getParent() != null) {
            View parent = (View) editSearch.getParent();
            if (parent instanceof TextInputLayout) {
                textInputLayout = (TextInputLayout) parent;
            }
        }
    }

    private void initFAQData() {
        faqList = new ArrayList<>();

        faqList.add(new FAQItem(
                "如何修改个人信息？",
                "您可以点击右上角的头像进入个人中心，然后选择编辑个人信息进行修改。"
        ));

        faqList.add(new FAQItem(
                "如何重置密码？",
                "在登录页面点击'忘记密码'，按照提示操作即可重置密码。"
        ));

        faqList.add(new FAQItem(
                "消息通知不提醒怎么办？",
                "请检查手机的系统设置和应用内的通知权限设置，确保通知功能已开启。"
        ));

        faqList.add(new FAQItem(
                "如何删除聊天记录？",
                "长按需要删除的聊天记录，选择删除选项即可。您也可以在设置中清空所有聊天记录。"
        ));

        faqList.add(new FAQItem(
                "应用闪退怎么解决？",
                "请尝试清除应用缓存，或者卸载后重新安装最新版本。"
        ));

        faqList.add(new FAQItem(
                "如何联系客服？",
                "您可以拨打客服电话400-123-4567，或者通过在线客服功能联系我们。"
        ));

        faqList.add(new FAQItem(
                "支持哪些支付方式？",
                "目前支持微信支付、支付宝、银联等多种支付方式。"
        ));

        faqList.add(new FAQItem(
                "如何注销账号？",
                "请联系客服处理账号注销事宜，我们需要核实您的身份信息。"
        ));
    }

    private void setupRecyclerView() {
        if (getContext() == null) return;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewFaq.setLayoutManager(layoutManager);

        adapter = new FAQAdapter(faqList);
        recyclerViewFaq.setAdapter(adapter);

        adapter.setOnItemClickListener(new FAQAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                FAQItem item = faqList.get(position);
                showFAQDetail(item);
            }
        });
    }

    private void setupSearch() {
        if (editSearch == null) return;

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filterFAQ(s.toString());
            }
        });

        // 添加键盘搜索按钮监听
        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    filterFAQ(v.getText().toString());
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });

        // 设置清除按钮点击监听
        if (textInputLayout != null) {
            textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editSearch.setText("");
                    filterFAQ("");
                    hideKeyboard();
                }
            });
        }
    }

    private void setupClickListeners() {
        // 在线客服 - 修正为 LinearLayout
        if (layoutOnlineSupport != null) {
            layoutOnlineSupport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOnlineSupportDialog();
                }
            });
        }

        // 客服电话 - 修正为 LinearLayout
        if (layoutPhoneSupport != null) {
            layoutPhoneSupport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPhoneSupportDialog();
                }
            });
        }

        // 反馈意见 - 修正为 LinearLayout
        if (layoutFeedback != null) {
            layoutFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFeedbackDialog();
                }
            });
        }
    }

    private void filterFAQ(String query) {
        if (adapter == null) return;

        List<FAQItem> filteredList = new ArrayList<>();

        if (query == null || query.isEmpty()) {
            filteredList.addAll(faqList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (FAQItem item : faqList) {
                if (item.getQuestion().toLowerCase().contains(lowerCaseQuery) ||
                        item.getAnswer().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(item);
                }
            }
        }

        adapter.updateList(filteredList);
    }

    private void hideKeyboard() {
        if (getContext() == null || getView() == null) return;

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void showFAQDetail(FAQItem item) {
        if (getContext() == null) return;

        new MaterialAlertDialogBuilder(getContext())
                .setTitle(item.getQuestion())
                .setMessage(item.getAnswer())
                .setPositiveButton("确定", null)
                .show();
    }

    private void showOnlineSupportDialog() {
        if (getContext() == null) return;

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("在线客服")
                .setMessage("我们的在线客服工作时间为：\n\n" +
                        "周一至周五：9:00-18:00\n" +
                        "周六至周日：10:00-17:00\n\n" +
                        "是否现在联系在线客服？")
                .setPositiveButton("联系客服", (dialog, which) -> {
                    Toast.makeText(getContext(), "正在为您转接在线客服...", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void showPhoneSupportDialog() {
        if (getContext() == null) return;

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("客服电话")
                .setMessage("客服电话：400-123-4567\n\n" +
                        "服务时间：24小时\n\n" +
                        "是否立即拨打？")
                .setPositiveButton("拨打", (dialog, which) -> {
                    try {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:4001234567"));
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "无法拨打电话", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void showFeedbackDialog() {
        if (getContext() == null) return;

        // 简化的反馈对话框
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("反馈意见")
                .setMessage("您可以通过以下方式反馈意见：\n\n" +
                        "1. 在线客服\n" +
                        "2. 客服电话：400-123-4567\n" +
                        "3. 反馈邮箱：feedback@example.com\n\n" +
                        "感谢您的宝贵意见！")
                .setPositiveButton("确定", null)
                .show();
    }

    /**
     * FAQ数据模型
     */
    public static class FAQItem {
        private String question;
        private String answer;

        public FAQItem(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }

        public String getQuestion() { return question; }
        public void setQuestion(String question) { this.question = question; }

        public String getAnswer() { return answer; }
        public void setAnswer(String answer) { this.answer = answer; }
    }
}