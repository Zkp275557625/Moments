package com.zhoukp.moments.adapter.viewholder;

import android.content.Context;
import android.view.View;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.zhoukp.moments.R;
import com.zhoukp.moments.view.NineGridlayout;

/**
 * 作者：zhoukp
 * 时间：2018/1/5 9:54
 * 邮箱：zhoukaiping@szy.cn
 * 作用：图片item viewHolder
 */

public class ImageViewHolder extends BaseViewHolder {

    //九宫格图片
//    public NineGridlayout imgs;
    public SubsamplingScaleImageView img;

    public ImageViewHolder(View itemView, Context context) {
        super(itemView, context);
//        imgs = (NineGridlayout) itemView.findViewById(R.id.imgs);
        img = (SubsamplingScaleImageView) itemView.findViewById(R.id.img);
    }
}
