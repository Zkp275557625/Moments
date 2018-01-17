package com.zhoukp.moments.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * @author 周开平
 * @date 2017/3/25 16:11
 * 作用：缓存数据
 */

public class CacheUtils {
    /**
     * 得到缓存值
     *
     * @param context 上下文
     * @param key     key
     * @return boolean
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("HQUZkP", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    /**
     * @param context 上下文
     * @param key     key
     * @param value   vlaue
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("HQUZkP", Context.MODE_PRIVATE);
        LogUtil.e(key + "==" + value);
        sp.edit().putBoolean(key, value).apply();
    }

    /**
     * 缓存文本数据
     *
     * @param context 上下文
     * @param key     key
     * @param value   value
     */
    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("HQUZkP", Context.MODE_PRIVATE);
        LogUtil.e(key + "==" + value);
        sp.edit().putString(key, value).apply();
    }

    /**
     * 获取缓存的文本信息
     *
     * @param context 上下文
     * @param key     key
     * @return String
     */
    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("HQUZkP", Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    /**
     * 缓存链表数据
     *
     * @param context 上下文
     * @param key     key
     * @param value   value
     */
    public static void putArrayList(Context context, String key, ArrayList<String> value) {
        String result = "";
        for (int i = 0; i < value.size(); i++) {
            result += value.get(i) + "##";
        }
        putString(context, key, result);
    }

    /**
     * 获取链表数据
     *
     * @param context 上下文
     * @param key     key
     * @return ArrayList
     */
    public static ArrayList<Integer> getArrayList(Context context, String key) {
        String value = getString(context, key);
        LogUtil.e("value==" + value);
        ArrayList<Integer> result = new ArrayList<>();
        if (!TextUtils.isEmpty(value)) {
            String[] split = value.split("##");
            for (int i = 0; i < split.length; i++) {
                result.add(Integer.valueOf(split[i]));
            }
        }
        return result;
    }

    /**
     * 缓存链表数据
     *
     * @param context 上下文
     * @param key     key
     * @param value   value
     */
    public static void putArrayListStr(Context context, String key, ArrayList<String> value) {
        String result = "";
        for (int i = 0; i < value.size(); i++) {
            result += value.get(i) + "##";
        }
        putString(context, key, result);
    }

    /**
     * 获取链表数据
     *
     * @param context 上下文
     * @param key     key
     * @return ArrayList
     */
    public static ArrayList<String> getArrayListStr(Context context, String key) {
        String value = getString(context, key);
        LogUtil.e(value);
        String[] split = value.split("##");
        ArrayList<String> result = new ArrayList<>();

        for (int i = 0; i < split.length; i++) {
            result.add(split[i]);
        }
        return result;
    }

    /**
     * 根据key删除对应的缓存数据
     *
     * @param context 上下文
     * @param key     key
     */
    public static void clearSp(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("HQUZkP", Context.MODE_PRIVATE);
        sp.edit().remove(key).apply();
    }
}
