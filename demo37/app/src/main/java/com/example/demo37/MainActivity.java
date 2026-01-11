package com.example.demo37;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private Button btnSimpleDialog, btnSingleChoice, btnMultiChoice, btnCustomList, btnCustomView;
    private int selectedColorIndex = 0; // 保存单选对话框的选择
    private boolean[] selectedLanguages = new boolean[]{false, false, false, false}; // 保存多选对话框的选择

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        btnSimpleDialog = findViewById(R.id.btn_simple_dialog);
        btnSingleChoice = findViewById(R.id.btn_single_choice);
        btnMultiChoice = findViewById(R.id.btn_multi_choice);
        btnCustomList = findViewById(R.id.btn_custom_list);
        btnCustomView = findViewById(R.id.btn_custom_view);
    }

    private void setupClickListeners() {
        // 1. 简单对话框
        btnSimpleDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSimpleDialog();
            }
        });

        // 2. 单选列表对话框
        btnSingleChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleChoiceDialog();
            }
        });

        // 3. 多选列表对话框
        btnMultiChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMultiChoiceDialog();
            }
        });

        // 4. 自定义列表对话框
        btnCustomList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomListDialog();
            }
        });

        // 5. 自定义视图对话框
        btnCustomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomViewDialog();
            }
        });
    }

    // ========== 1. 简单对话框 ==========
    private void showSimpleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("这是一个简单的对话框示例")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "点击了确定", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "点击了取消", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("更多", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "点击了更多", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setCancelable(false) // 点击对话框外部是否关闭
                .show();
    }

    // ========== 2. 单选列表对话框 ==========
    private void showSingleChoiceDialog() {
        final String[] colors = {"红色", "绿色", "蓝色", "黄色", "紫色"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择你喜欢的颜色")
                .setSingleChoiceItems(colors, selectedColorIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedColorIndex = which;
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this,
                                "选择了: " + colors[selectedColorIndex],
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    // ========== 3. 多选列表对话框 ==========
    private void showMultiChoiceDialog() {
        final String[] languages = {"Java", "Kotlin", "Python", "JavaScript"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择你会的编程语言")
                .setMultiChoiceItems(languages, selectedLanguages, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        selectedLanguages[which] = isChecked;
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuilder selected = new StringBuilder("选择了: ");
                        for (int i = 0; i < selectedLanguages.length; i++) {
                            if (selectedLanguages[i]) {
                                selected.append(languages[i]).append(" ");
                            }
                        }
                        Toast.makeText(MainActivity.this, selected.toString(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    // ========== 4. 自定义列表对话框 ==========
    private void showCustomListDialog() {
        // 创建自定义数据
        ArrayList<Person> personList = new ArrayList<>();
        personList.add(new Person("张三", "Android开发工程师", R.drawable.m3));
        personList.add(new Person("李四", "iOS开发工程师", R.drawable.m6));
        personList.add(new Person("王五", "后端开发工程师", R.drawable.m4));
        personList.add(new Person("赵六", "前端开发工程师", R.drawable.m5));

        // 创建自定义Adapter
        PersonAdapter adapter = new PersonAdapter(this, personList);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择联系人")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Person selectedPerson = personList.get(which);
                        Toast.makeText(MainActivity.this,
                                "选择了: " + selectedPerson.getName(),
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    // ========== 5. 自定义视图对话框 ==========
    private void showCustomViewDialog() {
        // 加载自定义布局
        LayoutInflater inflater = LayoutInflater.from(this);
        View customView = inflater.inflate(R.layout.dialog_custom_view, null);

        final EditText etUsername = customView.findViewById(R.id.et_username);
        final EditText etPassword = customView.findViewById(R.id.et_password);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("用户登录")
                .setView(customView)
                .setPositiveButton("登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String username = etUsername.getText().toString().trim();
                        String password = etPassword.getText().toString().trim();

                        if (!username.isEmpty() && !password.isEmpty()) {
                            Toast.makeText(MainActivity.this,
                                    "用户名: " + username + "\n密码: " + password,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    // 自定义数据类
    class Person {
        private String name;
        private String job;
        private int iconResId;

        public Person(String name, String job, int iconResId) {
            this.name = name;
            this.job = job;
            this.iconResId = iconResId;
        }

        public String getName() { return name; }
        public String getJob() { return job; }
        public int getIconResId() { return iconResId; }
    }
}