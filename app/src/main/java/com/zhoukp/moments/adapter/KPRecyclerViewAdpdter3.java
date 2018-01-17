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
import com.zhoukp.moments.bean.TableDataBean;
import com.zhoukp.moments.utils.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_PLAYING;

/**
 * 作者：zhoukp
 * 时间：2018/1/5 8:55
 * 邮箱：zhoukaiping@szy.cn
 * 作用：recyclerview数据适配器
 */

public class KPRecyclerViewAdpdter3 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
     * 上下文
     */
    private Context context;
    /**
     * 数据集合
     */
    private ArrayList<TableDataBean> datas;

    public KPRecyclerViewAdpdter3(Context context, ArrayList<TableDataBean> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getItemViewType(int position) {
        TableDataBean bean = datas.get(position);
        String type = bean.getType();
        int itemViewType = -1;
        if ("video".equals(type)) {
            itemViewType = TYPE_VIDEO;
        } else if ("image".equals(type)) {
            itemViewType = TYPE_IMAGE;
        } else {
            itemViewType = TYPE_TEXT;
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
        TableDataBean bean = datas.get(position);
        //判断viewHolder是那个类的实例
        if (holder instanceof ImageViewHolder) {
            //1.holder是ImageViewHolder的实例
            final ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
            LogUtil.e("图片");
            imageViewHolder.tv_title.setText(bean.getText());
            imageViewHolder.img.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
            imageViewHolder.img.setMinScale(1.0F);
            imageViewHolder.img.setMaxScale(1.5F);
            //下载图片保存到本地
            Glide.with(context).load(bean.getDownloadUrl()).downloadOnly(new SimpleTarget<File>() {
                @Override
                public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                    // 将保存的图片地址给SubsamplingScaleImageView,这里注意设置ImageViewState设置初始显示比例
                    imageViewHolder.img.setImage(ImageSource.uri(Uri.fromFile(resource)), new ImageViewState(2.0F, new PointF(0, 0), 0));
                }
            });
            imageViewHolder.tv_time.setText(bean.getText());
            imageViewHolder.tv_comment.setText(bean.getSubNumber());
            Glide.with(context).load(bean.getHeader()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageViewHolder.iv_head);
        } else if (holder instanceof VideoViewHolder) {
            //2.holder是VideoViewHolder的实例
            final VideoViewHolder videoViewHolder = ((VideoViewHolder) holder);
            LogUtil.e("视频");
            LogUtil.e("videoUrl==" + bean.getDownload());
            videoViewHolder.jc_videoplayer.setUp(bean.getDownload(), JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, bean.getText());
            Glide.with(context).load(bean.getThumbnail()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(videoViewHolder.jc_videoplayer.thumbImageView);
            videoViewHolder.tv_time.setText(bean.getPassTime());
            videoViewHolder.tv_title.setText(bean.getText());
            videoViewHolder.tv_comment.setText(bean.getSubNumber());
            Glide.with(context).load(bean.getHeader()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(videoViewHolder.iv_head);
        }else if (holder instanceof BaseViewHolder) {
            //5.holder是BaseViewHolder的实例
            LogUtil.e("文本");
            final BaseViewHolder baseViewHolder = (BaseViewHolder) holder;
            baseViewHolder.tv_title.setText(bean.getText());
            baseViewHolder.tv_time.setText(bean.getPassTime());
            baseViewHolder.tv_comment.setText(bean.getSubNumber());
            baseViewHolder.tv_name.setText(bean.getName());
            Glide.with(context).load(bean.getHeader()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(baseViewHolder.iv_head);
        }
    }
}
