package com.hemaapp.wnw.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.hemaapp.wnw.model.User;

/**
 * 用户信息数据库帮助类
 */
public class UserDBHelper extends DBHelper {
    String tableName = USER;

    String columns = "id,username,inviter_code,email,password,paypassword,nickname,feeaccount,sex,age," +
            "mobile,avatar,avatarbig,district_name,onlineflag,validflag,lng,lat,deviceid,devicetype," +
            "channelid,lastloginversion,lastlogintime,content,regdate,bankuser,bankname,bankcard,bankaddress," +
            "alipay,realname,thirdtype,thirduid,birthday,communityid,inviter,commision,tcommision,qrcode," +
            "team_xiaofei,rlevel,upline,fans,flag,edit_flag,token," +
            "major,like";//48项

    String insertColumns = "id,username,inviter_code,email,password,paypassword,nickname,feeaccount,sex,age," +
            "mobile,avatar,avatarbig,district_name,onlineflag,validflag,lng,lat,deviceid,devicetype," +
            "channelid,lastloginversion,lastlogintime,content,regdate,bankuser,bankname,bankcard,bankaddress," +
            "alipay,realname,thirdtype,thirduid,birthday,communityid,inviter,commision,tcommision,qrcode," +
            "team_xiaofei,rlevel,upline,fans,flag,edit_flag,token,android_must_update,android_last_version,android_update_url,major,like";//51项

    String updateColumns = "id=?,username=?,inviter_code=?,email=?,password=?,paypassword=?,nickname=?,feeaccount=?,sex=?,age=?," +
            "mobile=?,avatar=?,avatarbig=?,district_name=?,onlineflag=?,validflag=?,lng=?,lat=?,deviceid=?,devicetype=?," +
            "channelid=?,lastloginversion=?,lastlogintime=?,content=?,regdate=?,bankuser=?,bankname=?,bankcard=?,bankaddress=?," +
            "alipay=?,realname=?,thirdtype=?,thirduid=?,birthday=?,communityid=?,inviter=?,commision=?,tcommision=?,qrcode=?," +
            "team_xiaofei=?,rlevel=?,upline=?,fans=?,flag=?,edit_flag=?,token=?,android_must_update=?,android_last_version=?,android_update_url=?," +
            "major=?,like=?";//51项


    /**
     * 实例化系统初始化信息数据库帮助类
     *
     * @param context
     */
    public UserDBHelper(Context context) {
        super(context);
    }

    public boolean insertOrUpdate(User user) {
        if (isExist(user)) {
            return update(user);
        } else {
            return insert(user);
        }
    }

    /**
     * 插入一条记录
     *
     * @return 是否成功
     */
    private boolean insert(User user) {
        String sql = "insert into "
                + tableName
                + " ("
                + insertColumns
                + ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";//51

        Object[] bindArgs = new Object[]{
                user.getId(), user.getUsername(), user.getInviter_code(),
                user.getEmail(), user.getPassword(), user.getPaypassword(), user.getNickname(),
                user.getFeeaccount(), user.getSex(), user.getAge(), user.getMobile(), user.getAvatar(),
                user.getAvatarbig(), user.getDistrict_name(), user.getOnlineflag(), user.getValidflag(),
                user.getLng(), user.getLat(), user.getDeviceid(), user.getDevicetype(), user.getChannelid(),
                user.getLastloginversion(), user.getLastlogintime(), user.getContent(), user.getRegdate(), user.getBankuser(),
                user.getBankname(), user.getBankcard(), user.getBankaddress(), user.getAlipay(),
                user.getRealname(), user.getThirdtype(), user.getThirduid(), user.getBirthday(), user.getCommunityid(),
                user.getInviter(), user.getCommision(), user.getTcommision(), user.getQrcode(), user.getTeam_xiaofei(),
                user.getRlevel(), user.getUpline(), user.getFans(), user.getFlag(), user.getEdit_flag(), user.getToken(),
                user.getAndroid_must_update(), user.getAndroid_last_version(), user.getAndroid_update_url(), user.getMajor(), user.getLike()
        };//50

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
    private boolean update(User user) {
        String conditions = " where id=" + user.getId();
        String sql = "update " + tableName + " set " + updateColumns
                + conditions;
        Object[] bindArgs = new Object[]{
                user.getId(), user.getUsername(), user.getInviter_code(),
                user.getEmail(), user.getPassword(), user.getPaypassword(), user.getNickname(),
                user.getFeeaccount(), user.getSex(), user.getAge(), user.getMobile(), user.getAvatar(),
                user.getAvatarbig(), user.getDistrict_name(), user.getOnlineflag(), user.getValidflag(),
                user.getLng(), user.getLat(), user.getDeviceid(), user.getDevicetype(), user.getChannelid(),
                user.getLastloginversion(), user.getLastlogintime(), user.getContent(), user.getRegdate(), user.getBankuser(),
                user.getBankname(), user.getBankcard(), user.getBankaddress(), user.getAlipay(),
                user.getRealname(), user.getThirdtype(), user.getThirduid(), user.getBirthday(), user.getCommunityid(),
                user.getInviter(), user.getCommision(), user.getTcommision(), user.getQrcode(), user.getTeam_xiaofei(),
                user.getRlevel(), user.getUpline(), user.getFans(), user.getFlag(), user.getEdit_flag(), user.getToken(),
                user.getAndroid_must_update(), user.getAndroid_last_version(), user.getAndroid_update_url(),
                user.getMajor(), user.getLike()
        };

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

    public boolean isExist(User user) {
        String id = user.getId();
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
     * @return 用户信息
     */
    public User select(String username) {
        String conditions = " where username='" + username
                + "' or nickname = '" + username + "'";
        String sql = "select " + columns + " from " + tableName + conditions;

        SQLiteDatabase db = getWritableDatabase();
        User user = null;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            user = new User(cursor.getString(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getString(7),
                    cursor.getString(8), cursor.getString(9),
                    cursor.getString(10), cursor.getString(11),
                    cursor.getString(12), cursor.getString(13),
                    cursor.getString(14), cursor.getString(15),
                    cursor.getString(16), cursor.getString(17),
                    cursor.getString(18), cursor.getString(19),
                    cursor.getString(20), cursor.getString(21),
                    cursor.getString(22), cursor.getString(23),
                    cursor.getString(24), cursor.getString(25),
                    cursor.getString(26), cursor.getString(27),
                    cursor.getString(28), cursor.getString(29),
                    cursor.getString(30), cursor.getString(31),
                    cursor.getString(32), cursor.getString(33),
                    cursor.getString(34), cursor.getString(35),
                    cursor.getString(36), cursor.getString(37),
                    cursor.getString(38), cursor.getString(39),
                    cursor.getString(40), cursor.getString(41),
                    cursor.getString(42), cursor.getString(43),
                    cursor.getString(44), cursor.getString(45),
                    cursor.getString(46), cursor.getString(47));//最后三项不需要的
        }
        cursor.close();
        db.close();
        return user;
    }
}
