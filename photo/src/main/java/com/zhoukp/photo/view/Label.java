package com.zhoukp.photo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhoukp.photo.R;

/**
 * auther：zhoukp
 * time：2018/1/12 8:33
 * mail：zhoukaiping@szy.cn
 * for：自定义标签
 */

public class Label extends RelativeLayout {

    /**
     * 上下文
     */
    protected Context context;

    public TextView tvText;
    public ImageView ivDelete;

    public Label(Context context) {
        this(context, null);
    }

    public Label(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Label(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        onCreate(context);

        initData(context);
    }

    /**
     * 加载并初始化布局
     *
     * @param context 上下文
     */
    private void onCreate(Context context) {
        LayoutInflater.from(context).inflate(R.layout.label, this, true);
        tvText = (TextView) findViewById(R.id.tvText);
        ivDelete = (ImageView) findViewById(R.id.ivDelete);
    }

    /**
     * 初始化数据
     *
     * @param context 上下文
     */
    private void initData(Context context) {
        this.context = context;
    }
}
