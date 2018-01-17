package com.zhoukp.photo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.zhoukp.photo.R;
import com.zhoukp.photo.activity.adapter.PoiAdapter;
import com.zhoukp.photo.utils.LogUtil;

import java.util.List;

/**
 * auther：zhoukp
 * time：2018/1/15 11:48
 * mail：zhoukaiping@szy.cn
 * for：显示当前poi
 */

public class PoiActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    protected ImageView ivBack;
    protected EditText etSearch;
    protected TextView tvTitle, tvDone, tvHideLocation;
    protected ListView listView;

    protected PoiAdapter adapter;

    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    public PoiSearch mPoiSearch = null;
    protected String city;
    //纬度
    private double latitude;
    //经度
    private double longitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);

        initViews();

        initVariables();

        initEvent();
    }

    private void initViews() {
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("我的位置");
        tvDone = (TextView) findViewById(R.id.tvDone);
        tvDone.setVisibility(View.GONE);
        etSearch = (EditText) findViewById(R.id.etSearch);
        tvHideLocation = (TextView) findViewById(R.id.tvHideLocation);
        tvHideLocation.setSelected(true);
        listView = (ListView) findViewById(R.id.listView);
    }

    private void initVariables() {
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
    }

    private void initEvent() {
        ivBack.setOnClickListener(this);
        tvHideLocation.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        etSearch.addTextChangedListener(textWatcher);

        LocationClientOption option = new LocationClientOption();

        //可选，是否需要周边POI信息，默认为不需要，即参数为false
        //如果开发者需要获得周边POI信息，此处必须为true
        option.setIsNeedLocationPoiList(true);

        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true
        option.setIsNeedAddress(true);

        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
        mLocationClient.setLocOption(option);

        //mLocationClient为第二步初始化过的LocationClient对象
        //调用LocationClient的start()方法，便可发起定位请求
        mLocationClient.start();

        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                //关闭当前页面
                finish();
                break;
            case R.id.tvHideLocation:
                //不显示当前位置
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PoiAdapter.ViewHolder viewHolder = (PoiAdapter.ViewHolder) view.getTag();
        Intent intent = new Intent();
        intent.putExtra("poi", viewHolder.tvLocation.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //获取poi列表
            List<Poi> poiList = location.getPoiList();
            //获取城市
            city = location.getCity();
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            //POI附近检索参数设置类
            PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
            //搜索关键字，比如：银行、网吧、餐厅等
            nearbySearchOption.keyword("餐厅");
            //搜索的位置点
            nearbySearchOption.location(new LatLng(latitude, longitude));
            //搜索覆盖半径
            nearbySearchOption.radius(1000);
            //搜索类型，从近至远
            nearbySearchOption.sortType(PoiSortType.distance_from_near_to_far);
            //查询第几页：POI量可能会很多，会有分页查询
            nearbySearchOption.pageNum(10);
            //设置每页查询的个数，默认10个
            nearbySearchOption.pageCapacity(10);
            //查询
            mPoiSearch.searchNearby(nearbySearchOption);
        }
    }

    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult result) {
            //获取POI检索结果
            List<PoiInfo> allPoi = result.getAllPoi();
            if (allPoi != null) {
                adapter = new PoiAdapter(getApplicationContext(), allPoi);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(adapter);
                    }
                });
//                for (int i = 0; i < allPoi.size(); i++) {
//                    LogUtil.e(allPoi.get(i).name + "," + allPoi.get(i).uid + "," + allPoi.get(i).address + ","
//                            + allPoi.get(i).city + "," + allPoi.get(i).phoneNum + "," + allPoi.get(i).postCode + ","
//                            + allPoi.get(i).type + "," + allPoi.get(i).location.toString() + "," + allPoi.get(i).hasCaterDetails + ","
//                            + allPoi.get(i).isPano);
//                }
            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult result) {
            //获取Place详情页检索结果
        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
    };


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            //POI附近检索参数设置类
            PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
            //搜索关键字，比如：银行、网吧、餐厅等
            nearbySearchOption.keyword(etSearch.getText().toString());
            //搜索的位置点
            nearbySearchOption.location(new LatLng(latitude, longitude));
            //搜索覆盖半径
            nearbySearchOption.radius(1000);
            //搜索类型，从近至远
            nearbySearchOption.sortType(PoiSortType.distance_from_near_to_far);
            //查询第几页：POI量可能会很多，会有分页查询
            nearbySearchOption.pageNum(10);
            //设置每页查询的个数，默认10个
            nearbySearchOption.pageCapacity(10);
            //查询
            mPoiSearch.searchNearby(nearbySearchOption);
        }
    };
}
