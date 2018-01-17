package com.zhoukp.moments.model;

import android.content.Context;

import com.google.gson.Gson;
import com.zhoukp.moments.bean.NetDataBean;
import com.zhoukp.moments.bean.TableDataBean;
import com.zhoukp.moments.model.db.DBManager;
import com.zhoukp.moments.model.thread.KpThreadFactory;
import com.zhoukp.moments.utils.CacheUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 时间：2018/1/9 14:20
 * 邮箱：zhoukaiping@szy.cn
 * 作用：
 *
 * @author zhoukp
 */

public class MainActivityModel {

    /**
     * 线程池
     */
    private ExecutorService executorService =
            new ThreadPoolExecutor(3, 5, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(128), new KpThreadFactory("zkp"));

    private static Context context;

    private static MainActivityModel model = new MainActivityModel();

    private DBManager manager;

    private MainActivityModel() {
    }

    public static MainActivityModel getInstance(Context ctx) {
        context = ctx;
        return model;
    }

    public ExecutorService getGloblThreadPool() {
        return executorService;
    }

    /**
     * 加载网络数据
     *
     * @param callback 回调
     */
    public void loadData(String url, Callback callback) {
        //网络请求
        URL requestUrl = null;
        try {
            requestUrl = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
            //设置GET请求
            connection.setRequestMethod("GET");
            //设置连接时间
            connection.setConnectTimeout(3000);
            //设置读取时间
            connection.setReadTimeout(3000);

            if (connection.getResponseCode() == 200) {
                //请求成功
                InputStream in = connection.getInputStream();
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                if (callback != null) {
                    //请求到数据后就解析成对应的bean对象
//                    CacheUtils.putString(context, "buDeJieData", sb.toString());
                    NetDataBean dataBean = processData(sb.toString());

                    if (manager != null){
                        manager.close();
                    }

                    manager = new DBManager(context, "zkp");
                    manager.getBuDeJieDao().addData(dataBean);

                    callback.onresponse(manager.getBuDeJieDao().getData());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface Callback {
//        /**
//         * 请求数据的回调
//         *
//         * @param bean bean对象
//         */
//        void onresponse(NetDataBean bean);
        void onresponse(ArrayList<TableDataBean> bean);
    }

    public NetDataBean processData(String json) {
        return new Gson().fromJson(json, NetDataBean.class);
    }
}
