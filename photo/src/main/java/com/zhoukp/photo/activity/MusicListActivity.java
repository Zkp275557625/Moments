package com.zhoukp.photo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhoukp.photo.R;
import com.zhoukp.photo.activity.adapter.MusicListAdapter;
import com.zhoukp.photo.bean.MusicInfo;
import com.zhoukp.photo.utils.LogUtil;

import java.util.ArrayList;

/**
 * auther：zhoukp
 * time：2018/1/15 16:27
 * mail：zhoukaiping@szy.cn
 * for：音乐列表选择页面
 *
 * @author zhoukp
 */

public class MusicListActivity extends AppCompatActivity implements View.OnClickListener {

    protected ImageView ivBack;
    protected TextView tvTitle, tvDone, tvNoMusic;
    protected ExpandableListView listView;

    protected MusicListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        initViews();

        initData();

        initEvent();
    }

    private void initViews() {
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("选择背景音乐");
        tvDone = (TextView) findViewById(R.id.tvDone);
        tvDone.setText("保存");
        tvNoMusic = (TextView) findViewById(R.id.tvNoMusic);

        listView = (ExpandableListView) findViewById(R.id.listView);
    }

    private void initData() {
        MusicInfo musicInfo = new MusicInfo();
        musicInfo.setGroupName("儿童音乐精选");
        ArrayList<String> children = new ArrayList<>();
        children.add("小星星");
        children.add("彩虹的约定");
        children.add("我爱洗澡");
        children.add("数鸭子");
        children.add("春天在哪里");
        children.add("葫芦娃");
        children.add("爱我你就抱抱我");
        children.add("童年");
        children.add("小手牵大手");
        musicInfo.setChildren(children);

        ArrayList<MusicInfo> groups = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            groups.add(musicInfo);
        }

        adapter = new MusicListAdapter(MusicListActivity.this, groups);
        listView.setAdapter(adapter);
    }

    private void initEvent() {
        ivBack.setOnClickListener(this);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
                MusicListAdapter.ChildViewHolder viewHolder = (MusicListAdapter.ChildViewHolder) view.getTag();
                LogUtil.e(viewHolder.tvChildName.getText().toString());
                Intent intent = new Intent();
                intent.putExtra("music", viewHolder.tvChildName.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                return false;
            }
        });
        tvNoMusic.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                //关闭当前页面
                finish();
                break;
            case R.id.tvNoMusic:
                //不选择背景音乐
                tvNoMusic.setSelected(true);
                setResult(RESULT_OK);
                finish();
                break;
            default:
                break;
        }
    }
}
