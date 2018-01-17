package com.zhoukp.moments.model.db;

/**
 * auther：zhoukp
 * time：2018/1/16 11:18
 * mail：zhoukaiping@szy.cn
 * for：表格解耦股
 *
 * @author zhoukp
 */

public class BuDeJieTable {

    public static final String tabName = "BuDeJieTable";
    public static final String id = "id";
    public static final String subNumber = "subnumber";
    public static final String header = "header";
    public static final String name = "name";
    public static final String passTime = "passtime";
    public static final String type = "type";
    public static final String text = "text";
    public static final String downloadUrl = "download_url";
    public static final String download = "download";
    public static final String thumbnail = "thumbnail_small";

    public static final String createTable = "create table "
            + tabName + " ("
            + id + " text primary key,"
            + subNumber + " text,"
            + header + " text,"
            + name + " text,"
            + passTime + " text,"
            + type + " text,"
            + text + " text,"
            + downloadUrl + " text,"
            + download + " text,"
            + thumbnail + " text);";
}
