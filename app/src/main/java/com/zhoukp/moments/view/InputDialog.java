package com.zhoukp.moments.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.zhoukp.moments.R;

/**
 * 作者：zhoukp
 * 时间：2018/1/9 17:00
 * 邮箱：zhoukaiping@szy.cn
 * 作用：输入框
 */

public class InputDialog extends Dialog {

    public KPLinearLayout ll_comment;
    private Context context;

    public InputDialog(Context context) {
        super(context, R.style.DialogStyleBottom);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        //1.弹出输入法框
        InputMethodManager manager = (InputMethodManager)
                context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        ll_comment = (KPLinearLayout) findViewById(R.id.ll_comment);
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
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下
                if (!inRangeOfView(ll_comment, ev)) {
                    //如果点击事件不在控件中,则隐藏自身
                    InputDialog.this.dismiss();
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
