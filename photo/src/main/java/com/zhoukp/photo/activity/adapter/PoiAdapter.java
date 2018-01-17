package com.zhoukp.photo.activity.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.location.Poi;
import com.baidu.mapapi.search.core.PoiInfo;
import com.zhoukp.photo.R;

import java.util.List;

/**
 * auther：zhoukp
 * time：2018/1/15 14:07
 * mail：zhoukaiping@szy.cn
 * for：poi界面数据适配器
 */

public class PoiAdapter extends BaseAdapter {

    protected Context context;
    protected List<PoiInfo> allPoi;

    public PoiAdapter(Context context, List<PoiInfo> allPoi) {
        this.context = context;
        this.allPoi = allPoi;
    }

    @Override
    public int getCount() {
        return allPoi.size();
    }

    @Override
    public PoiInfo getItem(int position) {
        return allPoi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.poi_listview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvLocation.setText(getItem(position).name);
        viewHolder.tvDescription.setText(getItem(position).address);

        return convertView;
    }

    public class ViewHolder {
        public TextView tvLocation, tvDescription;
    }
}
