package com.zhoukp.photo.utils.pinyin;

import com.zhoukp.photo.bean.SortModel;

import java.util.Comparator;

/**
 * @author zhoukp
 */
public class PinyinComparator implements Comparator<SortModel> {

    @Override
    public int compare(SortModel o1, SortModel o2) {
        if (o1.sortLetters.equals("@") || o2.sortLetters.equals("#")) {
            return -1;
        } else if (o1.sortLetters.equals("#") || o2.sortLetters.equals("@")) {
            return 1;
        } else {
            return o1.sortLetters.compareTo(o2.sortLetters);
        }
    }

}
