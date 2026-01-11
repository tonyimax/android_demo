package com.example.demo27;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView selectedDateTextView;
    private Button btnToday;
    private Button btnCustomStyle;
    private LinearLayout eventsContainer;

    private Calendar selectedCalendar;
    private boolean isCustomStyle = false;

    // 模拟的事件数据
    private List<Event> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupEvents();
        setupCalendarView();
        setupButtons();
    }

    private void initViews() {
        calendarView = findViewById(R.id.calendarView);
        selectedDateTextView = findViewById(R.id.selectedDateTextView);
        btnToday = findViewById(R.id.btnToday);
        btnCustomStyle = findViewById(R.id.btnCustomStyle);
        eventsContainer = findViewById(R.id.eventsContainer);

        selectedCalendar = Calendar.getInstance();
    }

    private void setupEvents() {
        // 添加一些模拟事件
        Calendar cal = Calendar.getInstance();

        // 今天的事件
        events.add(new Event(cal.getTime(), "团队会议", "10:00 AM", Color.BLUE));

        // 明天的事件
        cal.add(Calendar.DAY_OF_MONTH, 1);
        events.add(new Event(cal.getTime(), "医生预约", "2:30 PM", Color.RED));

        // 后天的事件
        cal.add(Calendar.DAY_OF_MONTH, 1);
        events.add(new Event(cal.getTime(), "生日派对", "6:00 PM", Color.GREEN));

        // 一周后的事件
        cal.add(Calendar.DAY_OF_MONTH, 5);
        events.add(new Event(cal.getTime(), "项目截止", "5:00 PM", Color.MAGENTA));
    }

    private void setupCalendarView() {
        // 设置最小和最大日期（可选）
        Calendar minDate = Calendar.getInstance();
        minDate.add(Calendar.MONTH, -6);

        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.MONTH, 6);

        calendarView.setMinDate(minDate.getTimeInMillis());
        calendarView.setMaxDate(maxDate.getTimeInMillis());

        // 设置日期变更监听器
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year,
                                            int month, int dayOfMonth) {
                selectedCalendar.set(year, month, dayOfMonth);
                updateSelectedDateText();
                showEventsForSelectedDate();
            }
        });

        // 设置初始日期为今天
        updateSelectedDateText();
        showEventsForSelectedDate();
    }

    private void setupButtons() {
        btnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar today = Calendar.getInstance();
                calendarView.setDate(today.getTimeInMillis(), true, true);
                selectedCalendar = today;
                updateSelectedDateText();
                showEventsForSelectedDate();
                Toast.makeText(MainActivity.this, "已返回今天", Toast.LENGTH_SHORT).show();
            }
        });

        btnCustomStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCalendarStyle();
            }
        });
    }

    private void toggleCalendarStyle() {
        isCustomStyle = !isCustomStyle;

        if (isCustomStyle) {
            // 应用自定义样式
            calendarView.setBackgroundColor(Color.LTGRAY);

            // 注意：CalendarView 的自定义有限，部分样式需要通过主题设置
            // 以下是一些可以通过代码设置的属性
            calendarView.setFocusedMonthDateColor(Color.RED);
            calendarView.setSelectedWeekBackgroundColor(Color.parseColor("#330000FF"));
            calendarView.setWeekSeparatorLineColor(Color.BLUE);

            btnCustomStyle.setText("默认样式");
            Toast.makeText(this, "已应用自定义样式", Toast.LENGTH_SHORT).show();
        } else {
            // 恢复默认样式
            calendarView.setBackgroundColor(Color.WHITE);
            calendarView.setFocusedMonthDateColor(Color.BLACK);
            calendarView.setSelectedWeekBackgroundColor(0); // 使用默认
            calendarView.setWeekSeparatorLineColor(Color.GRAY);

            btnCustomStyle.setText("自定义样式");
            Toast.makeText(this, "已恢复默认样式", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSelectedDateText() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 EEEE", Locale.CHINA);
        String dateString = sdf.format(selectedCalendar.getTime());
        selectedDateTextView.setText("已选择: " + dateString);
    }

    private void showEventsForSelectedDate() {
        // 清空现有事件显示
        eventsContainer.removeAllViews();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String selectedDateStr = dateFormat.format(selectedCalendar.getTime());

        boolean hasEvents = false;

        // 显示选中日期的事件
        for (Event event : events) {
            String eventDateStr = dateFormat.format(event.getDate());
            if (eventDateStr.equals(selectedDateStr)) {
                addEventToView(event);
                hasEvents = true;
            }
        }

        if (!hasEvents) {
            TextView noEventText = new TextView(this);
            noEventText.setText("今天没有安排的事件");
            noEventText.setTextSize(16);
            noEventText.setTextColor(Color.GRAY);
            noEventText.setPadding(16, 16, 16, 16);
            eventsContainer.addView(noEventText);
        }
    }

    private void addEventToView(Event event) {
        LinearLayout eventItem = new LinearLayout(this);
        eventItem.setOrientation(LinearLayout.VERTICAL);
        eventItem.setPadding(16, 12, 16, 12);
        eventItem.setBackgroundColor(Color.WHITE);

        // 添加底部边框
        View divider = new View(this);
        divider.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 2));
        divider.setBackgroundColor(Color.LTGRAY);

        // 事件标题
        TextView titleText = new TextView(this);
        titleText.setText(event.getTitle());
        titleText.setTextSize(18);
        titleText.setTextColor(Color.BLACK);

        // 事件时间
        TextView timeText = new TextView(this);
        timeText.setText("时间: " + event.getTime());
        timeText.setTextSize(14);
        timeText.setTextColor(Color.DKGRAY);

        // 颜色指示器
        View colorIndicator = new View(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 4);
        params.setMargins(0, 8, 0, 0);
        colorIndicator.setLayoutParams(params);
        colorIndicator.setBackgroundColor(event.getColor());

        eventItem.addView(titleText);
        eventItem.addView(timeText);
        eventItem.addView(colorIndicator);

        eventsContainer.addView(eventItem);
        eventsContainer.addView(divider);

        // 添加点击事件
        eventItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,
                        "事件: " + event.getTitle(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 事件数据类
    private static class Event {
        private Date date;
        private String title;
        private String time;
        private int color;

        public Event(Date date, String title, String time, int color) {
            this.date = date;
            this.title = title;
            this.time = time;
            this.color = color;
        }

        public Date getDate() { return date; }
        public String getTitle() { return title; }
        public String getTime() { return time; }
        public int getColor() { return color; }
    }
}