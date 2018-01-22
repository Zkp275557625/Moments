package com.zhoukp.photo.utils;

import com.zhoukp.photo.bean.SortToken;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * time：2018/1/22 10:30
 * mail：zhoukaiping@szy.cn
 * for：
 *
 * @author zhoukp
 */

public class PinYinUtils {
    /**
     * 得到中文首字母
     *
     * @param str 需要转化的中文字符串
     * @return String
     */
    public static String getPinYinHeadChar(String str) {
        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert.substring(0, 1).toUpperCase();
    }

    /**
     * 解析sort_key,封装简拼,全拼
     *
     * @param sortKey key
     * @return SortToken
     */
    public static SortToken parseSortKey(String sortKey) {
        SortToken token = new SortToken();
        if (sortKey != null && sortKey.length() > 0) {
            // 其中包含的中文字符
            String[] enStrs = sortKey.replace(" ", "").split("[\\u4E00-\\u9FA5]+");
            for (int i = 0, length = enStrs.length; i < length; i++) {
                if (enStrs[i].length() > 0) {
                    // 拼接简拼
                    token.simpleSpell += enStrs[i].charAt(0);
                    token.wholeSpell += enStrs[i];
                }
            }
        }
        return token;
    }
}
