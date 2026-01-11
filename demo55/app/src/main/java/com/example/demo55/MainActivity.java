package com.example.demo55;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;

    private EditText etUrl;
    private Button btnDownload;
    private TextView tvStatus;
    private TextView tvFilePath;

    private DownloadTask downloadTask;
    private boolean isDownloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupClickListeners();

        // 自动填充示例URL
        etUrl.setText("https://cdn.plyr.io/static/demo/View_From_A_Blue_Moon_Trailer-576p.mp4");
    }

    private void initViews() {
        etUrl = findViewById(R.id.etUrl);
        btnDownload = findViewById(R.id.btnDownload);
        tvStatus = findViewById(R.id.tvStatus);
        tvFilePath = findViewById(R.id.tvFilePath);
    }

    private void setupClickListeners() {
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDownloading) {
                    cancelDownload();
                } else {
                    startDownload();
                }
            }
        });
    }

    private void startDownload() {
        String url = etUrl.getText().toString().trim();

        if (url.isEmpty()) {
            Toast.makeText(this, "请输入下载URL", Toast.LENGTH_SHORT).show();
            return;
        }

        // 检查权限
        if (checkPermissions()) {
            executeDownload(url);
        } else {
            requestPermissions();
        }
    }

    private void executeDownload(String url) {
        isDownloading = true;
        btnDownload.setText("取消下载");
        tvStatus.setText("下载状态：正在下载...");

        downloadTask = new DownloadTask(this, new DownloadTask.DownloadListener() {
            @Override
            public void onDownloadStart() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvStatus.setText("下载状态：正在连接服务器...");
                    }
                });
            }

            @Override
            public void onProgressUpdate(int progress, long downloaded, long total, int speed) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String status = String.format("下载中: %d%% (速度: %s)",
                                progress, formatSpeed(speed));
                        tvStatus.setText("下载状态：" + status);
                    }
                });
            }

            @Override
            public void onDownloadComplete(String filePath) {
                isDownloading = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnDownload.setText("开始下载");
                        tvStatus.setText("下载状态：下载完成");
                        tvFilePath.setText("文件路径：" + filePath);

                        Toast.makeText(MainActivity.this,
                                "文件下载完成: " + filePath, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onDownloadFailed(String error) {
                isDownloading = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnDownload.setText("开始下载");
                        tvStatus.setText("下载状态：下载失败");

                        Toast.makeText(MainActivity.this,
                                "下载失败: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onDownloadCancelled() {
                isDownloading = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnDownload.setText("开始下载");
                        tvStatus.setText("下载状态：已取消");

                        Toast.makeText(MainActivity.this,
                                "下载已取消", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        downloadTask.execute(url);
    }

    private void cancelDownload() {
        if (downloadTask != null && !downloadTask.isCancelled()) {
            downloadTask.cancel(true);
            downloadTask = null;
        }
        isDownloading = false;
        btnDownload.setText("开始下载");
    }

    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int writePermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int readPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            return writePermission == PackageManager.PERMISSION_GRANTED &&
                    readPermission == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                },
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String url = etUrl.getText().toString().trim();
                executeDownload(url);
            } else {
                Toast.makeText(this, "需要存储权限才能下载文件",
                        Toast.LENGTH_SHORT).show();
                isDownloading = false;
                btnDownload.setText("开始下载");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downloadTask != null && !downloadTask.isCancelled()) {
            downloadTask.cancel(true);
        }
    }

    private String formatSpeed(int speed) {
        if (speed < 1024) {
            return speed + " KB/s";
        } else {
            double mbSpeed = speed / 1024.0;
            return String.format("%.1f MB/s", mbSpeed);
        }
    }
}