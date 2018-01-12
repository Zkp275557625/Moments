package com.zhoukp.photo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * auther：zhoukp
 * time：2018/1/12 10:38
 * mail：zhoukaiping@szy.cn
 * for：
 */

public class FlowLayout extends ViewGroup {

    /**
     * 是否水平居中显示
     */
    private boolean centerHorizontal;

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p.width, p.height);
    }

    public boolean isCenterHorizontal() {
        return centerHorizontal;
    }

    /**
     * 动态设置子view的居中状态
     *
     * @param centerHorizontal 是否居中
     */
    public void setCenterHorizontal(boolean centerHorizontal) {
        if (this.centerHorizontal ^ centerHorizontal) {
            this.centerHorizontal = centerHorizontal;
            requestLayout();
        }
    }

    /**
     * 负责设置子控件的测量模式和大小 根据所有子控件设置自己的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获得它的父容器为它设置的测量模式和大小
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        // 如果是warp_content情况下，记录宽和高
        final int paddingWidth = getPaddingLeft() + getPaddingRight();
        final int paddingHeight = getPaddingTop() + getPaddingBottom();
        int width = paddingWidth;
        int height = paddingHeight;
        // 记录每一行的宽度，width不断取最大宽度
        int lineWidth = paddingWidth;
        // 每一行的高度，累加至height
        int lineHeight = 0;
        mAllViews.clear();
        mLineHeight.clear();
        mLineWidth.clear();
        // 存储每一行所有的childView
        List<View> lineViews = new ArrayList<>();

        int cCount = getChildCount();

        // 遍历每个子元素
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            // 得到child的lp
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            // 测量每一个child的宽和高
            final int marginWidth = lp.rightMargin + lp.leftMargin;
            final int marginHeight = lp.topMargin + lp.bottomMargin;
            // 子控件测量的时候需要将父控件的padding和当前子控件的margin算进去
            child.measure(
                    getChildMeasureSpec(widthMeasureSpec, paddingWidth + marginWidth, lp.width),
                    getChildMeasureSpec(heightMeasureSpec, paddingHeight + marginHeight, lp.height)
            );
            // 当前子控件实际占据的宽度
            int childWidth = child.getMeasuredWidth() + marginWidth;
            // 当前子控件实际占据的高度
            int childHeight = child.getMeasuredHeight() + marginHeight;
            //如果加入当前child，则超出最大宽度，则得到目前最大宽度给width，累加height 然后开启新行
            if (lineWidth + childWidth > sizeWidth) {
                // 取最大的
                width = Math.max(width, lineWidth);
                // 叠加当前高度
                height += lineHeight;
                // 记录这一行所有的View、总的宽度以及最大高度
                mLineHeight.add(lineHeight);
                mLineWidth.add(lineWidth);
                // 将当前行的childView保存，然后开启新的ArrayList保存下一行的childView
                mAllViews.add(lineViews);
                lineViews = new ArrayList<>();
                // 重新开启新行，开始记录
                lineWidth = paddingWidth;
                lineHeight = 0;
            }
            // 否则累加值lineWidth,lineHeight取最大高度
            lineWidth += childWidth;
            lineHeight = Math.max(lineHeight, childHeight);
            // 如果是最后一个，则将当前记录的最大宽度和当前lineWidth做比较
            if (i == cCount - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
            lineViews.add(child);
        }
        // 记录最后一行
        mLineHeight.add(lineHeight);
        mLineWidth.add(lineWidth);
        mAllViews.add(lineViews);
        setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth : width,
                (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight : height);
    }

    /**
     * 存储所有的View，按行记录
     */
    private List<List<View>> mAllViews = new ArrayList<>();
    /**
     * 记录每一行的最大高度
     */
    private List<Integer> mLineHeight = new ArrayList<>();
    private List<Integer> mLineWidth = new ArrayList<>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();
        int width = getWidth() - paddingLeft - paddingRight;
        int lineHeight;
        List<View> lineViews;
        int left = paddingLeft;
        int top = paddingTop;
        // 得到总行数
        int lineNums = mAllViews.size();
        for (int i = 0; i < lineNums; i++) {
            // 每一行的所有的views
            lineViews = mAllViews.get(i);
            // 当前行的最大高度
            lineHeight = mLineHeight.get(i);
            if (centerHorizontal) {
                //左右两端空白空间
                int leftRightSpace = width - (mLineWidth.get(i) - paddingLeft - paddingRight);
                left += leftRightSpace / 2;
            }
            // 遍历当前行所有的View
            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                //计算childView的left,top,right,bottom
                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();
                rc = Math.min(rc, getWidth() - paddingRight - lp.rightMargin);
                bc = Math.min(bc, getHeight() - paddingBottom - lp.bottomMargin);
                //布局子控件
                child.layout(lc, tc, rc, bc);
                left += child.getMeasuredWidth() + lp.rightMargin + lp.leftMargin;
            }
            left = paddingLeft;
            top += lineHeight;
        }

    }


    public static class LayoutParams extends MarginLayoutParams {

        LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        LayoutParams(int width, int height) {
            super(width, height);
        }
    }
}
