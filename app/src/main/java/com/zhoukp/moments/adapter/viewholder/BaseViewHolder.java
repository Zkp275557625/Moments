package com.zhoukp.moments.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhoukp.moments.R;
import com.zhoukp.moments.activity.CommentActivity;
import com.zhoukp.moments.activity.MainActivity;
import com.zhoukp.moments.adapter.ListViewAdapter;
import com.zhoukp.moments.utils.LogUtil;
import com.zhoukp.moments.utils.ShareUtils;
import com.zhoukp.moments.view.InputDialog;
import com.zhoukp.moments.view.RoundRectImageView;
import com.zhoukp.moments.view.pop.ActionItem;
import com.zhoukp.moments.view.pop.QuickAction;

import java.io.File;
import java.util.ArrayList;

/**
 * 作者：zhoukp
 * 时间：2018/1/5 8:54
 * 邮箱：zhoukaiping@szy.cn
 * 作用：ViewHolder基类
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    //头像
    public RoundRectImageView iv_head;
    //姓名，时间，标题
    public TextView tv_name, tv_time, tv_title;
    //浏览次数，评论次数，分享次数，点赞次数
    public TextView tv_scan_number, tv_comment, tv_share, tv_like, tv_content;
    //评论次数，分享次数，点赞次数
    public ImageView iv_comment, iv_share, iv_like;
    //点赞列表，评论列表，评论按钮
    public LinearLayout ll_like, ll_comment;
    public ListView listView;

    public ArrayList<TextView> textViews = new ArrayList<>();

    /**
     * 是否已经点击喜欢(点赞)
     */
    public boolean isAlreadyLike = false;

    public BaseViewHolder(View itemView, final Context context) {
        super(itemView);
        iv_head = (RoundRectImageView) itemView.findViewById(R.id.iv_head);
        tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        tv_scan_number = (TextView) itemView.findViewById(R.id.tv_scan_number);
        tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
        iv_comment = (ImageView) itemView.findViewById(R.id.iv_comment);
        tv_share = (TextView) itemView.findViewById(R.id.tv_share);
        iv_share = (ImageView) itemView.findViewById(R.id.iv_share);
        tv_like = (TextView) itemView.findViewById(R.id.tv_like);
        iv_like = (ImageView) itemView.findViewById(R.id.iv_like);
        ll_like = (LinearLayout) itemView.findViewById(R.id.ll_like);
        ll_comment = (LinearLayout) itemView.findViewById(R.id.ll_comment);
        tv_content = (TextView) itemView.findViewById(R.id.tv_content);
        listView = (ListView) itemView.findViewById(R.id.listview);

        iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAlreadyLike) {
                    tv_like.setText(Integer.parseInt(tv_like.getText().toString()) + 1 + "");
                    TextView textView = new TextView(context);
                    textView.setTextSize(14);
                    textView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    textView.setText(tv_name.getText().toString());
                    Drawable drawable = context.getResources().getDrawable(R.drawable.icon_like);
                    // 这一步必须要做,否则不会显示
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    textView.setCompoundDrawables(drawable, null, null, null);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    //4个参数按顺序分别是左上右下
                    layoutParams.setMargins(10, 10, 0, 0);
                    //设置textview的margin值
                    textView.setLayoutParams(layoutParams);
                    textView.setCompoundDrawablePadding(5);
                    ll_like.addView(textView, 0);
                    textViews.add(textView);
                    isAlreadyLike = true;
                } else {
                    tv_like.setText(Integer.parseInt(tv_like.getText().toString()) - 1 + "");
                    for (int i = 0; i < ll_like.getChildCount(); i++) {
                        if (TextUtils.equals(textViews.get(i).getText().toString(), tv_name.getText().toString())) {
                            ll_like.removeView(textViews.get(i));
                            textViews.remove(i);
                            break;
                        }
                    }
                    isAlreadyLike = false;
                }
            }
        });

        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.shareSingleImage((MainActivity) context,
                        Environment.getExternalStorageDirectory() + File.separator + "DCIM/1513397578833.png");
                tv_share.setText(Integer.parseInt(tv_share.getText().toString()) + 1 + "");
            }
        });

        //评论逻辑的处理
        iv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("position", getLayoutPosition() + "");
                ((MainActivity) context).startActivityForResult(intent, 1);
            }
        });

        tv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("position", getLayoutPosition() + "");
                ((MainActivity) context).startActivityForResult(intent, 1);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                //回复评论逻辑的处理
                final ListViewAdapter adapter = (ListViewAdapter) listView.getAdapter();
                final String item = adapter.getItem(position);
                if (item.contains("回复")) {
                    final QuickAction quickAction = new QuickAction(context, QuickAction.VERTICAL);
                    ActionItem Item = new ActionItem(1, "删除", null);
                    quickAction.addActionItem(Item);
                    quickAction.show(view);
                    quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
                        @Override
                        public void onItemClick(QuickAction source, int pos, int actionId) {
                            LogUtil.e("pos==" + pos);
                            adapter.deleteData(position);
                            //重新适配高度
                            changeHeight(adapter);

                            quickAction.dismiss();
                        }
                    });
                } else {
                    //评论回复
                    final QuickAction quickAction = new QuickAction(context, QuickAction.VERTICAL);
                    ActionItem Item1 = new ActionItem(1, "回复", null);
                    ActionItem Item2 = new ActionItem(1, "删除", null);
                    quickAction.addActionItem(Item1);
                    quickAction.addActionItem(Item2);
                    quickAction.show(view);
                    quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
                        @Override
                        public void onItemClick(QuickAction source, int pos, int actionId) {
                            switch (pos) {
                                case 0:
                                    //回复
                                    final InputDialog dialog = new InputDialog(context);
                                    dialog.show();
                                    dialog.ll_comment.btn_submit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String reply = dialog.ll_comment.et_content.getText().toString();
                                            dialog.ll_comment.et_content.setHint("回复 " + item);
                                            String str = "<font color='#29b6f6'>" + tv_name.getText().toString() + "</font>" +
                                                    "<font color='#606060'> 回复 </font>" +
                                                    "<font color='#29b6f6'>" + item + "</font>";
                                            adapter.insertData(reply, str, position + 1);
                                            dialog.dismiss();
                                            changeHeight(adapter);
                                        }
                                    });
                                    break;
                                case 1:
                                    //删除
                                    adapter.deleteData(position);
                                    for (int i = position; i < adapter.getCount(); i++) {
                                        if (adapter.getItem(i).contains("回复")) {
                                            adapter.deleteData(i);
                                        } else {
                                            break;
                                        }
                                    }
                                    changeHeight(adapter);
                                    break;
                                default:
                                    break;
                            }
                            quickAction.dismiss();
                        }
                    });
                }
            }
        });
    }

    /**
     * 根据listview中的item数量和高度改变listview的高度
     *
     * @param adapter
     */
    private void changeHeight(ListViewAdapter adapter) {

        int totalHeight = 0;

        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));

        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        listView.setLayoutParams(params);
    }
}
