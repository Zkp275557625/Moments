package com.zhoukp.photo.activity.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhoukp.photo.R;
import com.zhoukp.photo.bean.SortModel;

import java.util.ArrayList;
import java.util.Locale;

/**
 * time：2018/1/22 10:35
 * mail：zhoukaiping@szy.cn
 * for：
 *
 * @author zhoukp
 */

public class ContactsSortAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<SortModel> contacts;

    public ContactsSortAdapter(Context context, ArrayList<SortModel> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    private ArrayList<SortModel> getShowContacts(ArrayList<SortModel> contacts) {
        ArrayList<SortModel> result = new ArrayList<>();
        for (SortModel model : contacts) {
            if (model.isShow) {
                result.add(model);
            }
        }
        return result;
    }

    @Override
    public int getCount() {
        return getShowContacts(contacts).size();
    }

    @Override
    public SortModel getItem(int position) {
        return getShowContacts(contacts).get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.contact_listview_item, null);
            holder = new ViewHolder();
            holder.catalog = (TextView) convertView.findViewById(R.id.catalog);
            holder.ivSelected = (ImageView) convertView.findViewById(R.id.ivSelected);
            holder.tvChildName = (TextView) convertView.findViewById(R.id.tvChildName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.catalog.setVisibility(View.VISIBLE);
            holder.catalog.setText(getItem(position).sortLetters);
        } else {
            holder.catalog.setVisibility(View.GONE);
        }

        holder.tvChildName.setText(getItem(position).getName());
        holder.ivSelected.setSelected(getItem(position).isSelected);

        holder.ivSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getItem(position).setSelected(!holder.ivSelected.isSelected());
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView catalog;
        ImageView ivSelected;
        TextView tvChildName;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return getShowContacts(contacts).get(position).sortLetters.charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = getShowContacts(contacts).get(i).sortLetters;
            char firstChar = sortStr.toUpperCase(Locale.CHINESE).charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }
}
