package com.zhoukp.moments.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.zhoukp.moments.R;

/**
 * 创建人：周开平
 * 创建时间：2017/4/27 20:40
 * 作用：圆角imageview
 */

public class RoundRectImageView extends ImageView {
    private int ImageViewStyle = 2;
    public Paint paint;

    public RoundRectImageView(Context context) {
        this(context, null);

    }

    public RoundRectImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundRectImageView);
        ImageViewStyle = ta.getInt(R.styleable.RoundRectImageView_ImageViewStyle, 2);
    }


    public RoundRectImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable(); //获取ImageView的图片资源
        if (drawable != null) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();  //drawable转换成Bitmap类型
            Bitmap b = getRoundBitmap(bitmap, 32);   //获取圆角矩形图片的方法
            final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
            final Rect rectDest = new Rect(0, 0, getWidth(), getHeight());
            paint.reset();
            canvas.drawBitmap(b, rectSrc, rectDest, paint);
        } else {
            super.onDraw(canvas);
        }
    }

    private Bitmap getRoundBitmap(Bitmap bitmap, int roundPx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);  //抗锯齿设置
        canvas.drawARGB(0, 0, 0, 0);  //将会以颜色ARBG填充整个控件的Canvas背景
        paint.setColor(color);
        int x = bitmap.getWidth();

        if (ImageViewStyle == 1) {
            canvas.drawCircle(x / 2, x / 2, x / 2, paint);
        } else {
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        }
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}

