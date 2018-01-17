package com.zhoukp.moments.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zhoukp.moments.R;
import com.zhoukp.moments.adapter.KPRecyclerViewAdpdter;
import com.zhoukp.moments.adapter.KPRecyclerViewAdpdter3;
import com.zhoukp.moments.adapter.ListViewAdapter;
import com.zhoukp.moments.adapter.viewholder.BaseViewHolder;
import com.zhoukp.moments.bean.NetDataBean;
import com.zhoukp.moments.bean.TableDataBean;
import com.zhoukp.moments.model.IMainView;
import com.zhoukp.moments.model.MainPresenter;
import com.zhoukp.moments.utils.LogUtil;
import com.zhoukp.moments.utils.SpacesItemDecoration;

import java.util.ArrayList;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * 作者：zhoukp
 * 时间：2018/1/2 10:09
 * 邮箱：zhoukaiping@szy.cn
 * 作用：仿微信朋友圈多种类型item的列表展示
 * @author zhoukp
 */
public class MainActivity extends AppCompatActivity implements IMainView {

    private RecyclerView recyclerview;
    protected LinearLayoutManager manager;
    protected ListViewAdapter listViewAdapter;
    protected ArrayList<String> commemts;
    protected ArrayList<String> names;

    private MainPresenter presenter;
    private KPRecyclerViewAdpdter3 adpdter;

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

        initPresenter();

        //初始化数据
        initData();

        //初始化事件
        initEvent();
    }

    private void initPresenter() {
        presenter = new MainPresenter(this);
        presenter.attach(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        presenter.loadData();
    }

    /**
     * 初始化事件
     */
    private void initEvent() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            BaseViewHolder viewHolder;
            switch (requestCode) {
                case 1:
                    //提交评论的逻辑处理
                    String text = data.getStringExtra("text");
                    int position = Integer.parseInt(data.getStringExtra("position"));
                    LogUtil.e(position + "," + text);
                    viewHolder = (BaseViewHolder) recyclerview.findViewHolderForLayoutPosition(position);
                    if (viewHolder.listView.getAdapter() == null) {
                        LogUtil.e("null");
                        commemts = new ArrayList<>();
                        commemts.add(text);
                        names = new ArrayList<>();
                        names.add(viewHolder.tv_name.getText().toString() + "：");
                        listViewAdapter = new ListViewAdapter(MainActivity.this, commemts, names);
                        viewHolder.listView.setAdapter(listViewAdapter);
                    } else {
                        LogUtil.e("!null");
                        listViewAdapter = ((ListViewAdapter) viewHolder.listView.getAdapter());
                        listViewAdapter.addData(text, viewHolder.tv_name.getText().toString() + "：");
                    }
                    int totalHeight = 0;

                    for (int i = 0; i < listViewAdapter.getCount(); i++) {
                        View listItem = listViewAdapter.getView(i, null, viewHolder.listView);
                        listItem.measure(0, 0);
                        totalHeight += listItem.getMeasuredHeight();
                    }

                    ViewGroup.LayoutParams params = viewHolder.listView.getLayoutParams();

                    params.height = totalHeight + (viewHolder.listView.getDividerHeight() * (listViewAdapter.getCount() - 1));

                    ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
                    viewHolder.listView.setLayoutParams(params);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //释放节操播放器中持有的所有视频(如果不释放，应用切换到后台之后视频还会播放，可能会引起一系列问题)
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

//    @Override
//    public void loadDataSuccess(final NetDataBean netDataBean) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                adpdter = new KPRecyclerViewAdpdter(MainActivity.this, netDataBean.getList());
//                recyclerview.setAdapter(adpdter);
//            }
//        });
//    }

    @Override
    public void loadDataSuccess2(final ArrayList<TableDataBean> dataBean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adpdter = new KPRecyclerViewAdpdter3(MainActivity.this, dataBean);
                recyclerview.setAdapter(adpdter);
            }
        });
    }
}
