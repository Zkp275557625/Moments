package com.zhoukp.photo.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhoukp.photo.R;

/**
 * 作者： KaiPingZhou
 * 时间：2017/10/28 23:41
 * 邮箱：275557625@qq.com
 * 作用：
 */
public class CommomDialog extends Dialog implements View.OnClickListener {

    private EditText etContent;
    private Button btnCancle;
    private Button btnSumbit;

    private Context mContext;
    private OnCloseListener listener;


    public CommomDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public CommomDialog(Context context, int themeResId, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_commom);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        etContent = (EditText) findViewById(R.id.etContent);
        btnCancle = (Button) findViewById(R.id.btnCancle);
        btnCancle.setOnClickListener(this);
        btnSumbit = (Button) findViewById(R.id.btnSumbit);
        btnSumbit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancle:
                if (listener != null) {
                    listener.onClick(this, false, "");
                }
                this.dismiss();
                break;
            case R.id.btnSumbit:
                if (etContent.getText().toString().length() == 0) {
                    //还没有输入文字
                    Toast.makeText(getContext(), "标签不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (listener != null) {
                        listener.onClick(this, true, etContent.getText().toString());
                    }
                }
                break;
            default:
                break;
        }
    }

    public interface OnCloseListener {
        void onClick(Dialog dialog, boolean confirm, String data);
    }
}
