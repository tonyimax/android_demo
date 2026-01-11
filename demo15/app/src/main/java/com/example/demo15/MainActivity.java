package com.example.demo15;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerFruits;
    private TextView tvSelectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupSpinner();
    }

    private void initViews() {
        spinnerFruits = findViewById(R.id.spinnerFruits);
        tvSelectedItem = findViewById(R.id.tvSelectedItem);
    }

    private void setupSpinner() {
        List<Fruit> fruitList = Arrays.asList(
                new Fruit("🍎 苹果", R.drawable.ic_apple),
                new Fruit("🍌 香蕉", R.drawable.ic_banana),
                new Fruit("🍊 橙子", R.drawable.ic_orange),
                new Fruit("🍓 草莓", R.drawable.ic_strawberry),
                new Fruit("🍇 葡萄", R.drawable.ic_grape),
                new Fruit("🍉 西瓜", R.drawable.ic_watermelon)
        );

        FruitAdapter adapter = new FruitAdapter(this, fruitList);
        spinnerFruits.setAdapter(adapter);

        spinnerFruits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Fruit selectedFruit = (Fruit) parent.getItemAtPosition(position);
                if (selectedFruit != null) {
                    String message = selectedFruit.getName();
                    tvSelectedItem.setText(message);

                    // 美化Toast
                    Toast.makeText(MainActivity.this,
                            "已选择 " + message.replace(" ", ""),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tvSelectedItem.setText("暂无选择");
            }
        });
    }
}