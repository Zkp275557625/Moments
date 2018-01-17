package com.zhoukp.photo.activity.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhoukp.photo.R;
import com.zhoukp.photo.bean.ClassInfo;
import com.zhoukp.photo.bean.ClassesInfo;

import java.util.ArrayList;

/**
 * auther：zhoukp
 * time：2018/1/15 16:43
 * mail：zhoukaiping@szy.cn
 * for：班级列表数据适配器
 *
 * @author zhoukp
 */

public class ClassListAdapter extends BaseExpandableListAdapter {

    protected Activity context;
    private ArrayList<ClassInfo> groups;
    private onGroupSelectedListener listener;

    public ClassListAdapter(Activity context, ArrayList<ClassInfo> groups) {
        this.context = context;
        this.groups = groups;
    }

    private ArrayList<ClassInfo> getShowGroups(ArrayList<ClassInfo> groups) {
        ArrayList<ClassInfo> result = new ArrayList<>();
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).isMatched()) {
                result.add(groups.get(i));
            } else {
                ClassInfo classInfo = new ClassInfo();
                classInfo.setGroupName(groups.get(i).getGroupName());
                classInfo.setSelected(groups.get(i).isSelected());
                classInfo.setMatched(groups.get(i).isMatched());
                ArrayList<ClassesInfo> children = new ArrayList<>();
                for (int j = 0; j < groups.get(i).getChildren().size(); j++) {
                    if (groups.get(i).getChildren().get(j).isMatched()) {
                        children.add(groups.get(i).getChildren().get(j));
                    }
                }
                if (children.size() > 0) {
                    classInfo.setChildren(children);
                    result.add(classInfo);
                }
            }
        }
        return result;
    }

    public void setListener(onGroupSelectedListener listener) {
        this.listener = listener;
    }

    /**
     * 返回group数量
     *
     * @return int
     */
    @Override
    public int getGroupCount() {
        return getShowGroups(groups).size();
    }

    /**
     * 返回对应group的孩子的数量
     *
     * @param groupPosition group的index
     * @return 孩子数量
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return getShowGroups(groups).get(groupPosition).getChildren().size();
    }

    /**
     * 返回某个group的内容
     *
     * @param groupPosition index
     * @return group
     */
    @Override
    public ClassInfo getGroup(int groupPosition) {
        return getShowGroups(groups).get(groupPosition);
    }

    /**
     * 返回某个group中某个child的内容
     *
     * @param groupPosition group index
     * @param childPosition child index
     * @return child
     */
    @Override
    public ClassesInfo getChild(int groupPosition, int childPosition) {
        return getShowGroups(groups).get(groupPosition).getChildren().get(childPosition);
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
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, final ViewGroup parent) {
        final GroupViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.expandablelistview_group_item_class, null);
            viewHolder = new GroupViewHolder();
            viewHolder.tvGroupName = (TextView) convertView.findViewById(R.id.tvGroupName);
            viewHolder.ivSelected = (ImageView) convertView.findViewById(R.id.ivSelected);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }
        viewHolder.tvGroupName.setText(getGroup(groupPosition).getGroupName());
        viewHolder.tvGroupName.setSelected(isExpanded);
        viewHolder.ivSelected.setSelected(getGroup(groupPosition).isSelected());

        viewHolder.ivSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGroupSelectedListener(groupPosition);
            }
        });
        return convertView;
    }

    public interface onGroupSelectedListener {
        void onGroupSelectedListener(int groupPosition);
    }

    private class GroupViewHolder {
        TextView tvGroupName;
        ImageView ivSelected;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.expandablelistview_child_item_class, null);
            viewHolder = new ChildViewHolder();
            viewHolder.tvChildName = (TextView) convertView.findViewById(R.id.tvChildName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }

        viewHolder.tvChildName.setText(getChild(groupPosition, childPosition).getName());
        viewHolder.tvChildName.setSelected(getChild(groupPosition, childPosition).isSelected());

        return convertView;
    }

    private class ChildViewHolder {
        TextView tvChildName;
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
