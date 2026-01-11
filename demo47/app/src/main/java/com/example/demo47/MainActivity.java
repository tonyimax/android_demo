package com.example.demo47;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btn2;
    private Button btn3;
    private Button btn4;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        textView = findViewById(R.id.textView);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage("匿名内部类");
            }
        });

        btn2.setOnClickListener(new MyClickListener());

        btn3.setOnClickListener(v -> {
            showMessage("Lambda表达式");
        });

        btn4.setOnClickListener(this::onButtonClick);

        View.OnClickListener commonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage("通用监听器 - " + ((Button)v).getText());
            }
        };

        Button[] buttons = {btn1, btn2, btn3, btn4};
        for (Button btn : buttons) {
            btn.setOnClickListener(commonListener);
        }
    }

    private class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showMessage("独立监听器类");
        }
    }

    private void onButtonClick(View v) {
        showMessage("类私有方法引用");
    }

    private void showMessage(String message) {
        textView.setText(message);
        Log.d("ClickListenerDemo", message);
    }
}