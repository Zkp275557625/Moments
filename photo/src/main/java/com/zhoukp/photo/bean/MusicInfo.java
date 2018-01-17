package com.zhoukp.photo.bean;

import java.util.ArrayList;

/**
 * auther：zhoukp
 * time：2018/1/15 16:50
 * mail：zhoukaiping@szy.cn
 * for：
 */

public class MusicInfo {

    private String groupName;
    private ArrayList<String> children;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ArrayList<String> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<String> children) {
        this.children = children;
    }
}
