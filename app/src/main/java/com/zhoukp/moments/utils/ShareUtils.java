package com.zhoukp.moments.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.util.HashMap;

/**
 * 作者：zhoukp
 * 时间：2018/1/3 11:55
 * 邮箱：zhoukaiping@szy.cn
 * 作用：一些方法
 */

public class ShareUtils {
    /**
     * 调用系统分享分享单张图片
     *
     * @param context activity
     */
    public static void shareSingleImage(Activity context, String imagePath) {
        //由文件得到uri
        Uri imageUri = Uri.fromFile(new File(imagePath));

        Log.d("share", "uri:" + imageUri);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    /**
     * 服务器返回url，通过url去获取视频的第一帧
     * Android 原生给我们提供了一个MediaMetadataRetriever类
     * 提供了获取url视频第一帧的方法,返回Bitmap对象
     *
     * @param videoUrl
     * @return
     */
    public static Bitmap getNetVideoBitmap(String videoUrl) {
        Bitmap bitmap = null;

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据url获取缩略图
            retriever.setDataSource(videoUrl, new HashMap<String, String>());
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime(10 * 1000 * 1000, MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

    /**
     * 获取raw文件夹下视频文件的缩略图
     *
     * @param context
     * @param uri
     * @return
     */
    public static Drawable getThumbnail(Context context, Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context, uri);
        Bitmap bitmap = retriever
                .getFrameAtTime(10 * 1000 * 1000, MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);
        Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);

        return drawable;
    }

    /**
     * 获取网络视频的分辨率
     *
     * @param videoPath
     * @return
     */
    public static MediaMetadataRetriever getNetVideoSize(String videoPath) {
        MediaMetadataRetriever retr = new MediaMetadataRetriever();
        retr.setDataSource(videoPath, new HashMap<String, String>());
        return retr;
    }
}
