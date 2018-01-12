package com.zhoukp.moments.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.zhoukp.moments.R;

/**
 * 作者：zhoukp
 * 时间：2018/1/5 9:54
 * 邮箱：zhoukaiping@szy.cn
 * 作用：图片item viewHolder
 */

public class ADViewHolder extends BaseViewHolder {

    public Button btn_install;
    private ImageView iv_image_icon;

    public ADViewHolder(View itemView, Context context) {
        super(itemView, context);
        btn_install = (Button) itemView.findViewById(R.id.btn_install);
        iv_image_icon = (ImageView) itemView.findViewById(R.id.iv_image_icon);
    }
}
