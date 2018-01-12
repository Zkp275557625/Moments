package com.zhoukp.moments.model;

import com.google.gson.Gson;
import com.zhoukp.moments.bean.NetDataBean;
import com.zhoukp.moments.utils.Constants;

/**
 * 作者：zhoukp
 * 时间：2018/1/9 14:30
 * 邮箱：zhoukaiping@szy.cn
 * 作用：
 */

public class MainPresenter {

    private IMainView iMainView;
    private MainActivityModel model;

    public MainPresenter() {
        this.model = new MainActivityModel();
    }

    /**
     * 绑定
     *
     * @param iMainView
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                model.loadData(Constants.DATA_URL, new MainActivityModel.Callback() {
                    @Override
                    public void onresponse(String msg) {
                        if (iMainView != null) {
                            NetDataBean netDataBean = processData(msg);
                            iMainView.loadDataSuccess(netDataBean);
                        }
                    }
                });
            }
        }).start();
    }

    private NetDataBean processData(String json) {
        return new Gson().fromJson(json, NetDataBean.class);
    }
}
