package com.example.demo39;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvSelectedDate, tvSelectedTime;
    private Button btnSelectDate, btnSelectTime, btnSelectDateTime;
    private Calendar calendar;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        initViews();

        // 初始化日历实例
        calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        selectedMinute = calendar.get(Calendar.MINUTE);

        // 设置按钮点击监听器
        setupClickListeners();
    }

    private void initViews() {
        tvSelectedDate = findViewById(R.id.tv_selected_date);
        tvSelectedTime = findViewById(R.id.tv_selected_time);
        btnSelectDate = findViewById(R.id.btn_select_date);
        btnSelectTime = findViewById(R.id.btn_select_time);
        btnSelectDateTime = findViewById(R.id.btn_select_datetime);
    }

    private void setupClickListeners() {
        // 选择日期按钮点击事件
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // 选择时间按钮点击事件
        btnSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        // 选择日期和时间按钮点击事件
        btnSelectDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });
    }

    // 显示日期选择对话框
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // 保存选择的日期
                        selectedYear = year;
                        selectedMonth = month;
                        selectedDay = dayOfMonth;

                        // 更新显示
                        String selectedDate = String.format(Locale.getDefault(),
                                "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        tvSelectedDate.setText("选择的日期: " + selectedDate);

                        Toast.makeText(MainActivity.this,
                                "已选择日期: " + selectedDate, Toast.LENGTH_SHORT).show();
                    }
                },
                selectedYear, selectedMonth, selectedDay
        );

        // 可选：设置日期范围限制
        Calendar minDate = Calendar.getInstance();
        minDate.add(Calendar.DAY_OF_MONTH, -7); // 7天前
        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());

        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.MONTH, 3); // 3个月后
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

        datePickerDialog.setTitle("选择日期");
        datePickerDialog.show();
    }

    // 显示时间选择对话框
    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // 保存选择的时间
                        selectedHour = hourOfDay;
                        selectedMinute = minute;

                        // 更新显示
                        String amPm = (hourOfDay < 12) ? "AM" : "PM";
                        int displayHour = (hourOfDay == 0) ? 12 : (hourOfDay > 12) ? hourOfDay - 12 : hourOfDay;
                        String selectedTime = String.format(Locale.getDefault(),
                                "%02d:%02d %s", displayHour, minute, amPm);
                        tvSelectedTime.setText("选择的时间: " + selectedTime);

                        Toast.makeText(MainActivity.this,
                                "已选择时间: " + selectedTime, Toast.LENGTH_SHORT).show();
                    }
                },
                selectedHour, selectedMinute, false // false表示24小时制，true表示12小时制
        );

        timePickerDialog.setTitle("选择时间");
        timePickerDialog.show();
    }

    // 同时选择日期和时间（先日期后时间）
    private void showDateTimePicker() {
        // 先显示日期选择器
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedYear = year;
                        selectedMonth = month;
                        selectedDay = dayOfMonth;

                        // 日期选择后显示时间选择器
                        showTimePickerAfterDate(year, month, dayOfMonth);
                    }
                },
                selectedYear, selectedMonth, selectedDay
        );

        datePickerDialog.setTitle("选择日期和时间");
        datePickerDialog.show();
    }

    private void showTimePickerAfterDate(final int year, final int month, final int day) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectedHour = hourOfDay;
                        selectedMinute = minute;

                        // 显示完整的日期时间
                        String selectedDateTime = String.format(Locale.getDefault(),
                                "%04d-%02d-%02d %02d:%02d",
                                year, month + 1, day, hourOfDay, minute);

                        tvSelectedDate.setText("日期: " + String.format("%04d-%02d-%02d", year, month + 1, day));
                        tvSelectedTime.setText("时间: " + String.format("%02d:%02d", hourOfDay, minute));

                        Toast.makeText(MainActivity.this,
                                "已选择: " + selectedDateTime, Toast.LENGTH_LONG).show();
                    }
                },
                selectedHour, selectedMinute, true
        );

        timePickerDialog.setTitle("选择时间");
        timePickerDialog.show();
    }
}