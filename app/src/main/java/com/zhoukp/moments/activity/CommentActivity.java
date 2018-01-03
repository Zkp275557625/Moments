package com.zhoukp.moments.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.zhoukp.moments.R;
import com.zhoukp.moments.view.KPLinearLayout;

/**
 * 作者：zhoukp
 * 时间：2018/1/3 9:57
 * 邮箱：zhoukaiping@szy.cn
 * 作用：评论页面
 */

public class CommentActivity extends Activity {

    private KPLinearLayout ll_comment;
    private String position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        initViews();

        initData();

        initEvent();
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        ll_comment = (KPLinearLayout) findViewById(R.id.ll_comment);

        //1.弹出输入法框
        InputMethodManager manager = (InputMethodManager)
                getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        position = getIntent().getStringExtra("position");
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        //处理提交按钮的逻辑
        ll_comment.btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取文字
                String text = ll_comment.et_content.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("text", text);
                intent.putExtra("position", position);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * 判断点击的坐标点在不在view的范围中
     * 返回true-->在view的范围中，返回false-->不在view的范围中
     *
     * @param view 当前view
     * @param ev   点击事件（MotionEvent）
     * @return
     */
    private boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getX() < x || ev.getX() > (x + view.getWidth()) || ev.getY() < y || ev.getY() > (y + view.getHeight())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下
                if (!inRangeOfView(ll_comment, event)) {
                    //如果点击事件不在控件中,关闭当前页面
                    finish();
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }
}
