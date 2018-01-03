package com.zhoukp.moments.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;

/**
 * 作者：zhoukp
 * 时间：2018/1/3 11:55
 * 邮箱：zhoukaiping@szy.cn
 * 作用：调用系统分享
 */

public class ShareUtils {
    /**
     * 调用系统分享分享单张图片
     *
     * @param contex activity
     */
    public static void shareSingleImage(Activity contex, String imagePath) {
        //由文件得到uri
        Uri imageUri = Uri.fromFile(new File(imagePath));

        Log.d("share", "uri:" + imageUri);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        contex.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }
}
