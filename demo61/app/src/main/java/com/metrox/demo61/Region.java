package com.metrox.demo61;

import java.io.Serializable;

public class Region implements Serializable {
    private int id;
    private String name;
    private int parentId;
    private int level; // 0:省, 1:市, 2:区, 3:镇, 4:社区

    public Region() {}

    public Region(int id, String name, int parentId, int level) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.level = level;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getParentId() { return parentId; }
    public void setParentId(int parentId) { this.parentId = parentId; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    @Override
    public String toString() {
        return name;
    }
}