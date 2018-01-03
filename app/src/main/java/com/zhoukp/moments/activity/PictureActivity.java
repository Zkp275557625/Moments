package com.zhoukp.moments.activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zhoukp.moments.R;
import com.zhoukp.moments.view.ImageViewTouch;

import java.io.IOException;
import java.io.InputStream;

/**
 * 作者：zhoukp
 * 时间：2018/1/3 16:10
 * 邮箱：zhoukaiping@szy.cn
 * 作用：查看图片页面
 */

public class PictureActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageViewTouch image;
    private ImageView ivmore;
    private ImageView ivback;

    private Bitmap bitmap;
    private String imageUrl;
    private boolean isFromNet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        initViews();

        initData();

        initEvent();
    }

    private void initViews() {
        image = (ImageViewTouch) findViewById(R.id.image);
        ivmore = (ImageView) findViewById(R.id.iv_more);
        ivback = (ImageView) findViewById(R.id.iv_back);
    }

    private void initData() {
        imageUrl = getIntent().getStringExtra("url");
        //图片是否来源于网络
        isFromNet = getIntent().getBooleanExtra("isFromNet", false);
        if (!isFromNet) {
            imageUrl = imageUrl.substring(22);

            AssetManager assetManager = getResources().getAssets();
            try {
                InputStream inputStream = assetManager.open(imageUrl);

                bitmap = BitmapFactory.decodeStream(inputStream);

                image.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Picasso.with(PictureActivity.this).load(imageUrl).placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))).into(image);
        }
    }

    private void initEvent() {
        ivback.setOnClickListener(this);
        ivmore.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                //点击返回按钮，关闭当前页面
                finish();
                break;
            case R.id.iv_more:
                //点击更多按钮，让用户选择保存到手机还是分享给好友(调用系统分享)
//                Toast.makeText(PictureActivity.this, "更多", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PictureActivity.this, ShareOrSaveActivity.class);
                intent.putExtra("url", imageUrl);
                intent.putExtra("isFromNet", isFromNet);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
