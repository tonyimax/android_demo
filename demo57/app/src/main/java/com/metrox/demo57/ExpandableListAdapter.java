package com.metrox.demo57;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<GroupItem> groupItems;
    private HashMap<GroupItem, List<ChildItem>> childItems;

    public ExpandableListAdapter(Context context, List<GroupItem> groupItems,
                                 HashMap<GroupItem, List<ChildItem>> childItems) {
        this.context = context;
        this.groupItems = groupItems;
        this.childItems = childItems;
    }

    @Override
    public int getGroupCount() {
        return groupItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childItems.get(groupItems.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupItems.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childItems.get(groupItems.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GroupItem groupItem = (GroupItem) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
        }

        TextView groupNameTextView = convertView.findViewById(R.id.group_name);
        ImageView indicatorImageView = convertView.findViewById(R.id.group_indicator);

        groupNameTextView.setTypeface(null, Typeface.BOLD);
        groupNameTextView.setText(groupItem.getGroupName());

        // 根据展开状态改变指示器图标
        if (isExpanded) {
            indicatorImageView.setImageResource(R.drawable.ic_expand_less);
        } else {
            indicatorImageView.setImageResource(R.drawable.ic_expand_more);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ChildItem childItem = (ChildItem) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView childNameTextView = convertView.findViewById(R.id.child_name);
        TextView childDescTextView = convertView.findViewById(R.id.child_description);

        childNameTextView.setText(childItem.getName());
        childDescTextView.setText(childItem.getDescription());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
