package com.zhoukp.photo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhoukp.photo.R;
import com.zhoukp.photo.utils.CacheUtils;
import com.zhoukp.photo.utils.LogUtil;
import com.zhoukp.photo.view.Label;

import java.util.ArrayList;

/**
 * auther：zhoukp
 * time：2018/1/12 14:11
 * mail：zhoukaiping@szy.cn
 * for：发布朋友圈页面
 */

public class PublishActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout LlLabel;
    private TextView tvTitle, tvDone;

    private ArrayList<String> labelName;
    private ArrayList<Integer> gridLayout_selected;
    private ArrayList<Integer> selected;
    private ArrayList<String> gridLayout_labelName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViews();
    }

    private void initViews() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("发布相册");
        tvDone = (TextView) findViewById(R.id.tvDone);
        tvDone.setText("发送");

        LlLabel = (LinearLayout) findViewById(R.id.LlLabel);
        LlLabel.removeAllViews();
        LlLabel.addView(newAddItem());

        gridLayout_selected = CacheUtils.getArrayList(this, "KpGridLayout");
        gridLayout_labelName = CacheUtils.getArrayListStr(this, "KpGridLayout_labelName");

        for (int i = 0; i < gridLayout_selected.size(); i++) {
            LogUtil.e(gridLayout_selected.get(i) + "");
            Label label = newItemView();
            label.tvText.setText(gridLayout_labelName.get(gridLayout_selected.get(i)));
            LlLabel.addView(label, 0);
            LogUtil.e(gridLayout_labelName.get(gridLayout_selected.get(i)));
        }

        selected = CacheUtils.getArrayList(this, "gridLayoutAdd");
        labelName = CacheUtils.getArrayListStr(this, "gridLayoutAdd_labelName");

        for (int i = 0; i < selected.size(); i++) {
            LogUtil.e(selected.get(i) + "");
            Label label = newItemView();
            label.tvText.setText(labelName.get(selected.get(i)));
            LlLabel.addView(label, 0);
        }
    }

    /**
     * 创建一个新的item
     *
     * @return TextView
     */
    private Label newItemView() {
        final Label label = new Label(this);
        label.tvText.setPadding(20, 10, 20, 10);
        label.ivDelete.setVisibility(View.VISIBLE);
        label.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //从视图中移除label
                LlLabel.removeView(label);
                //从存储中移除选中的index
                Integer indexAdd = -1;
                for (int i = 0; i < gridLayout_labelName.size(); i++) {
                    if (gridLayout_labelName.get(i).equals(label.tvText.getText().toString())) {
                        indexAdd = i;
                        break;
                    }
                }
                if (gridLayout_selected.contains(indexAdd)) {
                    gridLayout_selected.remove(indexAdd);
                }
                Integer index = -1;
                for (int i = 0; i < labelName.size(); i++) {
                    if (labelName.get(i).equals(label.tvText.getText().toString())) {
                        index = i;
                        break;
                    }
                }
                if (selected.contains(index)) {
                    selected.remove(index);
                }
            }
        });
        return label;
    }

    private TextView newAddItem() {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(40, 30, 0, 0);
        textView.setLayoutParams(params);
        textView.setPadding(20, 10, 20, 10);
        textView.setGravity(Gravity.CENTER);

        textView.setBackgroundResource(R.drawable.add_label_bg);
        textView.setTextSize(12);
        textView.setTextColor(getResources().getColor(R.color.colorBlack, null));
        textView.setText(" + 新建标签");

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PublishActivity.this, MainActivity.class);
                intent.putExtra("KpGridLayout", arrayListToString(gridLayout_selected));
                intent.putExtra("gridLayoutAdd", arrayListToString(selected));
                intent.putExtra("gridLayoutAdd_labelName", arrayListToStringStr(labelName));
                startActivity(intent);
            }
        });
        return textView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            default:
                break;
        }
    }

    /**
     * 链表转换为字符串
     *
     * @param arrayList 链表
     * @return
     */
    private String arrayListToStringStr(ArrayList<String> arrayList) {
        String result = "";
        for (int i = 0; i < arrayList.size(); i++) {
            result += arrayList.get(i) + "##";
        }
        return result;
    }

    /**
     * 链表转换为字符串
     *
     * @param arrayList 链表
     * @return
     */
    private String arrayListToString(ArrayList<Integer> arrayList) {
        String result = "";
        for (int i = 0; i < arrayList.size(); i++) {
            result += arrayList.get(i) + "##";
        }
        return result;
    }
}
