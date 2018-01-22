package com.zhoukp.photo.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhoukp.photo.R;
import com.zhoukp.photo.bean.SortModel;

import java.util.ArrayList;

/**
 * 作者： KaiPingZhou
 * 时间：2017/9/24 13:55
 * 邮箱：275557625@qq.com
 * 作用：
 */
public class ContactsSelectedAdapter extends RecyclerView.Adapter<ContactsSelectedAdapter.KPViewHolder> {

    private Context context;
    private ArrayList<SortModel> datas;

    public ContactsSelectedAdapter(Context context, ArrayList<SortModel> datas) {
        this.context = context;
        this.datas = datas;
    }

    class KPViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivHead;
        private TextView tvName;

        public KPViewHolder(View itemView) {
            super(itemView);
            ivHead = (ImageView) itemView.findViewById(R.id.ivHead);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
        }
    }


    @Override
    public KPViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.contact_listview_selected_item, null);
        return new KPViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(KPViewHolder holder, int position) {
        //在这里设置数据
        holder.tvName.setText(datas.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}
