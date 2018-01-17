package com.zhoukp.photo.bean;

/**
 * auther：zhoukp
 * time：2018/1/17 11:47
 * mail：zhoukaiping@szy.cn
 * for：
 */

public class ClassesInfo {

    private String name;
    private boolean isSelected;
    private boolean isMatched;

    public ClassesInfo(String name, boolean isSelected, boolean isMatched) {
        this.name = name;
        this.isSelected = isSelected;
        this.isMatched = isMatched;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return name + "选中:" + isSelected;
    }
}
