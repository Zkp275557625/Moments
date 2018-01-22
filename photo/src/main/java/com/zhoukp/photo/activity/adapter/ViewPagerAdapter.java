package com.zhoukp.photo.activity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zhoukp.photo.activity.fragment.BaseFragment;
import com.zhoukp.photo.activity.fragment.ClassFragment;

import java.util.ArrayList;

/**
 * auther：zhoukp
 * time：2018/1/16 15:37
 * mail：zhoukaiping@szy.cn
 * for：
 *
 * @author zhoukp
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<BaseFragment> fragments;
    private final ArrayList<String> titles;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments, ArrayList<String> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
