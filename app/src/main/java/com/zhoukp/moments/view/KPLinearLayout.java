package com.zhoukp.moments.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zhoukp.moments.R;

/**
 * 作者：zhoukp
 * 时间：2018/1/3 10:10
 * 邮箱：zhoukaiping@szy.cn
 * 作用：自定义布局实现有文字的时候更换button的背景图片
 */

public class KPLinearLayout extends LinearLayout {

    public EditText et_content;
    public Button btn_submit;

    /**
     * 当输入框状态改变时，会调用相应的方法
     */
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(et_content.getText().toString())) {
                //设置button可点击
                btn_submit.setEnabled(true);
            } else {
                //设置button不可点击
                btn_submit.setEnabled(false);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        //在文字改变后调用
        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 0) {
                //没有文本，设置button背景为默认状态
                changeBtnBacgroud(true);
            } else {
                //有文本，设置button背景为selector状态
                changeBtnBacgroud(false);
            }
        }
    };

    /**
     * 更换button背景图片
     *
     * @param isDefault 是否为默认状态
     */
    private void changeBtnBacgroud(boolean isDefault) {
        if (isDefault) {
            btn_submit.setBackgroundResource(R.drawable.btn_bg_grey);
        } else {
            btn_submit.setBackgroundResource(R.drawable.btn_bg_selector);
        }
    }

    public KPLinearLayout(Context context) {
        this(context, null);
    }

    public KPLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KPLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.kpcomment, this, true);

        //初始化视图
        initViews();
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        et_content = (EditText) findViewById(R.id.et_content);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        //设置editText的文字变化监听
        et_content.addTextChangedListener(textWatcher);
    }
}
