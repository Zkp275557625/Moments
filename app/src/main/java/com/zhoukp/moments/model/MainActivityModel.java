package com.zhoukp.moments.model;

import com.zhoukp.moments.utils.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 作者：zhoukp
 * 时间：2018/1/9 14:20
 * 邮箱：zhoukaiping@szy.cn
 * 作用：
 */

public class MainActivityModel {
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

                LogUtil.e(sb.toString());

                if (callback != null) {
                    callback.onresponse(sb.toString());
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface Callback {
        void onresponse(String msg);
    }
}
