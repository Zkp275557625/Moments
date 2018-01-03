package com.zhoukp.moments.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.zhoukp.moments.R;
import com.zhoukp.moments.activity.CommentActivity;
import com.zhoukp.moments.activity.MainActivity;
import com.zhoukp.moments.bean.ItemType;
import com.zhoukp.moments.utils.LogUtil;
import com.zhoukp.moments.utils.ShareUtils;
import com.zhoukp.moments.utils.Utils;
import com.zhoukp.moments.view.NineGridlayout;
import com.zhoukp.moments.view.RoundRectImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

/**
 * 作者：zhoukp
 * 时间：2018/1/2 10:25
 * 邮箱：zhoukaiping@szy.cn
 * 作用：recyclerview数据适配器
 */

public class KPRecyclerViewAdpdter extends RecyclerView.Adapter<KPRecyclerViewAdpdter.KPViewHolder> {

    /**
     * 上下文
     */
    private Context context;
    /**
     * 数据集合
     */
    private ArrayList<ItemType> datas;
    /**
     * 时间处理工具类
     */
    private Utils utils;

    Handler handler = null;

    public KPRecyclerViewAdpdter(Context context, ArrayList<ItemType> datas) {
        this.context = context;
        this.datas = datas;
        utils = new Utils();
    }

    @Override
    public KPViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new KPViewHolder(itemView);
    }

    /**
     * 为recyclerview的item绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final KPViewHolder holder, final int position) {
        ItemType itemType = datas.get(position);
        switch (itemType.getType()) {
            case 1:
                //文本类型
                //1.隐藏图片和视频控件
                holder.imgs.setVisibility(View.GONE);
                holder.videoview.setVisibility(View.GONE);
                holder.iv_video_img.setVisibility(View.GONE);
                holder.iv_play.setVisibility(View.GONE);
                holder.rl_video.setVisibility(View.GONE);
                holder.ll_progress.setVisibility(View.GONE);
                //2.设置数据
                holder.tv_title.setText(itemType.getTitle());
                break;
            case 2:
                //图片类型
                //1.显示图片控件、隐藏视频控件
                holder.imgs.setVisibility(View.VISIBLE);
                holder.videoview.setVisibility(View.GONE);
                holder.iv_video_img.setVisibility(View.GONE);
                holder.iv_play.setVisibility(View.GONE);
                holder.rl_video.setVisibility(View.GONE);
                holder.ll_progress.setVisibility(View.GONE);
                //2.设置数据
                holder.tv_title.setText(itemType.getTitle());
                holder.imgs.setImagesData(itemType.getImages());

                holder.imgs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NineGridlayout gridlayout = (NineGridlayout) view;
                        LogUtil.e("孩子数量为:" + gridlayout.getChildCount());
                    }
                });
                break;
            case 3:
                //视频类型
                //1.隐藏图片控件、显示视频控件
                holder.imgs.setVisibility(View.GONE);
                holder.videoview.setVisibility(View.GONE);
                holder.iv_video_img.setVisibility(View.VISIBLE);
                holder.iv_play.setVisibility(View.VISIBLE);
                holder.rl_video.setVisibility(View.VISIBLE);
                //2.设置数据
                holder.tv_title.setText(itemType.getTitle());
                holder.videoview.setVideoURI(Uri.parse(itemType.getVideoUrl()));
                holder.iv_video_img.setBackground(getThumbnail(context, Uri.parse(itemType.getVideoUrl())));
                MediaMetadataRetriever retr = new MediaMetadataRetriever();
                retr.setDataSource(context, Uri.parse(itemType.getVideoUrl()));
                //视频高度
                final int height = Integer.parseInt(retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                //视频宽度
                int width = Integer.parseInt(retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));

                ViewGroup.LayoutParams params = holder.videoview.getLayoutParams();
                params.height = height;
                params.width = width;
                holder.videoview.setLayoutParams(params);
                //设置seekbar的最大值
                holder.seekbar.setMax(1000);

                holder.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    /**
                     * 当手指滑动的时候，会引起SeekBar进度变化，会回调这个方法
                     *
                     * @param seekBar
                     * @param progress
                     * @param fromUser 如果是用户引起的true,不是用户引起的false
                     */
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            int targetProcess = progress * holder.videoview.getDuration() / 1000;
                            LogUtil.e("progress==" + targetProcess);
                            holder.videoview.seekTo(targetProcess);
                            holder.videoview.start();
                            holder.tv_progress.setText(utils.stringForTime(holder.videoview.getCurrentPosition())
                                    + "/" + utils.stringForTime(holder.videoview.getDuration()));
                            if (handler == null) {
                                initHandler(holder);
                            }
                            handler.removeMessages(1);
                            handler.sendEmptyMessageDelayed(1, 1000);
                        }
                    }

                    /**
                     * 当手指触碰的时候回调这个方法
                     * @param seekBar
                     */
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        LogUtil.e("手指触碰了");
                    }

                    /**
                     * 当手指离开的时候回调这个方法
                     * @param seekBar
                     */
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        LogUtil.e("手指离开了");
                    }
                });

                initHandler(holder);

                //3.设置点击事件，播放视频
                holder.iv_play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.videoview.setVisibility(View.VISIBLE);
                        holder.ll_progress.setVisibility(View.VISIBLE);
                        holder.seekbar.setVisibility(View.VISIBLE);
                        holder.tv_progress.setVisibility(View.VISIBLE);
                        holder.iv_video_img.setVisibility(View.GONE);
                        holder.iv_play.setVisibility(View.GONE);

                        holder.videoview.start();
                        handler.sendEmptyMessage(1);
                    }
                });

                holder.videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        //适应屏幕显示
                        mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                    }
                });

                break;
            default:
                break;
        }
    }

    private void initHandler(final KPViewHolder holder) {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //1.得到当前的视频播放进程
                int currentPosition = (int) (holder.videoview.getCurrentPosition() * 1000.0f / holder.videoview.getDuration());

                //2.SeekBar.setProgress(当前进度);
                holder.seekbar.setProgress(currentPosition);

                //LogUtil.e("currentPosition==" + currentPosition);

                //更新文本播放进度
                holder.tv_progress.setText(utils.stringForTime(holder.videoview.getCurrentPosition())
                        + "/" + utils.stringForTime(holder.videoview.getDuration()));

                //3.每秒更新一次
                handler.removeMessages(1);
                handler.sendEmptyMessageDelayed(1, 1000);

            }
        };
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    /**
     * 获取raw文件夹下视频文件的缩略图
     *
     * @param context
     * @param uri
     * @return
     */
    private Drawable getThumbnail(Context context, Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context, uri);
        Bitmap bitmap = retriever
                .getFrameAtTime(10 * 1000 * 1000, MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);
        Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);

        return drawable;
    }


    public class KPViewHolder extends RecyclerView.ViewHolder {
        private RoundRectImageView iv_head;
        public TextView tv_name, tv_time, tv_title, tv_scan_number;
        private NineGridlayout imgs;
        public VideoView videoview;
        private ImageView iv_like, iv_share, iv_comment;
        public TextView tv_like, tv_share, tv_comment;
        private ImageView iv_video_img, iv_play;
        private LinearLayout ll_like, ll_progress;
        public LinearLayout ll_comment, ll_conent;
        private TextView tv_content, tv_progress;
        private SeekBar seekbar;
        private RelativeLayout rl_video;

        private ArrayList<TextView> textViews = new ArrayList<>();

        /**
         * 是否已经点击喜欢(点赞)
         */
        private boolean isAlreadyLike = false;

        public KPViewHolder(View itemView) {
            super(itemView);

            iv_head = (RoundRectImageView) itemView.findViewById(R.id.iv_head);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            imgs = (NineGridlayout) itemView.findViewById(R.id.imgs);
            videoview = (VideoView) itemView.findViewById(R.id.videoview);
            tv_scan_number = (TextView) itemView.findViewById(R.id.tv_scan_number);
            iv_like = (ImageView) itemView.findViewById(R.id.iv_like);
            tv_like = (TextView) itemView.findViewById(R.id.tv_like);
            iv_share = (ImageView) itemView.findViewById(R.id.iv_share);
            tv_share = (TextView) itemView.findViewById(R.id.tv_share);
            iv_comment = (ImageView) itemView.findViewById(R.id.iv_comment);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
            iv_video_img = (ImageView) itemView.findViewById(R.id.iv_video_img);
            iv_play = (ImageView) itemView.findViewById(R.id.iv_play);
            ll_like = (LinearLayout) itemView.findViewById(R.id.ll_like);
            ll_comment = (LinearLayout) itemView.findViewById(R.id.ll_comment);
            ll_conent = (LinearLayout) itemView.findViewById(R.id.ll_conent);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            seekbar = (SeekBar) itemView.findViewById(R.id.seekbar);
            rl_video = (RelativeLayout) itemView.findViewById(R.id.rl_video);
            ll_progress = (LinearLayout) itemView.findViewById(R.id.ll_progress);
            tv_progress = (TextView) itemView.findViewById(R.id.tv_progress);

            iv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isAlreadyLike) {
                        tv_like.setText(Integer.parseInt(tv_like.getText().toString()) + 1 + "");
                        TextView textView = new TextView(context);
                        textView.setTextSize(14);
                        textView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        textView.setText(tv_name.getText().toString());
                        Drawable drawable = context.getResources().getDrawable(R.drawable.icon_like);
                        // 这一步必须要做,否则不会显示
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        textView.setCompoundDrawables(drawable, null, null, null);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        //4个参数按顺序分别是左上右下
                        layoutParams.setMargins(10, 10, 0, 0);
                        //设置textview的margin值
                        textView.setLayoutParams(layoutParams);
                        textView.setCompoundDrawablePadding(5);
                        ll_like.addView(textView, 0);
                        textViews.add(textView);
                        isAlreadyLike = true;
                    } else {
                        tv_like.setText(Integer.parseInt(tv_like.getText().toString()) - 1 + "");
                        for (int i = 0; i < ll_like.getChildCount(); i++) {
                            if (TextUtils.equals(textViews.get(i).getText().toString(), tv_name.getText().toString())) {
                                ll_like.removeView(textViews.get(i));
                                textViews.remove(i);
                                break;
                            }
                        }
                        isAlreadyLike = false;
                    }
                }
            });

            iv_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareUtils.shareSingleImage((MainActivity) context,
                            Environment.getExternalStorageDirectory() + File.separator + "DCIM/1513397578833.png");
                    tv_share.setText(Integer.parseInt(tv_share.getText().toString()) + 1 + "");
                }
            });

            //评论逻辑的处理
            iv_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("position", getLayoutPosition() + "");
                    ((MainActivity) context).startActivityForResult(intent, 1);
                }
            });

            tv_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("position", getLayoutPosition() + "");
                    ((MainActivity) context).startActivityForResult(intent, 1);
                }
            });
        }
    }


}
