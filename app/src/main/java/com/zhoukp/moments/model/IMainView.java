package com.zhoukp.moments.model;

import com.zhoukp.moments.bean.NetDataBean;

/**
 * 作者：zhoukp
 * 时间：2018/1/9 14:31
 * 邮箱：zhoukaiping@szy.cn
 * 作用：
 */

public interface IMainView {
    /**
     * 加载数据成功
     *
     * @param netDataBean 数据
     */
    void loadDataSuccess(NetDataBean netDataBean);
}
