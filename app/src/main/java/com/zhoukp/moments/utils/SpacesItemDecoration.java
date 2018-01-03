package com.zhoukp.moments.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 作者：zhoukp
 * 时间：2017/12/29 15:13
 * 邮箱：zhoukaiping@szy.cn
 * 作用：recyclerview的item的间距
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space * 2;

        if (parent.getChildPosition(view) == 0) {
            outRect.top = space;
        }
    }
}
