package com.zhoukp.photo.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 作者： zhoukp
 * 时间：2017/12/15 10:40
 * 邮箱：zhoukaiping@szy.cn
 * 作用：图片加载工具类
 */

public class ImageUtils {
    /**
     * 通过内容提供器来获取图片缩略图
     * 缺点:必须更新媒体库才能看到最新的缩略图
     *
     * @param context
     * @param cr
     * @param Imagepath
     * @return
     */
    public static Bitmap getImageThumbnail(Context context, ContentResolver cr, String Imagepath) {
        ContentResolver testcr = context.getContentResolver();
        String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID,};
        String whereClause = MediaStore.Images.Media.DATA + " = '" + Imagepath + "'";
        Cursor cursor = testcr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, whereClause, null, null);
        int _id = 0;
        String imagePath = "";
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        } else if (cursor.moveToFirst()) {
            int _idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            int _dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            do {
                _id = cursor.getInt(_idColumn);
                imagePath = cursor.getString(_dataColumn);
            } while (cursor.moveToNext());
        }
        cursor.close();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(cr, _id, MediaStore.Images.Thumbnails.MINI_KIND, options);
        return bitmap;
    }

    /**
     * 获取所有本地图片的缩略图链接
     *
     * @param context
     * @return
     */
    public static ArrayList<HashMap<String, String>> getAllPictures(Context context) {
        ArrayList<HashMap<String, String>> picturemaps = new ArrayList<>();
        HashMap<String, String> picturemap;
        ContentResolver cr = context.getContentResolver();
        //先得到缩略图的URL和对应的图片id
        Cursor cursor = cr.query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Images.Thumbnails.IMAGE_ID,
                        MediaStore.Images.Thumbnails.DATA
                },
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            do {
                picturemap = new HashMap<>();
                picturemap.put("image_id_path", cursor.getInt(0) + "");
                picturemap.put("thumbnail_path", cursor.getString(1));
                picturemaps.add(picturemap);
            } while (cursor.moveToNext());
            cursor.close();
        }
        //再得到正常图片的path
        for (int i = 0; i < picturemaps.size(); i++) {
            picturemap = picturemaps.get(i);
            String media_id = picturemap.get("image_id_path");
            cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{
                            MediaStore.Images.Media.DATA
                    },
                    MediaStore.Audio.Media._ID + "=" + media_id,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {
                do {
                    picturemap.put("image_id", cursor.getString(0));
                    picturemaps.set(i, picturemap);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        LogUtil.e("picturemaps==" + picturemaps.toString());
        return picturemaps;
    }

    /**
     * 获取图片的旋转角度
     *
     * @param filepath 图片文件的路径
     * @return 角度
     */
    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                    default:
                        break;
                }
            }
        }
        LogUtil.e("degree==" + degree);
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param bitmap  bitmap
     * @param degrees 角度
     * @return bitmap
     */
    public static Bitmap rotateImage(Bitmap bitmap, int degrees) {
        if (degrees != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(degrees);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }
}
