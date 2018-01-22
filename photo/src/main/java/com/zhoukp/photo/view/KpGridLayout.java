package com.zhoukp.photo.view;

import android.animation.LayoutTransition;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.zhoukp.photo.utils.CacheUtils;
import com.zhoukp.photo.utils.LogUtil;
import com.zhoukp.photo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：zhoukp
 * 时间：2018/1/10 15:23
 * 邮箱：zhoukaiping@szy.cn
 * 作用：
 *
 * @author zhoukp
 */

public class KpGridLayout extends GridLayout {

    /**
     * 列数
     */
    private final int columnCount = 4;
    /**
     * gridlayout中子控件间的间距
     */
    private int marginTop;
    private int marginLeft;

    private int padding;

    private int textSize;

    private ArrayList<String> selected;
    private ArrayList<String> labelName;

    public KpGridLayout(Context context) {
        this(context, null);
    }

    public KpGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KpGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //初始化当前view
        initView();

        initData();
    }

    /**
     * 初始化当前View
     */
    private void initView() {
        this.setColumnCount(columnCount);
        this.setLayoutTransition(new LayoutTransition());
    }

    private void initData() {
        this.padding = 12;
        this.marginTop = 30;
        this.marginLeft = 40;
        this.textSize = 12;

        this.selected = new ArrayList<>();
        this.labelName = new ArrayList<>();
    }

    /**
     * 传递item的名字
     *
     * @param items texView中的文字
     */
    public void setItems(ArrayList<String> items) {
        labelName = items;
        CacheUtils.putArrayListStr(getContext(), "KpGridLayout_labelName", labelName);
        for (String item : items) {
            addItem(item);
        }
    }

    /**
     * 添加新建的item到本控件中
     *
     * @param item 文字
     */
    private void addItem(String item) {
        TextView textView = newItemView();
        textView.setText(item);
        this.addView(textView);
    }

    /**
     * 创建一个新的item
     *
     * @return TextView
     */
    private TextView newItemView() {
        //向GridLayout中添加栏目
        TextView textView = new TextView(getContext());
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();

        params.width = GridLayout.LayoutParams.WRAP_CONTENT;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.setMargins(marginLeft, marginTop, 0, 0);
        textView.setLayoutParams(params);
        textView.setPadding(2 * padding, padding, 2 * padding, padding);
        textView.setGravity(Gravity.CENTER);

        textView.setBackgroundResource(R.drawable.label_bg_selector);
        textView.setTextSize(textSize);
        textView.setTextColor(getResources().getColorStateList(R.color.label_text_bg));
        textView.setOnClickListener(onClickListener);

        return textView;
    }

    /**
     * 对item的点击监听
     */
    private View.OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView textView = (TextView) view;
            //获取被点击控件的字符串
            String extra = textView.getText().toString();
            int index = 0;
            LogUtil.e(extra);

            for (int i = 0; i < getChildCount(); i++) {
                if (getChildAt(i) == textView) {
                    index = i;
                    break;
                }
            }

            if (textView.isSelected()) {
                selected.remove(index + "");
            } else {
                selected.add(index + "");
            }
            textView.setSelected(!textView.isSelected());
            CacheUtils.putArrayList(getContext(), "KpGridLayout", selected);
        }
    };

    /**
     * 设置已选的item状态
     *
     * @param split index数组
     */
    public void setSelectes(String[] split) {
        for (int i = 0; i < split.length; i++) {
            if (!TextUtils.isEmpty(split[i])) {
                getChildAt(Integer.parseInt(split[i])).setSelected(true);
                selected.add(split[i]);
            }
        }
    }
}
