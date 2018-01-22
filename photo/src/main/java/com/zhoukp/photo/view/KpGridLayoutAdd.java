package com.zhoukp.photo.view;

import android.animation.LayoutTransition;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zhoukp.photo.utils.CacheUtils;
import com.zhoukp.photo.utils.LogUtil;
import com.zhoukp.photo.R;

import java.util.ArrayList;

/**
 * 作者：zhoukp
 * 时间：2018/1/10 15:23
 * 邮箱：zhoukaiping@szy.cn
 * 作用：
 *
 * @author zhoukp
 */

public class KpGridLayoutAdd extends FlowLayout {

    private ArrayList<String> selected;
    private ArrayList<String> labelName;

    private int marginTop;
    private int marginLeft;
    private int padding;
    private int textSize;

    private Context context;
    private AttributeSet attrs;

    private TextView textView;

    public KpGridLayoutAdd(Context context) {
        this(context, null);
    }

    public KpGridLayoutAdd(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KpGridLayoutAdd(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        this.attrs = attrs;

        //初始化当前view
        initView();

        initData();
    }

    /**
     * 初始化当前View
     */
    private void initView() {
        this.setLayoutTransition(new LayoutTransition());
    }

    private void initData() {
        this.padding = 12;
        this.marginTop = 30;
        this.marginLeft = 40;
        this.textSize = 12;

        selected = new ArrayList<>();
        labelName = new ArrayList<>();

        textView = newAddItemView();
        textView.setText(" + 新建标签");
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出对话框让用户输入标签名
                new CommomDialog(context, R.style.dialog, new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm, String data) {
                        if (confirm) {
                            //点击了确定按钮
                            addItem(data);
                            dialog.dismiss();
                        }
                    }
                }).show();
            }
        });
        this.addView(textView);
    }

    /**
     * 添加新建的item到本控件中
     *
     * @param item 文字
     */
    private void addItem(String item) {
        Label label = newItemView();
        label.tvText.setText(item);
        labelName.add(item);
        CacheUtils.putArrayListStr(context, "gridLayoutAdd_labelName", labelName);
        this.addView(label, 0);
    }

    public void addItems(String[] splitAddName) {
        for (int i = 0; i < splitAddName.length; i++) {
            addItem(splitAddName[i]);
        }
    }

    /**
     * 创建一个新的item
     *
     * @return TextView
     */
    private Label newItemView() {
        Label label = new Label(getContext());
        label.tvText.setOnClickListener(onClickListener);
        label.tvText.setPadding(2 * padding, padding, 2 * padding, padding);
        label.ivDelete.setOnClickListener(onDeleteListener);
        return label;
    }

    /**
     * 创建一个添加标签的item
     *
     * @return TextView
     */
    private TextView newAddItemView() {
        //向GridLayout中添加栏目
        TextView textView = new TextView(getContext());
        LayoutParams params = new LayoutParams(context, attrs);
        params.width = LayoutParams.WRAP_CONTENT;
        params.height = LayoutParams.WRAP_CONTENT;
        params.setMargins(marginLeft, marginTop, 0, 0);
        textView.setLayoutParams(params);
        textView.setPadding(2 * padding, padding, 2 * padding, padding);
        textView.setGravity(Gravity.CENTER);

        textView.setBackgroundResource(R.drawable.add_label_bg);
        textView.setTextSize(textSize);
        textView.setTextColor(getResources().getColor(R.color.colorBlack));

        return textView;
    }

    /**
     * 对item的点击监听
     */
    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView textView = (TextView) view;
            Label label;
            int childCount = KpGridLayoutAdd.this.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                label = (Label) KpGridLayoutAdd.this.getChildAt(i);
                if (textView == label.tvText && !label.tvText.isSelected()) {
                    label.tvText.setSelected(true);
                    if (!selected.contains(i + "")) {
                        selected.add(i + "");
                        CacheUtils.putArrayList(context, "gridLayoutAdd", selected);
                    }
                } else if (textView == label.tvText && label.tvText.isSelected()) {
                    label.tvText.setSelected(false);
                    selected.remove(i);
                    CacheUtils.putArrayList(context, "gridLayoutAdd", selected);
                }
            }
        }
    };

    private OnClickListener onDeleteListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            LogUtil.e("删除");
            int childCount = KpGridLayoutAdd.this.getChildCount();
            Label label;
            for (int i = 0; i < childCount; i++) {
                label = (Label) KpGridLayoutAdd.this.getChildAt(i);
                if (label.ivDelete == view) {
                    KpGridLayoutAdd.this.removeViewAt(i);
                    labelName.remove(i);

                    if (selected.contains(i)) {
                        selected.remove(i);
                    }
                    for (int j = 0; j < selected.size(); j++) {
                        if (Integer.parseInt(selected.get(j)) >= i) {
                            LogUtil.e(selected.get(j) + "");
                            selected.set(j, Integer.parseInt(selected.get(j)) - 1 + "");
                            LogUtil.e(selected.get(j) + "");
                        }
                    }
                    CacheUtils.putArrayList(context, "gridLayoutAdd", selected);
                    CacheUtils.putArrayListStr(context, "gridLayoutAdd_labelName", labelName);
                    break;
                }
            }
        }
    };

    /**
     * 显示/不显示 删除按钮
     *
     * @param canDelete 是否处于可删除item状态
     */
    public void unDelete(boolean canDelete) {
        int childCount = KpGridLayoutAdd.this.getChildCount();
        LogUtil.e("childCount==" + childCount);
        Label label;
        if (canDelete) {
            for (int i = 0; i < childCount - 1; i++) {
                label = (Label) KpGridLayoutAdd.this.getChildAt(i);
                label.ivDelete.setVisibility(GONE);
                label.tvText.setEnabled(true);
            }
        } else {
            for (int i = 0; i < childCount - 1; i++) {
                label = (Label) KpGridLayoutAdd.this.getChildAt(i);
                label.ivDelete.setVisibility(VISIBLE);
                label.tvText.setEnabled(false);
            }
        }
    }

    /**
     * 显示/不显示 选中状态
     *
     * @param showSelected true-显示  false-不显示
     */
    public void showSelected(boolean showSelected) {
        if (showSelected) {
            //显示选中状态
            ArrayList<Integer> gridLayoutAdd = CacheUtils.getArrayList(context, "gridLayoutAdd");
            int childCount = KpGridLayoutAdd.this.getChildCount();
            Label label;
            for (int i = 0; i < childCount - 1; i++) {
                if (gridLayoutAdd.contains(i)) {
                    label = (Label) KpGridLayoutAdd.this.getChildAt(i);
                    label.tvText.setSelected(true);
                }
            }
        } else {
            //不显示选中状态
            int childCount = KpGridLayoutAdd.this.getChildCount();
            Label label;
            for (int i = 0; i < childCount - 1; i++) {
                label = (Label) KpGridLayoutAdd.this.getChildAt(i);
                label.tvText.setSelected(false);
            }
        }
    }

    /**
     * 显示/不显示 添加标签按钮
     *
     * @param hideAddItemLabel true-不显示 false-显示
     */
    public void hideAddItemLabel(boolean hideAddItemLabel) {
        if (hideAddItemLabel) {
            addView(textView, getChildCount());
        } else {
            removeView(textView);
        }
    }

    /**
     * 设置已选的item状态
     *
     * @param split index数组
     */
    public void setSelectes(String[] split) {
        for (int i = 0; i < split.length; i++) {
            if (!TextUtils.isEmpty(split[i])) {
                ((Label) getChildAt(Integer.parseInt(split[i]))).tvText.setSelected(true);
                selected.add(split[i]);
            }
        }
    }
}
