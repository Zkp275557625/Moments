package com.zhoukp.moments.adapter;

import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.zhoukp.moments.R;
import com.zhoukp.moments.adapter.viewholder.ADViewHolder;
import com.zhoukp.moments.adapter.viewholder.BaseViewHolder;
import com.zhoukp.moments.adapter.viewholder.GifViewHolder;
import com.zhoukp.moments.adapter.viewholder.ImageViewHolder;
import com.zhoukp.moments.adapter.viewholder.VideoViewHolder;
import com.zhoukp.moments.bean.NetDataBean;
import com.zhoukp.moments.utils.LogUtil;

import java.io.File;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_PLAYING;

/**
 * 作者：zhoukp
 * 时间：2018/1/5 8:55
 * 邮箱：zhoukaiping@szy.cn
 * 作用：recyclerview数据适配器
 */

public class KPRecyclerViewAdpdter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 视频
     */
    private static final int TYPE_VIDEO = 0;

    /**
     * 图片
     */
    private static final int TYPE_IMAGE = 1;

    /**
     * 文字
     */
    private static final int TYPE_TEXT = 2;
    /**
     * GIF图片
     */
    private static final int TYPE_GIF = 3;

    /**
     * 软件推广
     */
    private static final int TYPE_AD = 4;

    /**
     * 上下文
     */
    private Context context;
    /**
     * 数据集合
     */
    private List<NetDataBean.ListEntity> datas;

    public KPRecyclerViewAdpdter(Context context, List<NetDataBean.ListEntity> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getItemViewType(int position) {
        NetDataBean.ListEntity listEntity = datas.get(position);
        String type = listEntity.getType();
        int itemViewType = -1;
        if ("video".equals(type)) {
            itemViewType = TYPE_VIDEO;
        } else if ("image".equals(type)) {
            itemViewType = TYPE_IMAGE;
        } else if ("text".equals(type)) {
            itemViewType = TYPE_TEXT;
        } else if ("gif".equals(type)) {
            itemViewType = TYPE_GIF;
        } else {
            //广告
            itemViewType = TYPE_AD;
        }
        return itemViewType;
    }

    /**
     * 当前item滚动到屏幕不可见的位置时调用这个方法
     * （用于对recyclerview的优化）
     *
     * @param holder viewHolder
     */
    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof VideoViewHolder) {
            LogUtil.e("解绑了");
            //如果视频正在播放，就暂停播放
            if (((VideoViewHolder) holder).jc_videoplayer.currentState == CURRENT_STATE_PLAYING) {
                ((VideoViewHolder) holder).jc_videoplayer.startButton.performClick();
            }
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType) {
            case TYPE_TEXT:
                //文本
                LogUtil.e("文本");
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false);
                return new BaseViewHolder(itemView, parent.getContext());
            case TYPE_IMAGE:
                //图片
                LogUtil.e("图片");
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
                return new ImageViewHolder(itemView, parent.getContext());
            case TYPE_VIDEO:
                //视频
                LogUtil.e("视频");
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
                return new VideoViewHolder(itemView, parent.getContext());
            case TYPE_GIF:
                LogUtil.e("GIF");
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gif, parent, false);
                return new GifViewHolder(itemView, parent.getContext());
            case TYPE_AD:
                LogUtil.e("AD");
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ad, parent, false);
                return new ADViewHolder(itemView, parent.getContext());
            default:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false);
                return new BaseViewHolder(itemView, parent.getContext());
        }
    }

    /**
     * 为recyclerview的item绑定数据
     *
     * @param holder   viewHolder
     * @param position 位置
     */
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        NetDataBean.ListEntity listEntity = datas.get(position);
        //判断viewHolder是那个类的实例
        if (holder instanceof ImageViewHolder) {
            //1.holder是ImageViewHolder的实例
            final ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
            LogUtil.e("图片");
            imageViewHolder.tv_title.setText(listEntity.getText());
            imageViewHolder.img.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
            imageViewHolder.img.setMinScale(1.0F);
            imageViewHolder.img.setMaxScale(1.5F);
            //下载图片保存到本地
            Glide.with(context).load(listEntity.getImage().getBig().get(0)).downloadOnly(new SimpleTarget<File>() {
                @Override
                public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                    // 将保存的图片地址给SubsamplingScaleImageView,这里注意设置ImageViewState设置初始显示比例
                    imageViewHolder.img.setImage(ImageSource.uri(Uri.fromFile(resource)), new ImageViewState(2.0F, new PointF(0, 0), 0));
                }
            });
            imageViewHolder.tv_time.setText(listEntity.getPasstime());
        } else if (holder instanceof VideoViewHolder) {
            //2.holder是VideoViewHolder的实例
            final VideoViewHolder videoViewHolder = ((VideoViewHolder) holder);
            LogUtil.e("视频");
            LogUtil.e("videoUrl==" + listEntity.getVideo().getVideo().get(0));
            videoViewHolder.jc_videoplayer.setUp(listEntity.getVideo().getVideo().get(0), JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, listEntity.getText());
            Glide.with(context).load(listEntity.getVideo().getThumbnail().get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(videoViewHolder.jc_videoplayer.thumbImageView);
            videoViewHolder.tv_time.setText(listEntity.getPasstime());
            videoViewHolder.tv_title.setText(listEntity.getText());
        } else if (holder instanceof GifViewHolder) {
            //3.holder是GIFViewHolder的实例
            GifViewHolder gifViewHolder = (GifViewHolder) holder;
            LogUtil.e("GIF");
            Glide.with(context).load(listEntity.getGif().getImages().get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(gifViewHolder.iv_image_gif);
            gifViewHolder.tv_time.setText(listEntity.getPasstime());
        } else if (holder instanceof ADViewHolder) {
            //4.holder是ADViewHolder的实例
            LogUtil.e("AD");
        } else if (holder instanceof BaseViewHolder) {
            //5.holder是BaseViewHolder的实例
            LogUtil.e("文本");
            final BaseViewHolder baseViewHolder = (BaseViewHolder) holder;
            baseViewHolder.tv_title.setText(listEntity.getText());
            baseViewHolder.tv_time.setText(listEntity.getPasstime());
        }
    }
}
