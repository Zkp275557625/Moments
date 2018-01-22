package com.zhoukp.photo.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhoukp.photo.R;
import com.zhoukp.photo.activity.adapter.ContactsSortAdapter;
import com.zhoukp.photo.activity.adapter.ContactsSelectedAdapter;
import com.zhoukp.photo.bean.SortModel;
import com.zhoukp.photo.utils.LogUtil;
import com.zhoukp.photo.utils.PinYinUtils;
import com.zhoukp.photo.utils.pinyin.CharacterParser;
import com.zhoukp.photo.utils.pinyin.PinyinComparator;
import com.zhoukp.photo.view.SideBar;

import java.util.ArrayList;
import java.util.Collections;

/**
 * time：2018/1/22 8:33
 * mail：zhoukaiping@szy.cn
 * for：联系人列表
 *
 * @author zhoukp
 */

public class ContactFragment extends BaseFragment {

    protected SideBar sideBar;
    protected TextView dialog;
    protected EditText etSearch;
    protected ListView lvContacts;
    protected LinearLayout llContacts;
    protected RecyclerView recyclerView;
    protected Button btnSumbit, btnCancle;

    /**
     * 汉字转换成拼音的类
     */
    protected CharacterParser parser;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    protected PinyinComparator comparator;
    /**
     * 所有的联系人数据
     */
    protected ArrayList<SortModel> contacts;

    protected String[] names = {
            "陈阿三", "多多", "方圆", "胡成", "黄蓉",
            "何阿三", "李明", "刘杰", "妹妹", "潘岳华",
            "苏阿三", "邰阿四", "王五", "王宝强", "小小",
            "小明", "谢霆锋", "杨过", "叶剑英", "张三"};

    private ContactsSortAdapter adapter;
    private ContactsSelectedAdapter selectedAdapter;
    protected ArrayList<SortModel> contactSelected;

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.fragment_contact, null);
        sideBar = (SideBar) view.findViewById(R.id.sideBar);
        dialog = (TextView) view.findViewById(R.id.dialog);
        sideBar.setTextDialog(dialog);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        lvContacts = (ListView) view.findViewById(R.id.lvContacts);
        llContacts = (LinearLayout) view.findViewById(R.id.llContacts);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        btnSumbit = (Button) view.findViewById(R.id.btnSumbit);
        btnCancle = (Button) view.findViewById(R.id.btnCancle);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        parser = CharacterParser.getInstance();
        comparator = new PinyinComparator();
        contacts = new ArrayList<>();
        contactSelected = new ArrayList<>();

        for (String name : names) {
            SortModel model = new SortModel(name, "", PinYinUtils.getPinYinHeadChar(name));
            model.sortLetters = PinYinUtils.getPinYinHeadChar(name);
            model.sortToken = PinYinUtils.parseSortKey(name);
            contacts.add(model);
        }

        //根据a-z进行排序源数据
        Collections.sort(contacts, comparator);

        adapter = new ContactsSortAdapter(context, contacts);
        lvContacts.setAdapter(adapter);

        contactSelected = getSelectedItem(contacts, contactSelected);
        selectedAdapter = new ContactsSelectedAdapter(context, contactSelected);

        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayout.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(selectedAdapter);

        initEvent();
    }

    private void initEvent() {
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String str) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(str.charAt(0));
                if (position != -1) {
                    lvContacts.setSelection(position);
                }
            }
        });

        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.getItem(position).setSelected(!adapter.getItem(position).isSelected);
                adapter.notifyDataSetChanged();

                contactSelected = getSelectedItem(contacts, contactSelected);

                if (contactSelected.size() > 0) {
                    llContacts.setVisibility(View.VISIBLE);
                } else {
                    llContacts.setVisibility(View.GONE);
                }

                selectedAdapter.notifyDataSetChanged();
            }
        });

        btnSumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //确定逻辑的处理
                Intent intent = new Intent();
                String names = getNames(contactSelected);
                intent.putExtra("object", names);
                context.setResult(Activity.RESULT_OK, intent);
                context.finish();
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //重置匹配信息
                for (int i = 0; i < contacts.size(); i++) {
                    contacts.get(i).setShow(false);
                }

                String match = etSearch.getText().toString();
                LogUtil.e("文字变化了" + match);

                if (editable.length() > 0) {
                    for (int i = 0; i < contacts.size(); i++) {
                        if (contacts.get(i).getName().contains(match)) {
                            contacts.get(i).setShow(true);
                        }
                    }
                } else {
                    for (int i = 0; i < contacts.size(); i++) {
                        contacts.get(i).setShow(false);
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

                    for (int i = 0; i < contacts.size(); i++) {
                        contacts.get(i).setShow(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    // 失去焦点
                    LogUtil.e("失去焦点");
                    btnCancle.setVisibility(View.GONE);

                    for (int i = 0; i < contacts.size(); i++) {
                        contacts.get(i).setShow(true);
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
    }

    private ArrayList<SortModel> getSelectedItem(ArrayList<SortModel> contacts, ArrayList<SortModel> result) {
        result.clear();
        for (SortModel model : contacts) {
            if (model.isSelected) {
                result.add(model);
            }
        }
        return result;
    }

    private String getNames(ArrayList<SortModel> contacts) {
        String result = "";

        for (SortModel model : contacts) {
            result += model.getName() + ",";
        }

        return result.substring(0, result.length() - 1);
    }
}
