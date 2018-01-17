package com.zhoukp.photo.activity.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.zhoukp.photo.R;
import com.zhoukp.photo.bean.MusicInfo;

import java.util.ArrayList;

/**
 * auther：zhoukp
 * time：2018/1/15 16:43
 * mail：zhoukaiping@szy.cn
 * for：音乐列表数据适配器
 *
 * @author zhoukp
 */

public class MusicListAdapter extends BaseExpandableListAdapter {

    protected Context context;
    protected ArrayList<MusicInfo> groups;

    public MusicListAdapter(Context context, ArrayList<MusicInfo> groups) {
        this.context = context;
        this.groups = groups;
    }

    /**
     * 返回group数量
     *
     * @return int
     */
    @Override
    public int getGroupCount() {
        return groups.size();
    }

    /**
     * 返回对应group的孩子的数量
     *
     * @param groupPosition group的index
     * @return 孩子数量
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).getChildren().size();
    }

    /**
     * 返回某个group的内容
     *
     * @param groupPosition index
     * @return group
     */
    @Override
    public MusicInfo getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    /**
     * 返回某个group中某个child
     *
     * @param groupPosition group index
     * @param childPosition child index
     * @return child
     */
    @Override
    public String getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).getChildren().get(childPosition);
    }

    /**
     * 返回当前group的index
     *
     * @param groupPosition group index
     * @return index
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * 返回某个group中某个child的index
     *
     * @param groupPosition group index
     * @param childPosition child index
     * @return index
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.expandablelistview_group_item, null);
            viewHolder = new GroupViewHolder();
            viewHolder.tvGroupName = (TextView) convertView.findViewById(R.id.tvGroupName);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }

        viewHolder.tvGroupName.setText(getGroup(groupPosition).getGroupName());
        viewHolder.tvGroupName.setSelected(isExpanded);

        return convertView;
    }

    public class GroupViewHolder {
        TextView tvGroupName;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.expandablelistview_child_item, null);
            viewHolder = new ChildViewHolder();
            viewHolder.tvChildName = (TextView) convertView.findViewById(R.id.tvChildName);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }

        viewHolder.tvChildName.setText(getChild(groupPosition, childPosition));

        return convertView;
    }

    public class ChildViewHolder {
        public TextView tvChildName;
    }

    /**
     * child可被选中
     *
     * @param groupPosition group index
     * @param childPosition child index
     * @return true-可被选中  false-不可被选中
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
