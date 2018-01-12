package com.zhoukp.moments.adapter.viewholder;

import android.content.Context;
import android.view.View;

import com.zhoukp.moments.R;

import pl.droidsonroids.gif.GifImageView;

/**
 * 作者：zhoukp
 * 时间：2018/1/5 9:56
 * 邮箱：zhoukaiping@szy.cn
 * 作用：视频item viewHolder
 * Gif图片
 */

public class GifViewHolder extends BaseViewHolder {

    public GifImageView iv_image_gif;

    public GifViewHolder(View itemView, Context context) {
        super(itemView, context);
        iv_image_gif = (GifImageView) itemView.findViewById(R.id.iv_image_gif);
    }
}
