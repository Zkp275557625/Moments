package com.zhoukp.photo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.zhoukp.photo.R;
import com.zhoukp.photo.activity.adapter.ViewPagerAdapter;
import com.zhoukp.photo.activity.fragment.ClassFragment;
import com.zhoukp.photo.utils.TabUtils;

import java.util.ArrayList;

/**
 * time：2018/1/16 15:04
 * mail：zhoukaiping@szy.cn
 * for：选择发布对象页面
 *
 * @author zhoukp
 */

public class ContactActivity extends AppCompatActivity {

    protected ImageView ivBack;
    protected TabLayout tabLayout;
    protected ViewPager viewPager;

    private ArrayList<ClassFragment> fragments;
    private ArrayList<String> titles;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        initViews();

        initVariables();
    }

    private void initViews() {
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        //关联viewpager
        tabLayout.setupWithViewPager(viewPager);
        //设置显示模式
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    private void initVariables() {
        fragments = new ArrayList<>();
        fragments.add(new ClassFragment());
        fragments.add(new ClassFragment());

        titles = new ArrayList<>();
        titles.add("班级");
        titles.add("宝宝");

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        TabUtils.reflex(tabLayout);
    }
}
