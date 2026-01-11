package com.example.demo30;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private NumberPicker dayPicker, monthPicker, yearPicker;
    private TextView tvSelectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        dayPicker = findViewById(R.id.dayPicker);
        monthPicker = findViewById(R.id.monthPicker);
        yearPicker = findViewById(R.id.yearPicker);

        setupDatePickers();
    }

    private void setupDatePickers() {
        // 设置月份选择器（使用字符串数组）
        final String[] months = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };

        monthPicker.setMinValue(0);
        monthPicker.setMaxValue(months.length - 1);
        monthPicker.setDisplayedValues(months);
        monthPicker.setValue(Calendar.getInstance().get(Calendar.MONTH));

        // 设置年份选择器
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        yearPicker.setMinValue(currentYear - 10);
        yearPicker.setMaxValue(currentYear + 10);
        yearPicker.setValue(currentYear);

        // 设置天数选择器（根据月份和年份动态更新）
        updateDayPicker();

        // 设置监听器
        NumberPicker.OnValueChangeListener dateChangeListener =
                new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        updateDayPicker();
                        updateSelectedDate();
                    }
                };

        monthPicker.setOnValueChangedListener(dateChangeListener);
        yearPicker.setOnValueChangedListener(dateChangeListener);
        dayPicker.setOnValueChangedListener(dateChangeListener);

        // 初始显示日期
        updateSelectedDate();
    }

    private void updateDayPicker() {
        int month = monthPicker.getValue(); // 0-11
        int year = yearPicker.getValue();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(maxDays);

        // 确保当前选择的天数不超过新月份的最大天数
        if (dayPicker.getValue() > maxDays) {
            dayPicker.setValue(maxDays);
        }
    }

    private void updateSelectedDate() {
        int day = dayPicker.getValue();
        int month = monthPicker.getValue();
        int year = yearPicker.getValue();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
        String formattedDate = sdf.format(calendar.getTime());

        tvSelectedDate.setText("Selected Date: " + formattedDate);
    }
}