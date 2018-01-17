package com.zhoukp.photo.bean;

import java.util.ArrayList;

/**
 * auther：zhoukp
 * time：2018/1/15 16:50
 * mail：zhoukaiping@szy.cn
 * for：
 */

public class ClassInfo {

    private String groupName;
    private boolean isSelected;
    private boolean isMatched;
    private ArrayList<ClassesInfo> children;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public ArrayList<ClassesInfo> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<ClassesInfo> children) {
        this.children = children;
    }
}
