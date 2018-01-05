package com.zhoukp.moments.adapter.viewholder;

import android.content.Context;
import android.view.View;

import com.zhoukp.moments.R;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * 作者：zhoukp
 * 时间：2018/1/5 9:56
 * 邮箱：zhoukaiping@szy.cn
 * 作用：视频item viewHolder
 * 使用节操播放器
 */

public class VideoViewHolder extends BaseViewHolder {
    //    public VideoView videoview;
//    public ImageView iv_video_img, iv_play;
//    public SeekBar seekbar;
//    public TextView tv_progress;
//    public LinearLayout ll_progress;
    //节操播放器
    public JCVideoPlayer jc_videoplayer;

    public VideoViewHolder(View itemView, Context context) {
        super(itemView, context);
//        videoview = (VideoView) itemView.findViewById(R.id.videoview);
//        iv_video_img = (ImageView) itemView.findViewById(R.id.iv_video_img);
//        iv_play = (ImageView) itemView.findViewById(R.id.iv_play);
//        seekbar = (SeekBar) itemView.findViewById(R.id.seekbar);
//        ll_progress = (LinearLayout) itemView.findViewById(R.id.ll_progress);
//        tv_progress = (TextView) itemView.findViewById(R.id.tv_progress);
        jc_videoplayer = (JCVideoPlayer) itemView.findViewById(R.id.jc_videoplayer);
    }
}
