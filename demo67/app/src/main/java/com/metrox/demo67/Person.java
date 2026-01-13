package com.metrox.demo67;

public class Person {
    private String name;
    private String email;
    private int age;
    private int avatarResId;

    public Person(String name, String email, int age, int avatarResId) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.avatarResId = avatarResId;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAvatarResId() {
        return avatarResId;
    }

    public void setAvatarResId(int avatarResId) {
        this.avatarResId = avatarResId;
    }
}
