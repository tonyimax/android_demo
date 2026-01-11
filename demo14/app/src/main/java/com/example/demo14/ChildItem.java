package com.example.demo14;

public class ChildItem {
    private String name;
    private int age;
    private String position;
    private boolean isOnline;
    private int avatarResId;

    public ChildItem(String name, int age, String position, boolean isOnline, int avatarResId) {
        this.name = name;
        this.age = age;
        this.position = position;
        this.isOnline = isOnline;
        this.avatarResId = avatarResId;
    }

    // Getter 方法
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getPosition() { return position; }
    public boolean isOnline() { return isOnline; }
    public int getAvatarResId() { return avatarResId; }
}