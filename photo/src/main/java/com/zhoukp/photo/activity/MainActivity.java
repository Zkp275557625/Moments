package com.zhoukp.photo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhoukp.photo.R;
import com.zhoukp.photo.utils.CacheUtils;
import com.zhoukp.photo.view.KpGridLayout;
import com.zhoukp.photo.view.KpGridLayoutAdd;

import java.util.ArrayList;

/**
 * @author zhoukp
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private KpGridLayout gridLayout;
    private KpGridLayoutAdd gridLayoutAdd;
    public Button btnSumbit;
    private TextView tvDone, tvNoLabel;

    private ArrayList<String> texts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViews();
    }

    private void initViews() {
        gridLayout = (KpGridLayout) findViewById(R.id.gridLayout);
        gridLayoutAdd = (KpGridLayoutAdd) findViewById(R.id.gridLayoutAdd);
        btnSumbit = (Button) findViewById(R.id.btnSumbit);
        btnSumbit.setSelected(true);
        btnSumbit.setEnabled(true);
        tvDone = (TextView) findViewById(R.id.tvDone);
        tvDone.setOnClickListener(this);
        btnSumbit.setOnClickListener(this);
        tvNoLabel = (TextView) findViewById(R.id.tvNoLabel);
        tvNoLabel.setOnClickListener(this);

        texts = new ArrayList<>();
        texts.add("下雪啦");
        texts.add("元旦快乐");
        texts.add("掌通家园");
        texts.add("过年啦");
        texts.add("放假啦");
        texts.add("下班啦");
        gridLayout.setItems(texts);
        Intent intent = getIntent();
        String[] split = intent.getStringExtra("KpGridLayout").split("##");
        gridLayout.setSelectes(split);
        String[] splitAddName = intent.getStringExtra("gridLayoutAdd_labelName").split("##");
        gridLayoutAdd.addItems(splitAddName);
        String[] splitAdd = intent.getStringExtra("gridLayoutAdd").split("##");
        gridLayoutAdd.setSelectes(splitAdd);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvDone:
                if (tvDone.getText().toString().equals("完成")) {
                    tvDone.setText("编辑");
                    btnSumbit.setSelected(true);
                    btnSumbit.setEnabled(true);
                    //隐藏添加标签按钮
                    gridLayoutAdd.hideAddItemLabel(true);
                    //不可删除
                    gridLayoutAdd.unDelete(true);
                    //显示选中
                    gridLayoutAdd.showSelected(true);
                } else {
                    tvDone.setText("完成");
                    btnSumbit.setSelected(false);
                    btnSumbit.setEnabled(false);
                    //可删除
                    gridLayoutAdd.unDelete(false);
                    //不显示选中
                    gridLayoutAdd.showSelected(false);
                    //显示添加标签按钮
                    gridLayoutAdd.hideAddItemLabel(false);
                }
                break;
            case R.id.btnSumbit:
                //带回逻辑的处理
                startActivity(new Intent(MainActivity.this, PublishActivity.class));
                finish();
                break;
            case R.id.tvNoLabel:
                //清空所有已选标签
                CacheUtils.putArrayList(MainActivity.this, "KpGridLayout", new ArrayList<String>());
                CacheUtils.putArrayList(MainActivity.this, "gridLayoutAdd", new ArrayList<String>());
                finish();
                break;
            default:
                break;
        }
    }
}
