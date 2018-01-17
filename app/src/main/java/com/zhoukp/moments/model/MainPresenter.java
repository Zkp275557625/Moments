package com.zhoukp.moments.model;

import android.content.Context;
import android.text.TextUtils;

import com.zhoukp.moments.bean.NetDataBean;
import com.zhoukp.moments.bean.TableDataBean;
import com.zhoukp.moments.model.db.DBManager;
import com.zhoukp.moments.utils.CacheUtils;
import com.zhoukp.moments.utils.Constants;

import java.util.ArrayList;

/**
 * 作者：zhoukp
 * 时间：2018/1/9 14:30
 * 邮箱：zhoukaiping@szy.cn
 * 作用：
 *
 * @author zhoukp
 */

public class MainPresenter {

    private IMainView iMainView;
    private MainActivityModel model;
    protected Context context;
    private DBManager manager;

    public MainPresenter(Context context) {
        this.context = context;
        this.model = MainActivityModel.getInstance(context);
    }

    /**
     * 绑定
     *
     * @param iMainView mianActivity
     */
    public void attach(IMainView iMainView) {
        this.iMainView = iMainView;
    }

    /**
     * 解除绑定
     */
    public void detach() {
        this.iMainView = null;
    }

    public void loadData() {
        if (manager != null) {
            manager.close();
        }

        manager = new DBManager(context, "zkp");
        ArrayList<TableDataBean> been = manager.getBuDeJieDao().getData();

        if (been != null && been.size() > 0) {
            iMainView.loadDataSuccess2(been);
        } else {
            getDataFromNet();
        }

//        String data = CacheUtils.getString(context, "buDeJieData");
//
//        if (!TextUtils.isEmpty(data)) {
//            //本地有数据，直接从本地加载数据
//            iMainView.loadDataSuccess(model.processData(data));
//        } else {
//            getDataFromNet();
//        }
    }

    /**
     * 从网络请求数据
     */
    private void getDataFromNet() {
        model.getGloblThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                model.loadData(Constants.DATA_URL, new MainActivityModel.Callback() {
                    @Override
                    public void onresponse(ArrayList<TableDataBean> bean) {
                        iMainView.loadDataSuccess2(bean);
                    }
//                    @Override
//                    public void onresponse(NetDataBean bean) {
//                        if (iMainView != null) {
//                            iMainView.loadDataSuccess(bean);
//                        }
//                    }
                });
            }
        });
    }
}
