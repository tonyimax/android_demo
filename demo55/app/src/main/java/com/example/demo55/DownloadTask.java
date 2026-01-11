package com.example.demo55;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class DownloadTask extends AsyncTask<String, Integer, Integer> {

    // 下载状态常量
    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_FAILED = 1;
    public static final int STATUS_CANCELLED = 2;

    private Context context;
    private Dialog downloadDialog;
    private ProgressBar progressBar;
    private TextView tvProgress, tvSpeed, tvFileSize, tvFileName, tvDownloaded;
    private Button btnCancel;

    private String downloadUrl;
    private String fileName;
    private String savePath;
    private File outputFile;

    private long fileSize = 0;
    private long downloadedSize = 0;
    private long startTime = 0;
    private boolean isCanceled = false;

    private DownloadListener downloadListener;

    public interface DownloadListener {
        void onDownloadStart();
        void onProgressUpdate(int progress, long downloaded, long total, int speed);
        void onDownloadComplete(String filePath);
        void onDownloadFailed(String error);
        void onDownloadCancelled();
    }

    public DownloadTask(Context context, DownloadListener listener) {
        this.context = context;
        this.downloadListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (downloadListener != null) {
            downloadListener.onDownloadStart();
        }

        showDownloadDialog();
    }

    @Override
    protected Integer doInBackground(String... params) {
        if (params.length == 0) {
            return STATUS_FAILED;
        }

        downloadUrl = params[0];

        // 从URL中提取文件名
        fileName = getFileNameFromUrl(downloadUrl);
        if (fileName == null || fileName.isEmpty()) {
            fileName = "download_" + System.currentTimeMillis() + ".tmp";
        }

        // 创建保存目录
        savePath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

        File directory = new File(savePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        outputFile = new File(savePath, fileName);

        try {
            // 创建URL连接
            URL url = new URL(downloadUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setRequestProperty("Accept-Encoding", "identity");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                return STATUS_FAILED;
            }

            // 获取文件大小
            fileSize = connection.getContentLength();

            // 如果文件已存在，先删除
            if (outputFile.exists()) {
                outputFile.delete();
            }

            // 开始下载
            InputStream inputStream = connection.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(outputFile);

            byte[] buffer = new byte[8192];
            int bytesRead;
            downloadedSize = 0;
            startTime = System.currentTimeMillis();
            long lastUpdateTime = startTime;
            long lastDownloadedSize = 0;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                if (isCanceled) {
                    inputStream.close();
                    outputStream.close();
                    if (outputFile.exists()) {
                        outputFile.delete();
                    }
                    return STATUS_CANCELLED;
                }

                outputStream.write(buffer, 0, bytesRead);
                downloadedSize += bytesRead;

                // 计算下载速度（每秒更新一次）
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastUpdateTime >= 1000) {
                    long timeDiff = currentTime - lastUpdateTime;
                    long sizeDiff = downloadedSize - lastDownloadedSize;
                    int speed = (int) ((sizeDiff * 1000.0) / (timeDiff * 1024.0)); // KB/s

                    // 计算进度
                    int progress = 0;
                    if (fileSize > 0) {
                        progress = (int) ((downloadedSize * 100) / fileSize);
                    }

                    publishProgress(progress, speed);

                    lastUpdateTime = currentTime;
                    lastDownloadedSize = downloadedSize;
                }
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
            connection.disconnect();

            return STATUS_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            // 下载失败，删除文件
            if (outputFile.exists()) {
                outputFile.delete();
            }
            return STATUS_FAILED;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        if (downloadDialog != null && downloadDialog.isShowing() && !isCanceled) {
            int progress = values[0];
            int speed = values[1];

            // 更新UI
            progressBar.setProgress(progress);
            tvProgress.setText(progress + "%");
            tvSpeed.setText(formatSpeed(speed));

            if (fileSize > 0) {
                tvFileSize.setText("文件大小: " + formatFileSize(fileSize));
                tvDownloaded.setText("已下载: " + formatFileSize(downloadedSize) +
                        " / " + formatFileSize(fileSize));
            }

            if (downloadListener != null) {
                downloadListener.onProgressUpdate(progress, downloadedSize, fileSize, speed);
            }
        }
    }

    @Override
    protected void onPostExecute(Integer status) {
        super.onPostExecute(status);

        if (downloadDialog != null && downloadDialog.isShowing()) {
            downloadDialog.dismiss();
        }

        switch (status) {
            case STATUS_SUCCESS:
                if (downloadListener != null) {
                    downloadListener.onDownloadComplete(outputFile.getAbsolutePath());
                }
                break;

            case STATUS_FAILED:
                if (downloadListener != null) {
                    downloadListener.onDownloadFailed("下载失败");
                }
                break;

            case STATUS_CANCELLED:
                if (downloadListener != null) {
                    downloadListener.onDownloadCancelled();
                }
                break;
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        isCanceled = true;

        if (downloadDialog != null && downloadDialog.isShowing()) {
            downloadDialog.dismiss();
        }

        if (downloadListener != null) {
            downloadListener.onDownloadCancelled();
        }
    }

    private void showDownloadDialog() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                downloadDialog = new Dialog(context);
                downloadDialog.setContentView(R.layout.dialog_download);
                downloadDialog.setCancelable(false);

                // 初始化视图
                progressBar = downloadDialog.findViewById(R.id.progressBar);
                tvProgress = downloadDialog.findViewById(R.id.tvProgress);
                tvSpeed = downloadDialog.findViewById(R.id.tvSpeed);
                tvFileSize = downloadDialog.findViewById(R.id.tvFileSize);
                tvFileName = downloadDialog.findViewById(R.id.tvFileName);
                tvDownloaded = downloadDialog.findViewById(R.id.tvDownloaded);
                btnCancel = downloadDialog.findViewById(R.id.btnCancel);

                // 设置文件名
                tvFileName.setText(fileName);

                // 设置取消按钮
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isCanceled = true;
                        cancel(true);
                    }
                });

                downloadDialog.show();
            }
        });
    }

    private String getFileNameFromUrl(String url) {
        try {
            if (url.contains("/")) {
                String[] parts = url.split("/");
                if (parts.length > 0) {
                    String lastPart = parts[parts.length - 1];
                    // 移除查询参数
                    if (lastPart.contains("?")) {
                        lastPart = lastPart.substring(0, lastPart.indexOf("?"));
                    }
                    return lastPart;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String formatFileSize(long size) {
        if (size <= 0) return "0 B";

        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups))
                + " " + units[digitGroups];
    }

    private String formatSpeed(int speed) {
        if (speed < 1024) {
            return speed + " KB/s";
        } else {
            double mbSpeed = speed / 1024.0;
            return String.format("%.1f MB/s", mbSpeed);
        }
    }

    public String getDownloadedFilePath() {
        return outputFile != null ? outputFile.getAbsolutePath() : null;
    }
}
