package com.example.demo14;

import java.util.List;
public class GroupItem {
    private String groupName;
    private List<ChildItem> children;
    private int iconResId;
    private boolean isOnline;

    public GroupItem(String groupName, List<ChildItem> children, int iconResId, boolean isOnline) {
        this.groupName = groupName;
        this.children = children;
        this.iconResId = iconResId;
        this.isOnline = isOnline;
    }

    // Getter 方法
    public String getGroupName() { return groupName; }
    public List<ChildItem> getChildren() { return children; }
    public int getChildrenCount() { return children.size(); }
    public int getIconResId() { return iconResId; }
    public boolean isOnline() { return isOnline; }
}