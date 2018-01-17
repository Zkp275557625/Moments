package com.zhoukp.moments.model.db;

import android.content.Context;

/**
 * auther：zhoukp
 * time：2018/1/16 10:58
 * mail：zhoukaiping@szy.cn
 * for：DBManager
 *
 * @author zhoukp
 */

public class DBManager {

    private final DBHelper dbHelper;
    private final BuDeJieDao buDeJieDao;

    public DBManager(Context context, String name) {
        dbHelper = new DBHelper(context, name);
        buDeJieDao = new BuDeJieDao(dbHelper);
    }

    public BuDeJieDao getBuDeJieDao() {
        return buDeJieDao;
    }

    /**
     * 关闭数据库连接
     */
    public void close() {
        dbHelper.close();
    }
}
