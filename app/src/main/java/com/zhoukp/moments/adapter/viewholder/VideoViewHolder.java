package com.zhoukp.moments.adapter.viewholder;

import android.content.Context;
import android.view.View;

import com.zhoukp.moments.R;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * 作者：zhoukp
 * 时间：2018/1/5 9:56
 * 邮箱：zhoukaiping@szy.cn
 * 作用：视频item viewHolder
 * 使用节操播放器
 */

public class VideoViewHolder extends BaseViewHolder {
    //节操播放器
    public JCVideoPlayerStandard jc_videoplayer;

    public VideoViewHolder(View itemView, Context context) {
        super(itemView, context);
        jc_videoplayer = (JCVideoPlayerStandard) itemView.findViewById(R.id.jc_videoplayer);
    }
}
