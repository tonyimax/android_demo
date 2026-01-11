package com.example.demo15;

import androidx.annotation.NonNull;

public class Fruit {
    private String name;
    private int imageResId;

    public Fruit(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public String getName() { return name; }
    public int getImageResId() { return imageResId; }

    @NonNull
    @Override
    public String toString() {
        return name; // 这个决定了Spinner显示的内容
    }
}