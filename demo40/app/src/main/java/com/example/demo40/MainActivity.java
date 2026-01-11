package com.example.demo40;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // UI 组件
    private Button btnSpinnerProgress;
    private Button btnHorizontalProgress;
    private Button btnCancelableProgress;
    private Button btnModernAlternative;
    private Button btnWithButtons;

    // ProgressDialog 实例
    private ProgressDialog spinnerDialog;
    private ProgressDialog horizontalDialog;
    private ProgressDialog cancelableDialog;

    // 状态标志
    private boolean isTaskCanceled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        initViews();

        // 设置点击监听器
        setupListeners();
    }

    private void initViews() {
        btnSpinnerProgress = findViewById(R.id.btn_spinner_progress);
        btnHorizontalProgress = findViewById(R.id.btn_horizontal_progress);
        btnCancelableProgress = findViewById(R.id.btn_cancelable_progress);
        btnModernAlternative = findViewById(R.id.btn_modern_alternative);
        btnWithButtons = findViewById(R.id.btn_with_buttons);
    }

    private void setupListeners() {
        // 1. 圆形进度条示例
        btnSpinnerProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSpinnerProgressDialog();
            }
        });

        // 2. 水平进度条示例
        btnHorizontalProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHorizontalProgressDialog();
            }
        });

        // 3. 可取消的进度条示例
        btnCancelableProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelableProgressDialog();
            }
        });

        // 4. 现代替代方案示例
        btnModernAlternative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showModernProgressAlternative();
            }
        });

        // 5. 带按钮的进度条示例
        btnWithButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressWithButtons();
            }
        });
    }

    // ==================== 1. 圆形进度条示例 ====================
    private void showSpinnerProgressDialog() {
        spinnerDialog = new ProgressDialog(MainActivity.this);
        spinnerDialog.setTitle("数据加载");
        spinnerDialog.setMessage("正在加载数据，请稍候...");
        spinnerDialog.setCancelable(false);
        spinnerDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        spinnerDialog.show();

        // 使用 AsyncTask 模拟耗时操作
        new SpinnerAsyncTask().execute();
    }

    private class SpinnerAsyncTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 可以在任务开始前执行一些初始化操作
        }

        @Override
        protected String doInBackground(Void... params) {
            // 模拟耗时操作
            try {
                for (int i = 0; i <= 100; i++) {
                    if (isCancelled()) {
                        break;
                    }
                    Thread.sleep(30);
                    publishProgress(i);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return "操作中断";
            }
            return "数据加载完成";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (spinnerDialog != null && spinnerDialog.isShowing()) {
                spinnerDialog.setMessage("正在加载数据: " + values[0] + "%");
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (spinnerDialog != null && spinnerDialog.isShowing()) {
                spinnerDialog.dismiss();
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (spinnerDialog != null && spinnerDialog.isShowing()) {
                spinnerDialog.dismiss();
            }
        }
    }

    // ==================== 2. 水平进度条示例 ====================
    private void showHorizontalProgressDialog() {
        horizontalDialog = new ProgressDialog(this);
        horizontalDialog.setTitle("文件下载");
        horizontalDialog.setMessage("正在下载文件...");
        horizontalDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        horizontalDialog.setCancelable(false);
        horizontalDialog.setMax(100);
        horizontalDialog.setProgress(0);
        horizontalDialog.show();

        // 使用线程模拟下载任务
        new Thread(new HorizontalProgressTask()).start();
    }

    private class HorizontalProgressTask implements Runnable {
        @Override
        public void run() {
            try {
                for (int i = 0; i <= 100; i++) {
                    Thread.sleep(100);
                    final int progress = i;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (horizontalDialog != null && horizontalDialog.isShowing()) {
                                horizontalDialog.setProgress(progress);
                                horizontalDialog.setMessage("下载进度: " + progress + "%");

                                if (progress == 100) {
                                    horizontalDialog.setMessage("下载完成！");
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (horizontalDialog != null && horizontalDialog.isShowing()) {
                                                horizontalDialog.dismiss();
                                                Toast.makeText(MainActivity.this,
                                                        "文件下载完成", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }, 1000);
                                }
                            }
                        }
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // ==================== 3. 可取消的进度条示例 ====================
    private void showCancelableProgressDialog() {
        isTaskCanceled = false;
        cancelableDialog = new ProgressDialog(this);
        cancelableDialog.setTitle("数据处理");
        cancelableDialog.setMessage("正在处理数据，请稍候...");
        cancelableDialog.setCancelable(true);
        cancelableDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // 设置取消监听器
        cancelableDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isTaskCanceled = true;
                Toast.makeText(MainActivity.this, "操作已取消", Toast.LENGTH_SHORT).show();
            }
        });

        cancelableDialog.show();

        // 模拟长时间运行的任务
        new Thread(new CancelableTask()).start();
    }

    private class CancelableTask implements Runnable {
        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    if (isTaskCanceled) {
                        break;
                    }

                    Thread.sleep(1000);

                    final int currentStep = i + 1;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (cancelableDialog != null && cancelableDialog.isShowing()) {
                                cancelableDialog.setMessage("正在处理第 " + currentStep + " 步/共 10 步");
                            }
                        }
                    });
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (cancelableDialog != null && cancelableDialog.isShowing()) {
                            if (!isTaskCanceled) {
                                Toast.makeText(MainActivity.this, "数据处理完成",
                                        Toast.LENGTH_SHORT).show();
                            }
                            cancelableDialog.dismiss();
                        }
                    }
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // ==================== 4. 现代替代方案示例 ====================
    private void showModernProgressAlternative() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("现代进度指示器");
        builder.setMessage("这是 ProgressDialog 的替代方案");

        // 创建并添加 ProgressBar
        ProgressBar progressBar = new ProgressBar(this);
        builder.setView(progressBar);

        builder.setCancelable(false);

        // 添加按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "操作取消", Toast.LENGTH_SHORT).show();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();

        // 模拟 3 秒后自动关闭
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "操作完成", Toast.LENGTH_SHORT).show();
                }
            }
        }, 3000);
    }

    // ==================== 5. 带按钮的进度条示例 ====================
    private void showProgressWithButtons() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("确认操作");
        dialog.setMessage("您确定要执行此操作吗？");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);

        // ⚠️ 这里不要写逻辑，避免自动 dismiss
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", (d, w) -> {});
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                (d, w) -> {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "操作已取消", Toast.LENGTH_SHORT).show();
                });

        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "稍后提醒",
                (d, w) -> {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "稍后提醒设置", Toast.LENGTH_SHORT).show();
                });

        dialog.setOnShowListener(d -> {

            Button btnPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

            btnPositive.setOnClickListener(v -> {
                Toast.makeText(MainActivity.this, "确定按钮点击", Toast.LENGTH_SHORT).show();

                // ✅ 这里 Dialog 不会被系统关闭
                dialog.setMessage("正在执行操作...");
                btnPositive.setEnabled(false);
                btnPositive.setText("处理中...");

                // 模拟耗时任务（16 秒）
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "操作执行完成",
                            Toast.LENGTH_SHORT).show();
                }, 16000);
            });
        });

        dialog.show();
    }


    // ==================== 生命周期管理 ====================
    @Override
    protected void onPause() {
        super.onPause();
        // 在 Activity 暂停时关闭所有对话框
        dismissAllDialogs();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在 Activity 销毁时关闭所有对话框
        dismissAllDialogs();
    }

    private void dismissAllDialogs() {
        if (spinnerDialog != null && spinnerDialog.isShowing()) {
            spinnerDialog.dismiss();
        }
        if (horizontalDialog != null && horizontalDialog.isShowing()) {
            horizontalDialog.dismiss();
        }
        if (cancelableDialog != null && cancelableDialog.isShowing()) {
            cancelableDialog.dismiss();
        }
    }
}