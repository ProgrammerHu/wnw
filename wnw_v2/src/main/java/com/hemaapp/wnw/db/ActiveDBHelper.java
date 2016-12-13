package com.hemaapp.wnw.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 */
public class ActiveDBHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "ActiveAndroid.db";
    /**
     * 系统初始化信息
     */
    protected static final String SYSINITINFO = "SysInitInfo";
    /**
     * 当前登录用户信息
     */
    protected static final String USER = "User";
    /**
     * 搜索内容缓存信息
     */
    protected static final String SEARCH_LOG = "SearchLog";
    /**
     * 缓存界面数据实体的json
     */
    protected static final String CacheModel = "CacheModel";
    /**
     * 数据库版本号
     */
    protected static final int VERSION = 1;

    public ActiveDBHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlitedatabase, int oldVersion, int newVersion) {


    }

}
