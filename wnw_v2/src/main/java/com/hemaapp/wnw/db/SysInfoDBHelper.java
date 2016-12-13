package com.hemaapp.wnw.db;

import com.hemaapp.wnw.model.SysInitInfo;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * 系统初始化信息数据库帮助类
 */
public class SysInfoDBHelper extends DBHelper {
    String tableName = SYSINITINFO;

    String columns = "sys_web_service,sys_plugins,sys_show_iospay,sys_chat_ip,sys_chat_port,"
            + "android_must_update,android_last_version,iphone_must_update,iphone_last_version,sys_pagesize,sys_service_phone,"
            + "android_update_url,iphone_update_url,iphone_comment_url,msg_invite,qrcode_memo,goods_memo,group_memo,start_img,info,logo";

    String updateColumns = "sys_web_service=?,sys_plugins=?,sys_show_iospay=?,sys_chat_ip=?,sys_chat_port=?,"
            + "android_must_update=?,android_last_version=?,iphone_must_update=?,iphone_last_version=?,sys_pagesize=?,sys_service_phone=?,"
            + "android_update_url=?,iphone_update_url=?,iphone_comment_url=?,msg_invite=?,qrcode_memo=?,goods_memo=?,group_memo=?,start_img=?,info=?,logo=?";

    /**
     * 实例化系统初始化信息数据库帮助类
     *
     * @param context
     */
    public SysInfoDBHelper(Context context) {
        super(context);
    }

    public boolean insertOrUpdate(SysInitInfo info) {
        if (isExist()) {
            return update(info);
        } else {
            return insert(info);
        }
    }

    /**
     * 插入一条记录
     *
     * @return 是否成功
     */
    public boolean insert(SysInitInfo info) {
        String sql = "insert into " + tableName + " (" + columns
                + ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";// 21个字段

        Object[] bindArgs = new Object[]{info.getSys_web_service(),
                info.getSys_plugins(), info.getSys_show_iospay(),
                info.getSys_chat_ip(), info.getSys_chat_port(),
                info.getAndroid_must_update(), info.getAndroid_last_version(),
                info.getIphone_must_update(), info.getIphone_last_version(),
                info.getSys_pagesize(), info.getSys_service_phone(),
                info.getAndroid_update_url(), info.getIphone_update_url(),
                info.getIphone_comment_url(), info.getMsg_invite(), info.getQrcode_memo(),
                info.getGoods_memo(), info.getGroup_memo(),
                info.getStart_img(), info.getInfo(), info.getLogo()};
        SQLiteDatabase db = getWritableDatabase();
        boolean success = true;
        try {
            db.execSQL(sql, bindArgs);
        } catch (SQLException e) {
            success = false;
        }
        db.close();
        return success;
    }

    /**
     * 更新
     *
     * @return 是否成功
     */
    public boolean update(SysInitInfo info) {
        int id = 1;
        String conditions = " where id=" + id;
        String sql = "update " + tableName + " set " + updateColumns
                + conditions;
        Object[] bindArgs = new Object[]{info.getSys_web_service(),
                info.getSys_plugins(), info.getSys_show_iospay(),
                info.getSys_chat_ip(), info.getSys_chat_port(),
                info.getAndroid_must_update(), info.getAndroid_last_version(),
                info.getIphone_must_update(), info.getIphone_last_version(),
                info.getSys_pagesize(), info.getSys_service_phone(),
                info.getAndroid_update_url(), info.getIphone_update_url(),
                info.getIphone_comment_url(), info.getMsg_invite(), info.getQrcode_memo(),
                info.getGoods_memo(), info.getGroup_memo(),
                info.getStart_img(), info.getInfo(), info.getLogo()};
        SQLiteDatabase db = getWritableDatabase();
        boolean success = true;
        try {
            db.execSQL(sql, bindArgs);
        } catch (SQLException e) {
            success = false;
        }
        db.close();
        return success;
    }

    public boolean isExist() {
        int id = 1;
        String sql = ("select * from " + tableName + " where id=" + id);
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        boolean exist = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exist;
    }

    /**
     * 清空
     */
    public void clear() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + tableName);
        db.close();
    }

    /**
     * 判断表是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + tableName, null);
        boolean empty = 0 == cursor.getCount();
        cursor.close();
        db.close();
        return empty;
    }

    /**
     * @return 系统初始化信息
     */
    public SysInitInfo select() {
        int id = 1;
        String conditions = " where id=" + id;
        String sql = "select " + columns + " from " + tableName + conditions;

        SQLiteDatabase db = getWritableDatabase();
        SysInitInfo info = null;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            info = new SysInitInfo(cursor.getString(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getString(7),
                    cursor.getString(8), cursor.getInt(9),
                    cursor.getString(10), cursor.getString(11),
                    cursor.getString(12), cursor.getString(13),
                    cursor.getString(14), cursor.getString(15),
                    cursor.getString(16), cursor.getString(17),
                    cursor.getString(18), cursor.getString(19),
                    cursor.getString(20));
        }
        cursor.close();
        db.close();
        return info;
    }
}
