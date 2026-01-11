package com.example.demo28;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button btnGetDate, btnGetTime, btnGetDateTime;
    private TextView tvResult;

    // 用于存储日期时间的变量
    private int selectedYear, selectedMonth, selectedDay;
    private int selectedHour, selectedMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        initViews();

        // 设置默认值
        setDefaultDateTime();

        // 设置监听器
        setupListeners();
    }

    private void initViews() {
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        btnGetDate = findViewById(R.id.btnGetDate);
        btnGetTime = findViewById(R.id.btnGetTime);
        btnGetDateTime = findViewById(R.id.btnGetDateTime);
        tvResult = findViewById(R.id.tvResult);
    }

    private void setDefaultDateTime() {
        // 获取当前日期时间
        Calendar calendar = Calendar.getInstance();

        // 设置 DatePicker 默认值
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

        datePicker.init(selectedYear, selectedMonth, selectedDay, null);

        // 设置 TimePicker 默认值（24小时制）
        selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        selectedMinute = calendar.get(Calendar.MINUTE);

        timePicker.setIs24HourView(true); // 设置为24小时制
        timePicker.setHour(selectedHour);
        timePicker.setMinute(selectedMinute);
    }

    private void setupListeners() {
        // DatePicker 监听器
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedYear = year;
                selectedMonth = monthOfYear;
                selectedDay = dayOfMonth;

                // 实时显示日期变化
                String dateStr = formatDate(year, monthOfYear, dayOfMonth);
                Toast.makeText(MainActivity.this, "日期已更改为: " + dateStr, Toast.LENGTH_SHORT).show();
            }
        });

        // TimePicker 监听器
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                selectedHour = hourOfDay;
                selectedMinute = minute;

                // 实时显示时间变化
                String timeStr = formatTime(hourOfDay, minute);
                Toast.makeText(MainActivity.this, "时间已更改为: " + timeStr, Toast.LENGTH_SHORT).show();
            }
        });

        // 获取日期按钮点击事件
        btnGetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 直接从 DatePicker 获取当前值
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();

                String dateStr = formatDate(year, month, day);
                tvResult.setText("选中日期: " + dateStr);
            }
        });

        // 获取时间按钮点击事件
        btnGetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 兼容不同 Android 版本的获取方式
                int hour, minute;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else {
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }

                String timeStr = formatTime(hour, minute);
                tvResult.setText("选中时间: " + timeStr);
            }
        });

        // 获取完整日期时间按钮点击事件
        btnGetDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取日期
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();

                // 获取时间
                int hour, minute;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else {
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }

                // 格式化为完整日期时间
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day, hour, minute);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.getDefault());
                String dateTimeStr = sdf.format(calendar.getTime());

                tvResult.setText("完整日期时间: " + dateTimeStr);
            }
        });
    }

    // 格式化日期
    private String formatDate(int year, int month, int day) {
        // 注意：month 是从 0 开始的
        return String.format(Locale.getDefault(), "%04d年%02d月%02d日", year, month + 1, day);
    }

    // 格式化时间
    private String formatTime(int hour, int minute) {
        return String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
    }

    // 实用方法：设置最小和最大日期
    private void setDatePickerLimits() {
        // 获取 Calendar 实例
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        // 设置最小日期为当前日期
        datePicker.setMinDate(minDate.getTimeInMillis());

        // 设置最大日期为当前日期加一年
        maxDate.add(Calendar.YEAR, 1);
        datePicker.setMaxDate(maxDate.getTimeInMillis());
    }

    // 实用方法：设置时间范围
    private void setTimePickerRange() {
        // 注意：原生的 TimePicker 不直接支持设置时间范围
        // 如果需要限制时间范围，可以在按钮点击时验证
    }

    // 实用方法：以对话框形式显示 DatePicker
    /*
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    // 处理选择的日期
                    String date = formatDate(year, month, dayOfMonth);
                    tvResult.setText("对话框选择日期: " + date);
                }
            },
            selectedYear, selectedMonth, selectedDay
        );
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
            this,
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    // 处理选择的时间
                    String time = formatTime(hourOfDay, minute);
                    tvResult.setText("对话框选择时间: " + time);
                }
            },
            selectedHour, selectedMinute, true // true 表示24小时制
        );
        timePickerDialog.show();
    }
    */
}