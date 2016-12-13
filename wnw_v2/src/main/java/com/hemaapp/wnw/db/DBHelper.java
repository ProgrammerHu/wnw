package com.hemaapp.wnw.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "vwow_v2000.db";
    /**
     * 系统初始化信息
     */
    protected static final String SYSINITINFO = "sysinfo";
    /**
     * 当前登录用户信息
     */
    protected static final String USER = "user";
    /**
     * 访问城市缓存信息
     */
    protected static final String VISIT_CITYS = "visit_citys";
    /**
     * 缓存注册信息
     */
    protected static final String REGISTER_INFO = "register";
    /**
     * 搜索内容缓存信息
     */
    protected static final String SEARCH_LOG = "search_log";
    /**
     * 数据库版本号
     */
    protected static final int VERSION = 1;
    /*
    * 2016年8月31日 将数据库版本升级为2 ，user表添加major,like两字段
    * */

    public DBHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sys = "sys_web_service text,sys_plugins text,sys_show_iospay text,sys_chat_ip text,sys_chat_port text,"
                + "android_must_update text,android_last_version text,iphone_must_update text,iphone_last_version text,sys_pagesize int,sys_service_phone text,"
                + "android_update_url text,iphone_update_url text,iphone_comment_url text,msg_invite text,qrcode_memo text,goods_memo text,group_memo text," +
                "start_img text,info text,logo text";
        String sysSQL = "create table " + SYSINITINFO
                + " (id integer primary key," + sys + ")";
        // 创建系统初始化信息缓存表
        db.execSQL(sysSQL);
        // 创建当前登录用户信息缓存表
        String user = "id text,username text,inviter_code text,email text,password text,paypassword text,nickname text,feeaccount text,sex text,age text," +
                "mobile text,avatar text,avatarbig text,district_name text,onlineflag text,validflag text,lng text,lat text,deviceid text,devicetype text," +
                "channelid text,lastloginversion text,lastlogintime text,content text,regdate text,bankuser text,bankname text,bankcard text,bankaddress text," +
                "alipay text,realname text,thirdtype text,thirduid text,birthday text,communityid text,inviter text,commision text,tcommision text,qrcode text," +
                "team_xiaofei text,rlevel text,upline text,fans text,flag text,edit_flag text,token text,android_must_update text,android_last_version text,android_update_url text," +
                "major text, like text";
        String userSQL = "create table " + USER + " (" + user + ")";
        db.execSQL(userSQL);
        // 创建访问城市缓存表
        String citys = "id text primary key,name text,parentid text,nodepath text,"
                + "namepath text,charindex text,level text,orderby text";
        String citysSQL = "create table " + VISIT_CITYS + " (" + citys + ")";
        db.execSQL(citysSQL);

        //创建注册信息缓存表
        String registers = "temp_token text,college_id text,intoyear text,outyear text,username text,password text,"
                + "nickname text,recom_mobile text,realname text,sex text, imagehead text,image_student1 text,image_student2 text, college_name text, school_name text";
        String registerSQL = "create table " + REGISTER_INFO + "(" + registers + ")";
        db.execSQL(registerSQL);

        //创建搜索内容缓存表
        String search = "id integer(4) PRIMARY KEY, type varchar(4), content text, searchdate datetime";
        String searchSQL = "create table " + SEARCH_LOG + " (" + search + ")";
        db.execSQL(searchSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlitedatabase, int oldVersion, int newVersion) {
//        if (oldVersion <= 1) {
//            String alterSQL = "alter table " + USER + " ADD major text";
//            sqlitedatabase.execSQL(alterSQL);
//            alterSQL = "alter table " + USER + " ADD like text";
//            sqlitedatabase.execSQL(alterSQL);
//        }
//        if(oldVersion <= 2)
//        {
//            String alterSQL = "alter table " + SYSINITINFO + " ADD start_img text";
//            sqlitedatabase.execSQL(alterSQL);
//        }
//        if(oldVersion <= 3)
//        {
//            String alterSQL = "alter table " + USER + " ADD creditflag text";
//            sqlitedatabase.execSQL(alterSQL);
//        }

    }

}
