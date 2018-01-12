package com.zhoukp.moments.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhoukp.moments.R;
import com.zhoukp.moments.utils.LogUtil;

import java.util.ArrayList;

/**
 * 作者：zhoukp
 * 时间：2018/1/9 12:33
 * 邮箱：zhoukaiping@szy.cn
 * 作用：
 */

public class ListViewAdapter extends BaseAdapter {

    /**
     * 上下文
     */
    private Context context;
    /**
     * 评论列表
     */
    private ArrayList<String> datas;
    private ArrayList<String> names;

    public ListViewAdapter(Context context, ArrayList<String> datas, ArrayList<String> names) {
        this.context = context;
        this.datas = datas;
        this.names = names;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public String getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.listview_item, null);
            viewHolder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_comment.setText(datas.get(position));
        viewHolder.tv_name.setText(Html.fromHtml(getItem(position)));
        return convertView;
    }

    class ViewHolder {
        TextView tv_comment, tv_name;
    }

    public void addData(String data, String name) {
        datas.add(data);
        names.add(name);
        LogUtil.e("datas.size()==" + datas.size());
        notifyDataSetChanged();
    }

    public void insertData(String data, String name, int position) {
        datas.add(position, data);
        names.add(position, name);
        LogUtil.e("datas.size()==" + datas.size());
        notifyDataSetChanged();
    }

    public void deleteData(int position) {
        datas.remove(position);
        names.remove(position);
        LogUtil.e("datas.size()==" + datas.size());
        notifyDataSetChanged();
    }
}
