package com.zhoukp.moments.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhoukp.moments.R;
import com.zhoukp.moments.adapter.KPRecyclerViewAdpdter;
import com.zhoukp.moments.adapter.KPRecyclerViewAdpdter2;
import com.zhoukp.moments.adapter.viewholder.BaseViewHolder;
import com.zhoukp.moments.bean.Image;
import com.zhoukp.moments.bean.ItemType;
import com.zhoukp.moments.utils.LogUtil;
import com.zhoukp.moments.utils.SpacesItemDecoration;

import java.util.ArrayList;

/**
 * 作者：zhoukp
 * 时间：2018/1/2 10:09
 * 邮箱：zhoukaiping@szy.cn
 * 作用：仿微信朋友圈多种类型item的列表展示
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerview;
    private ArrayList<ItemType> datas;
    private KPRecyclerViewAdpdter adpdter;
    private KPRecyclerViewAdpdter2 adpdter2;
    private LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        //设置recyclerview布局方式为垂直显示
        manager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(manager);
        //设置recyclerview动画为默认动画
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        //设置recyclerview的item的间距
        int spacingInPixels = 8;
        recyclerview.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        //初始化数据
        initData();

        //初始化事件
        initEvent();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        String title = "大家新年快乐";
        String[][] images = new String[][]{{
                "http://img4.duitang.com/uploads/item/201209/25/20120925201555_eUHEU.jpeg", "640", "960"}
                , {"file:///android_asset/img1.jpg", "250", "250"}
                , {"file:///android_asset/img2.jpg", "250", "250"}
                , {"file:///android_asset/img3.jpg", "250", "250"}
                , {"file:///android_asset/img4.jpg", "250", "250"}
                , {"file:///android_asset/img5.jpg", "250", "250"}
                , {"file:///android_asset/img6.jpg", "250", "250"}
                , {"file:///android_asset/img7.jpg", "250", "250"}
                , {"http://img.blog.csdn.net/20161119235312178?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQv/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center", "1280", "800"}
        };
//        String url = "android.resource://" + getPackageName() + "/" + R.raw.video;
        String url = "http://dvideo.spriteapp.cn/video/2018/0105/5f004944-f1b5-11e7-a1f8-1866daeb0df1cutblack_wpd.mp4";
        String thumbnail = "http://dimg.spriteapp.cn/picture/2018/0105/5f004944-f1b5-11e7-a1f8-1866daeb0df1cutblack_wpd.jpg";

        datas = new ArrayList<>();

        ItemType itemTypeTitle = new ItemType();
        itemTypeTitle.setType(1);
        itemTypeTitle.setTitle(title);

        ArrayList<Image> imgs = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            imgs.add(new Image(images[i][0], Integer.parseInt(images[i][1]), Integer.parseInt(images[i][2])));
        }

        ItemType itemTypeImgs = new ItemType();
        itemTypeImgs.setType(2);
        itemTypeImgs.setTitle(title);
        itemTypeImgs.setImages(imgs);


        ItemType itemTypeVideo = new ItemType();
        itemTypeVideo.setType(3);
        itemTypeVideo.setTitle(title);
        itemTypeVideo.setVideoUrl(url);
        itemTypeVideo.setThumbnailUrl(thumbnail);

        for (int i = 0; i < 3; i++) {
            datas.add(itemTypeTitle);
            datas.add(itemTypeImgs);
            datas.add(itemTypeVideo);
        }

//        adpdter = new KPRecyclerViewAdpdter(MainActivity.this, datas);
        adpdter2 = new KPRecyclerViewAdpdter2(MainActivity.this, datas);

        recyclerview.setAdapter(adpdter2);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                /**
                 * new State
                 0（SCROLL_STATE_IDLE）表示recyclerview是不动的
                 1（SCROLL_STATE_DRAGGING）表示recyclerview正在被拖拽
                 2（SCROLL_STATE_SETTLING）表示recyclerview正在惯性下滚动
                 */
                switch (newState) {
                    case 0:
                        System.out.println("recyclerview已经停止滚动");
                        break;
                    case 1:
                        System.out.println("recyclerview正在被拖拽");
                        break;
                    case 2:
                        System.out.println("recyclerview正在依靠惯性滚动");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();//可见范围内的第一项的位置
//                int lastVisibleItemPosition = manager.findLastVisibleItemPosition();//可见范围内的最后一项的位置
//                int itemCount = manager.getItemCount();//recyclerview中的item的所有的数目
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            BaseViewHolder viewHolder = null;
            switch (requestCode) {
                case 1:
                    //提交的评论逻辑处理
                    String text = data.getStringExtra("text");
                    int position = Integer.parseInt(data.getStringExtra("position"));
                    LogUtil.e(position + "," + text);
                    viewHolder = (BaseViewHolder) recyclerview.findViewHolderForLayoutPosition(position);
                    viewHolder.tv_comment.setText(Integer.parseInt(viewHolder.tv_comment.getText().toString()) + 1 + "");

                    TextView textView = new TextView(MainActivity.this);
                    textView.setTextSize(14);
                    String str = "<font color='#29b6f6'>" + viewHolder.tv_name.getText().toString() + "：</font>" +
                            "<font color='#000000'>" + text + "</font>";
                    textView.setText(Html.fromHtml(str));
                    Drawable drawable = MainActivity.this.getResources().getDrawable(R.drawable.icon_comment);
                    // 这一步必须要做,否则不会显示
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    textView.setCompoundDrawables(drawable, null, null, null);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    //4个参数按顺序分别是左上右下
                    layoutParams.setMargins(10, 10, 0, 0);
                    //设置textview的margin值
                    textView.setLayoutParams(layoutParams);
                    textView.setCompoundDrawablePadding(5);
                    viewHolder.ll_comment.addView(textView);
                    break;
                default:
                    break;
            }
        }
    }
}
