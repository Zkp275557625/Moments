package com.zhoukp.photo.bean;

/**
 * time：2018/1/22 10:04
 * mail：zhoukaiping@szy.cn
 * for：
 *
 * @author zhoukp
 */

public class SortModel extends Contact {

    /**
     * 是否被选中
     */
    public boolean isSelected = false;

    /**
     * 是否显示
     */
    public boolean isShow = true;

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public SortModel(String name, String headUrl, String sortKey) {
        super(name, headUrl, sortKey);
    }

    /**
     * 显示数据拼音的首字母
     */
    public String sortLetters;

    public SortToken sortToken = new SortToken();
}
