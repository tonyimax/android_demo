package com.example.demo14;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<GroupItem> groupList;

    public ExpandableListAdapter(Context context, List<GroupItem> groupList) {
        this.context = context;
        this.groupList = groupList;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_item, null);

            holder = new GroupViewHolder();
            holder.ivCustomIndicator = convertView.findViewById(R.id.ivCustomIndicator);
            holder.ivGroupIcon = convertView.findViewById(R.id.ivGroupIcon);
            holder.tvGroupName = convertView.findViewById(R.id.tvGroupName);
            holder.tvGroupInfo = convertView.findViewById(R.id.tvGroupInfo);
            holder.tvBadge = convertView.findViewById(R.id.tvBadge);

            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }

        GroupItem group = (GroupItem) getGroup(groupPosition);

        // 设置数据
        holder.ivGroupIcon.setImageResource(group.getIconResId());
        holder.tvGroupName.setText(group.getGroupName());
        holder.tvGroupInfo.setText(group.getChildrenCount() + "个成员");

        // 设置指示器动画
        if (isExpanded) {
            holder.ivCustomIndicator.setImageResource(R.drawable.ic_arrow_down);
            holder.ivCustomIndicator.animate().rotation(0).setDuration(200).start();
        } else {
            holder.ivCustomIndicator.setImageResource(R.drawable.ic_arrow_right);
            holder.ivCustomIndicator.animate().rotation(0).setDuration(200).start();
        }

        // 设置徽章
        if (group.isOnline()) {
            holder.tvBadge.setText("在线");
            holder.tvBadge.setBackgroundResource(R.drawable.badge_background);
        } else {
            holder.tvBadge.setText("离线");
            holder.tvBadge.setBackgroundColor(ContextCompat.getColor(context, R.color.warning));
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_item, null);

            holder = new ChildViewHolder();
            holder.civAvatar = convertView.findViewById(R.id.civAvatar);
            holder.tvChildName = convertView.findViewById(R.id.tvChildName);
            holder.tvChildAge = convertView.findViewById(R.id.tvChildAge);
            holder.tvChildPosition = convertView.findViewById(R.id.tvChildPosition);
            holder.viewStatus = convertView.findViewById(R.id.viewStatus);

            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }

        ChildItem child = (ChildItem) getChild(groupPosition, childPosition);

        // 设置数据
        holder.civAvatar.setImageResource(child.getAvatarResId());
        holder.tvChildName.setText(child.getName());
        holder.tvChildAge.setText(child.getAge() + "岁");
        holder.tvChildPosition.setText(child.getPosition());

        // 设置在线状态
        if (child.isOnline()) {
            holder.viewStatus.setBackgroundResource(R.drawable.status_online);
        } else {
            holder.viewStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.outline));
        }

        return convertView;
    }

    // 其他必要的方法保持不变...
    @Override public int getGroupCount() { return groupList.size(); }
    @Override public int getChildrenCount(int groupPosition) { return groupList.get(groupPosition).getChildrenCount(); }
    @Override public Object getGroup(int groupPosition) { return groupList.get(groupPosition); }
    @Override public Object getChild(int groupPosition, int childPosition) { return groupList.get(groupPosition).getChildren().get(childPosition); }
    @Override public long getGroupId(int groupPosition) { return groupPosition; }
    @Override public long getChildId(int groupPosition, int childPosition) { return childPosition; }
    @Override public boolean hasStableIds() { return false; }
    @Override public boolean isChildSelectable(int groupPosition, int childPosition) { return true; }

    // ViewHolder 类
    private static class GroupViewHolder {
        ImageView ivCustomIndicator;
        ImageView ivGroupIcon;
        TextView tvGroupName;
        TextView tvGroupInfo;
        TextView tvBadge;
    }

    private static class ChildViewHolder {
        CircleImageView civAvatar;
        TextView tvChildName;
        TextView tvChildAge;
        TextView tvChildPosition;
        View viewStatus;
    }
}