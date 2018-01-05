package com.zhoukp.moments.bean;

import java.util.ArrayList;

/**
 * 作者：zhoukp
 * 时间：2018/1/2 10:28
 * 邮箱：zhoukaiping@szy.cn
 * 作用：recyclerview中item的类型
 */

public class ItemType {
    /**
     * 1-->文本  2-->图片 3-->视频
     */
    private int type;
    /**
     * 标题
     */
    private String title;
    /**
     * item中的图片
     */
    private ArrayList<Image> images;
    /**
     * 视频的链接
     */
    private String videoUrl;
    /**
     * 视频缩略图链接
     */
    private String thumbnailUrl;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
