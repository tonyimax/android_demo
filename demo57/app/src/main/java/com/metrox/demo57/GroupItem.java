package com.metrox.demo57;

import java.util.List;

public class GroupItem {
    private String groupName;
    private List<ChildItem> children;

    public GroupItem(String groupName, List<ChildItem> children) {
        this.groupName = groupName;
        this.children = children;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<ChildItem> getChildren() {
        return children;
    }

    public void setChildren(List<ChildItem> children) {
        this.children = children;
    }
}
