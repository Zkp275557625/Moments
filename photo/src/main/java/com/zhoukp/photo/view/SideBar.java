package com.zhoukp.photo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.zhoukp.photo.R;
import com.zhoukp.photo.utils.LogUtil;

/**
 * time：2018/1/22 8:49
 * mail：zhoukaiping@szy.cn
 * for：联系人列表索引
 *
 * @author zhoukp
 */

public class SideBar extends View {

    /**
     * 26个字母
     */
    public static String[] alphabets = {
            "#", "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};

    /**
     * 每一个字母的高度
     */
    private float singleHeight;

    /**
     * 画笔
     */
    private Paint paint;

    /**
     * 触摸显示字母对话框的背景
     */
    public static int[] dialogColor = {
            R.drawable.dialog_color_blue,
            R.drawable.dialog_color_green,
            R.drawable.dialog_color_orange,
            R.drawable.dialog_color_purple,
            R.drawable.dialog_color_red};

    private TextView textDialog;

    /**
     * 当前选中的索引
     */
    protected int choose = -1;

    /**
     * 触摸事件
     */
    private OnTouchingLetterChangedListener listener;

    public SideBar(Context context) {
        this(context, null);
    }

    public SideBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initVariates();
    }

    private void initVariates() {
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //获取宽高
        int width = getWidth();
        int height = getHeight();

        //获取每一个字母的高度
        singleHeight = (height * 1f) / alphabets.length;
        singleHeight = (height * 1f - singleHeight / 2) / alphabets.length;

        //绘制每一个字母
        for (int i = 0; i < alphabets.length; i++) {
            paint.setColor(Color.parseColor("#00BAFF"));
            paint.setTypeface(Typeface.DEFAULT);
            paint.setAntiAlias(true);
            paint.setTextSize(40);
            //选中的状态
            if (i == choose) {
                paint.setColor(Color.parseColor("#C60000"));
                paint.setFakeBoldText(true);
            }
            //x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - paint.measureText(alphabets[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(alphabets[i], xPos, yPos, paint);
            //重置画笔
            paint.reset();
        }
    }

    /**
     * 触摸事件的处理
     *
     * @param event 事件
     * @return true
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float y = event.getY();
        int oldChoose = choose;

        //点击y坐标所占总高度的比例*alphabets数组的长度就等于点击alphabets中的索引
        int index = (int) (y / getHeight() * alphabets.length);

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                setBackgroundDrawable(new ColorDrawable(0x00000000));
                //重置选中索引
                choose = -1;
                if (textDialog != null) {
                    textDialog.setVisibility(View.GONE);
                }
                invalidate();
                break;
            default:
                setBackgroundResource(R.drawable.sidebar_background);
                if (oldChoose != index) {
                    if (index >= 0 && index < alphabets.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(alphabets[index]);
                        }
                        if (textDialog != null) {
                            textDialog.setText(alphabets[index]);
                            textDialog.setVisibility(View.VISIBLE);
                            // 动态改变文字dialog的位置
                            int right = textDialog.getLeft();
                            textDialog.setX(right / 2 * 3);
                            if (index > 24) {
                                textDialog.setY(singleHeight * 24);
                            } else {
                                textDialog.setY(singleHeight * index);
                            }
                            textDialog.setBackground(getContext().getResources().getDrawable(dialogColor[index / 6]));
                        }

                        choose = index;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.listener = onTouchingLetterChangedListener;
    }

    public void setTextDialog(TextView textDialog) {
        this.textDialog = textDialog;
    }

    /**
     * 触摸监听
     */
    public interface OnTouchingLetterChangedListener {
        /**
         * 触摸监听
         *
         * @param str 字符
         */
        void onTouchingLetterChanged(String str);
    }
}
