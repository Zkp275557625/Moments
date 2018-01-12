package com.zhoukp.moments.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhoukp.moments.R;
import com.zhoukp.moments.adapter.viewholder.BaseViewHolder;
import com.zhoukp.moments.adapter.viewholder.ImageViewHolder;
import com.zhoukp.moments.adapter.viewholder.VideoViewHolder;
import com.zhoukp.moments.bean.ItemType;
import com.zhoukp.moments.utils.LogUtil;

import java.util.ArrayList;

/**
 * 作者：zhoukp
 * 时间：2018/1/5 8:55
 * 邮箱：zhoukaiping@szy.cn
 * 作用：recyclerview数据适配器
 */

public class KPRecyclerViewAdpdter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 上下文
     */
    private Context context;
    /**
     * 数据集合
     */
    private ArrayList<ItemType> datas;

    public KPRecyclerViewAdpdter2(Context context, ArrayList<ItemType> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getItemViewType(int position) {
        ItemType itemType = datas.get(position);
        return itemType.getType();
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType) {
            case 1:
                //文本
                LogUtil.e("文本");
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false);
                return new BaseViewHolder(itemView, parent.getContext());
            case 2:
                //图片
                LogUtil.e("图片");
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
                return new ImageViewHolder(itemView, parent.getContext());
            case 3:
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
        ItemType itemType = datas.get(position);
        //判断viewHolder是那个类的实例
        if (holder instanceof ImageViewHolder) {
            //1.holder是ImageViewHolder的实例
            final ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
            LogUtil.e("图片");
            imageViewHolder.tv_title.setText(itemType.getTitle());
            //imageViewHolder.imgs.setImagesData(itemType.getImages());
        } else if (holder instanceof VideoViewHolder) {
            //2.holder是VideoViewHolder的实例
            final VideoViewHolder videoViewHolder = ((VideoViewHolder) holder);
            LogUtil.e("视频");
//            videoViewHolder.jc_videoplayer.setUp(itemType.getVideoUrl(), itemType.getThumbnailUrl(), null);
        } else if (holder instanceof BaseViewHolder) {
            //3.holder是BaseViewHolder的实例
            LogUtil.e("文本");
            final BaseViewHolder baseViewHolder = (BaseViewHolder) holder;
            baseViewHolder.tv_title.setText(itemType.getTitle());
        }
    }
}
