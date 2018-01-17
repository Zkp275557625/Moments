package com.zhoukp.photo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * 作者：zhoukp
 * 时间：2018/1/2 9:59
 * 邮箱：zhoukaiping@szy.cn
 * 作用：带灰色蒙版的ImageView
 * @author zhoukp
 */

public class CustomImageView extends ImageView {
    private String url;
    private Bitmap bitmap;
    private boolean isAttachedToWindow;
    protected Context context;

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public CustomImageView(Context context) {
        this(context, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Drawable drawable = getDrawable();
                if (drawable != null) {
                    drawable.mutate().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Drawable drawableUp = getDrawable();
                if (drawableUp != null) {
                    drawableUp.mutate().clearColorFilter();
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onAttachedToWindow() {
//        isAttachedToWindow = true;
        setImageBitmap(bitmap);
//        setImageUrl(url);
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        //取消图片加载请求
//        Picasso.with(getContext()).cancelRequest(this);
        //清除Glide所有图片加载请求
        //Glide.clear(this);
//        isAttachedToWindow = false;
//        setImageBitmap(null);
//        setImageUrl(null);
        super.onDetachedFromWindow();
    }


    public void setImageUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            this.url = url;
            if (isAttachedToWindow) {
                //使用Glide加载图片
                Picasso.with(getContext()).load(url).placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))).into(this);
            }
        }
    }
}
