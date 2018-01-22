package com.zhoukp.photo.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhoukp.photo.R;
import com.zhoukp.photo.activity.adapter.ClassListAdapter;
import com.zhoukp.photo.bean.ClassInfo;
import com.zhoukp.photo.bean.ClassesInfo;
import com.zhoukp.photo.utils.LogUtil;

import java.util.ArrayList;

/**
 * time：2018/1/16 15:16
 * mail：zhoukaiping@szy.cn
 * for：
 *
 * @author zhoukp
 */

public class ClassFragment extends BaseFragment {

    protected EditText etSearch;
    protected Button btnCancle, btnSumbit;
    protected ExpandableListView listView;
    private LinearLayout llClasses;
    private TextView tvClasses;

    private ArrayList<ClassInfo> groups;
    protected ClassListAdapter adapter;
    protected String result = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String text = (String) msg.obj;
            if (text.length() > 0) {
                llClasses.setVisibility(View.VISIBLE);
                tvClasses.setText(text.substring(0, text.length() - 1));
            } else {
                llClasses.setVisibility(View.GONE);
                tvClasses.setText(text);
            }
        }
    };

    public Handler getHandler() {
        return handler;
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.fragment_class, null);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        btnCancle = (Button) view.findViewById(R.id.btnCancle);
        listView = (ExpandableListView) view.findViewById(R.id.listView);
        llClasses = (LinearLayout) view.findViewById(R.id.llClasses);
        tvClasses = (TextView) view.findViewById(R.id.tvClasses);
        btnSumbit = (Button) view.findViewById(R.id.btnSumbit);
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        ClassInfo classInfo1 = new ClassInfo();
        classInfo1.setGroupName("大班");
        classInfo1.setSelected(false);
        classInfo1.setMatched(true);
        ArrayList<ClassesInfo> children1 = new ArrayList<>();
        children1.add(new ClassesInfo("大一班", false, true));
        children1.add(new ClassesInfo("大二班", false, true));
        children1.add(new ClassesInfo("大三班", false, true));
        children1.add(new ClassesInfo("大四班", false, true));
        children1.add(new ClassesInfo("大五班", false, true));
        children1.add(new ClassesInfo("大六班", false, true));
        classInfo1.setChildren(children1);

        ClassInfo classInfo2 = new ClassInfo();
        classInfo2.setGroupName("小班");
        classInfo2.setSelected(false);
        classInfo2.setMatched(true);
        ArrayList<ClassesInfo> children2 = new ArrayList<>();
        children2.add(new ClassesInfo("小一班", false, true));
        children2.add(new ClassesInfo("小二班", false, true));
        children2.add(new ClassesInfo("小三班", false, true));
        children2.add(new ClassesInfo("小四班", false, true));
        children2.add(new ClassesInfo("小五班", false, true));
        classInfo2.setChildren(children2);

        groups = new ArrayList<>();
        groups.add(classInfo1);
        groups.add(classInfo2);

        adapter = new ClassListAdapter(context, groups);
        listView.setAdapter(adapter);

        initEvent();
    }

    private void initEvent() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //在文字变化后回调该方法
                //重置匹配信息
                for (int i = 0; i < groups.size(); i++) {
                    groups.get(i).setMatched(true);
                    for (int j = 0; j < groups.get(i).getChildren().size(); j++) {
                        groups.get(i).getChildren().get(j).setMatched(true);
                    }
                }

                String match = etSearch.getText().toString();
                LogUtil.e("文字变化了" + match);

                if (editable.length() > 0) {
                    for (int i = 0; i < groups.size(); i++) {
                        for (int j = 0; j < groups.get(i).getChildren().size(); j++) {
                            if (!groups.get(i).getChildren().get(j).getName().contains(match)) {
                                groups.get(i).getChildren().get(j).setMatched(false);
                                groups.get(i).setMatched(false);
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < groups.size(); i++) {
                        groups.get(i).setMatched(false);
                        for (int j = 0; j < groups.get(i).getChildren().size(); j++) {
                            groups.get(i).getChildren().get(j).setMatched(false);
                        }
                    }
                }
                //刷新数据适配器
                adapter.notifyDataSetChanged();
            }
        });

        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置输入框可聚集
                etSearch.setFocusable(true);
                //设置触摸聚焦
                etSearch.setFocusableInTouchMode(true);
                //请求焦点
                etSearch.requestFocus();
                //获取焦点
                etSearch.findFocus();
            }
        });

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点
                    LogUtil.e("获得焦点");
                    btnCancle.setVisibility(View.VISIBLE);

                    for (int i = 0; i < groups.size(); i++) {
                        groups.get(i).setMatched(false);
                        for (int j = 0; j < groups.get(i).getChildren().size(); j++) {
                            groups.get(i).getChildren().get(j).setMatched(false);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    // 失去焦点
                    LogUtil.e("失去焦点");
                    btnCancle.setVisibility(View.GONE);

                    for (int i = 0; i < groups.size(); i++) {
                        groups.get(i).setMatched(true);
                        for (int j = 0; j < groups.get(i).getChildren().size(); j++) {
                            groups.get(i).getChildren().get(j).setMatched(true);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置输入框不可聚焦，即失去焦点和光标
                etSearch.setFocusable(false);
            }
        });

        //对group中child点击的监听
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
                boolean selected = adapter.getGroup(groupPosition).getChildren().get(childPosition).isSelected();
                adapter.getGroup(groupPosition).getChildren().get(childPosition).setSelected(!selected);
                LogUtil.e(adapter.getGroup(groupPosition).getChildren().get(childPosition).isSelected() + "");
                //更新该组选中状态
                boolean selectedGroup = true;
                for (int i = 0; i < adapter.getGroup(groupPosition).getChildren().size(); i++) {
                    selectedGroup = selectedGroup && adapter.getGroup(groupPosition).getChildren().get(i).isSelected();
                }
                adapter.getGroup(groupPosition).setSelected(selectedGroup);

                adapter.notifyDataSetChanged();
                //清空字符串
                result = "";
                //拼凑字符串
                for (int i = 0; i < adapter.getGroupCount(); i++) {
                    ClassInfo group = adapter.getGroup(i);
                    if (group.isSelected()) {
                        //该组全部被选中
                        result += group.getGroupName() + ":";
                        for (int j = 0; j < group.getChildren().size(); j++) {
                            result += group.getChildren().get(j).getName() + ",";
                        }
                    } else {
                        //该组未全部选中，找出被选中的孩子
                        for (int j = 0; j < group.getChildren().size(); j++) {
                            if (group.getChildren().get(j).isSelected()) {
                                //若该孩子被选中
                                result += group.getChildren().get(j).getName() + ",";
                            }
                        }
                    }
                }
                Message msg = new Message();
                msg.obj = result;
                getHandler().sendMessage(msg);
                return false;
            }
        });

        adapter.setListener(new ClassListAdapter.onGroupSelectedListener() {
            @Override
            public void onGroupSelectedListener(int groupPosition) {
                //选中某个组
                adapter.getGroup(groupPosition).setSelected(!adapter.getGroup(groupPosition).isSelected());
                ClassInfo group = adapter.getGroup(groupPosition);
                for (int i = 0; i < group.getChildren().size(); i++) {
                    adapter.getChild(groupPosition, i).setSelected(adapter.getGroup(groupPosition).isSelected());
                }

                adapter.notifyDataSetChanged();
                //清空字符串
                result = "";
                //拼凑字符串
                for (int i = 0; i < adapter.getGroupCount(); i++) {
                    group = adapter.getGroup(i);
                    if (group.isSelected()) {
                        //该组全部被选中
                        result += group.getGroupName() + ":";
                        for (int j = 0; j < group.getChildren().size(); j++) {
                            result += group.getChildren().get(j).getName() + ",";
                        }
                    } else {
                        //该组未全部选中，找出被选中的孩子
                        for (int j = 0; j < group.getChildren().size(); j++) {
                            if (group.getChildren().get(j).isSelected()) {
                                //若该孩子被选中
                                result += group.getChildren().get(j).getName() + ",";
                            }
                        }
                    }
                }
                Message msg = new Message();
                msg.obj = result;
                getHandler().sendMessage(msg);
            }
        });

        btnSumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //确定按钮的逻辑处理
                String text = tvClasses.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("object", text);
                context.setResult(Activity.RESULT_OK, intent);
                context.finish();
            }
        });
    }
}
