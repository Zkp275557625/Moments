package com.zhoukp.moments.model.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhoukp.moments.bean.NetDataBean;
import com.zhoukp.moments.bean.TableDataBean;
import com.zhoukp.moments.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * auther：zhoukp
 * time：2018/1/16 11:17
 * mail：zhoukaiping@szy.cn
 * for：数据库表格操作类
 *
 * @author zhoukp
 */

public class BuDeJieDao {
    private final DBHelper dbHelper;

    public BuDeJieDao(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * 把网络请求的数据更新到数据库表格中
     *
     * @param bean bean
     */
    public void addData(NetDataBean bean) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<NetDataBean.ListEntity> list = bean.getList();
        ContentValues values;
        for (int i = 0; i < list.size(); i++) {
            values = new ContentValues();
            values.put(BuDeJieTable.id, list.get(i).getId());
            values.put(BuDeJieTable.subNumber, list.get(i).getUp());
            values.put(BuDeJieTable.header, list.get(i).getU().getHeader().get(0));
            values.put(BuDeJieTable.name, list.get(i).getU().getName());
            values.put(BuDeJieTable.passTime, list.get(i).getPasstime());
            values.put(BuDeJieTable.type, list.get(i).getType());
            values.put(BuDeJieTable.text, list.get(i).getText());
            switch (list.get(i).getType()) {
                case "text":
                    break;
                case "image":
                    values.put(BuDeJieTable.downloadUrl, list.get(i).getImage().getDownload_url().get(0));
                    break;
                case "video":
                    values.put(BuDeJieTable.download, list.get(i).getVideo().getDownload().get(0));
                    values.put(BuDeJieTable.thumbnail, list.get(i).getVideo().getThumbnail().get(0));
                    break;
                default:
                    break;
            }
            db.replace(BuDeJieTable.tabName, null, values);
        }
        //关闭数据库
        db.close();
    }

    public ArrayList<TableDataBean> getData() {
        ArrayList<TableDataBean> result = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select * from " + BuDeJieTable.tabName;
        Cursor cursor = db.rawQuery(sql, null);
        TableDataBean bean = null;
        while (cursor.moveToNext()) {
            bean = new TableDataBean();
            bean.setId(cursor.getString(cursor.getColumnIndex(BuDeJieTable.id)));
            bean.setSubNumber(cursor.getString(cursor.getColumnIndex(BuDeJieTable.subNumber)));
            bean.setHeader(cursor.getString(cursor.getColumnIndex(BuDeJieTable.header)));
            bean.setName(cursor.getString(cursor.getColumnIndex(BuDeJieTable.name)));
            bean.setText(cursor.getString(cursor.getColumnIndex(BuDeJieTable.text)));
            bean.setType(cursor.getString(cursor.getColumnIndex(BuDeJieTable.type)));
            switch (cursor.getString(cursor.getColumnIndex(BuDeJieTable.type))) {
                case "text":
                    break;
                case "image":
                    bean.setDownloadUrl(cursor.getString(cursor.getColumnIndex(BuDeJieTable.downloadUrl)));
                    break;
                case "video":
                    bean.setDownload(cursor.getString(cursor.getColumnIndex(BuDeJieTable.download)));
                    bean.setThumbnail(cursor.getString(cursor.getColumnIndex(BuDeJieTable.thumbnail)));
                    break;
                default:
                    break;
            }
            LogUtil.e(bean.toString());
            result.add(bean);
        }
        //关闭游标
        cursor.close();
        //关闭数据库
        db.close();
        return result;
    }
}
