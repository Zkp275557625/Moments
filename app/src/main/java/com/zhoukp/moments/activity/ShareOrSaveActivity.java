package com.zhoukp.moments.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhoukp.moments.R;
import com.zhoukp.moments.utils.ShareUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 作者：zhoukp
 * 时间：2018/1/3 16:46
 * 邮箱：zhoukaiping@szy.cn
 * 作用：分享或保存页面
 */

public class ShareOrSaveActivity extends Activity implements View.OnClickListener {

    private TextView textcancle;
    private TextView tvsave;
    private TextView tvshare;

    /**
     * 图片的链接
     */
    private String imageUrl;
    /**
     * 图片是否来源于网络 false-->来源于assets文件夹  true-->来源于网络
     */
    private boolean isFromNet;
    /**
     * 分享图片的链接
     */
    private String shareImgUrl = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareorsave);

        initViews();

        initData();

        initEvent();
    }

    private void initViews() {
        textcancle = (TextView) findViewById(R.id.text_cancle);
        tvsave = (TextView) findViewById(R.id.tv_save);
        tvshare = (TextView) findViewById(R.id.tv_share);
    }

    private void initData() {
        //获取上级页面传递过来的数据
        imageUrl = getIntent().getStringExtra("url");
        //图片是否来源于网络
        isFromNet = getIntent().getBooleanExtra("isFromNet", false);
    }

    private void initEvent() {
        tvsave.setOnClickListener(this);
        tvshare.setOnClickListener(this);
        textcancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_save:
                //保存逻辑的处理
                saveImage();
                Toast.makeText(ShareOrSaveActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.tv_share:
                //分享逻辑的处理
                File file = new File(shareImgUrl);
                if (!file.exists()) {
                    saveImage();
                }
                ShareUtils.shareSingleImage(ShareOrSaveActivity.this, shareImgUrl);
                finish();
                break;
            case R.id.text_cancle:
                //取消逻辑的处理
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 保存图片到本地
     */
    private void saveImage() {
        if (!isFromNet) {
            //图片来源于assets文件夹的情况
            AssetManager assetManager = getResources().getAssets();
            try {
                InputStream inputStream = assetManager.open(imageUrl);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                shareImgUrl = Environment.getExternalStorageDirectory()
                        + File.separator + "DCIM/" + getCurrentTime() + ".jpg";

                File file = new File(shareImgUrl);

                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                FileOutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();

                //保存图片后发送广播通知更新数据库
                Uri uri = Uri.fromFile(file);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //图片来源于网络的情况
            URL myFileUrl = null;
            Bitmap bitmap = null;

            try {
                myFileUrl = new URL(imageUrl);
                HttpURLConnection conn;

                conn = (HttpURLConnection) myFileUrl.openConnection();

                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);

                shareImgUrl = Environment.getExternalStorageDirectory()
                        + File.separator + "DCIM/" + getCurrentTime() + ".jpg";

                File file = new File(shareImgUrl);

                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                FileOutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();

                //保存图片后发送广播通知更新数据库
                Uri uri = Uri.fromFile(file);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getCurrentTime() {
        return System.currentTimeMillis() / 1000 + "";
    }
}
