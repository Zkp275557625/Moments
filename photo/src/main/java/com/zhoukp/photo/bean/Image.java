package com.zhoukp.photo.bean;

import android.graphics.Bitmap;

/**
 * 作者：zhoukp
 * 时间：2018/1/2 10:04
 * 邮箱：zhoukaiping@szy.cn
 * 作用：图片实体类
 */

public class Image {
    //原图路径
    private String url;
    //缩略图
    private Bitmap bitmap;
    //缩略图宽
    private int width;
    //缩略图高
    private int height;

    public Image(String url, int width, int height) {
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public Image(String url, Bitmap bitmap, int width, int height) {
        this.url = url;
        this.bitmap = bitmap;
        this.width = width;
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "image-->>url=" + url + "width=" + width + "height=" + height;
    }
}
