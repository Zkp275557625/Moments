package com.zhoukp.moments.view.pop;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * 作者：zhoukp
 * 时间：2018/1/10 9:22
 * 邮箱：zhoukaiping@szy.cn
 * 作用：
 */

public class PopupWindows {

    protected Context context;
    protected PopupWindow popupWindow;
    protected WindowManager manager;
    protected View rootView;
    protected Drawable background = null;

    public PopupWindows(Context context) {
        this.context = context;
        popupWindow = new PopupWindow(context);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public void setContentView(int layoutResID) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setContentView(inflator.inflate(layoutResID, null));
    }

    public void setContentView(View root) {
        rootView = root;
        popupWindow.setContentView(root);
    }

    public void setBackgroundDrawable(Drawable background) {
        this.background = background;
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        popupWindow.setOnDismissListener(listener);
    }

    public void dismiss() {
        popupWindow.dismiss();
    }

    protected void onDismiss() {
    }


    protected void onShow() {
    }

    protected void preShow() {
        if (rootView == null) {
            throw new IllegalStateException("setContentView was not called with a view to display.");
        }

        onShow();

        if (background == null) {
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
        } else {
            popupWindow.setBackgroundDrawable(background);
        }

        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        popupWindow.setContentView(rootView);
    }
}
