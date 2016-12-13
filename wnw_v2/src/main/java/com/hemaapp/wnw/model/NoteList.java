package com.hemaapp.wnw.model;

import android.text.format.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import xtom.frame.XtomObject;

/**
 * 帖子列表
 * Created by Hufanglin on 2016/3/2.
 */
public class NoteList extends XtomObject implements Serializable {

    private static final long serialVersionUID = -2544403015129044263L;
    private String nickname;
    private String avatar;
    private String id;
    private String client_id;
    private String imgurl;
    private String imgurlbig;
    private String title;
    private String replycount;
    private String praise_count;
    private String content;
    private String regdate;
    private String flag;
    private String curr_regdate;

    private String good;
    private String update_date;
    private String visitcount;
    private String edit_flag;
    private String goods_id;

    public NoteList(JSONObject jsonObject) {
        try {
            nickname = jsonObject.getString("nickname");
            avatar = jsonObject.getString("avatar");
            id = jsonObject.getString("id");
            if (!jsonObject.isNull("client_id")) {
                client_id = jsonObject.getString("client_id");
            }
            imgurl = jsonObject.getString("imgurl");
            imgurlbig = jsonObject.getString("imgurlbig");
            title = jsonObject.getString("title");
            replycount = jsonObject.getString("replycount");
            praise_count = jsonObject.getString("praise_count");
            content = jsonObject.getString("content");
            regdate = jsonObject.getString("regdate");

            if (!jsonObject.isNull("flag")) {
                flag = jsonObject.getString("flag");
            }
            if (!jsonObject.isNull("curr_regdate")) {
                curr_regdate = jsonObject.getString("curr_regdate");
            } else {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                curr_regdate = df.format(new Date());
            }

            good = get(jsonObject, "good");
            update_date = get(jsonObject, "update_date");
            visitcount = get(jsonObject, "visitcount");
            edit_flag = get(jsonObject, "edit_flag");
            goods_id = get(jsonObject, "goods_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getCurr_regdate() {
        return curr_regdate;
    }

    public void setCurr_regdate(String curr_regdate) {
        this.curr_regdate = curr_regdate;
    }

    public String getGood() {
        return good;
    }

    public void setGood(String good) {
        this.good = good;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getVisitcount() {
        return visitcount;
    }

    public void setVisitcount(String visitcount) {
        this.visitcount = visitcount;
    }

    public String getEdit_flag() {
        return edit_flag;
    }

    public void setEdit_flag(String edit_flag) {
        this.edit_flag = edit_flag;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public void setImgurlbig(String imgurlbig) {
        this.imgurlbig = imgurlbig;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReplycount(String replycount) {
        this.replycount = replycount;
    }

    public void setPraise_count(String praise_count) {
        this.praise_count = praise_count;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getId() {
        return id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getImgurlbig() {
        return imgurlbig;
    }

    public String getTitle() {
        return title;
    }

    public String getReplycount() {
        return replycount;
    }

    public String getPraise_count() {
        return praise_count;
    }

    public String getContent() {
        return content;
    }

    public String getRegdate() {
        return regdate;
    }

    public String getFlag() {
        return flag;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getTimeDiff() {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        Date dateRegdate = null;
        Date serverDate = null;
        try {
            dateRegdate = sdf1.parse(update_date);
            serverDate = sdf1.parse(curr_regdate);
            long secondDiff = (serverDate.getTime() - dateRegdate.getTime()) / 1000;
            long minutesDiff = secondDiff / 60;
            long hoursDiff = minutesDiff / 60;
            long daysDiff = hoursDiff / 24;
            if (secondDiff < 60) {
                return "1分钟前";
            } else if (minutesDiff < 60) {
                return minutesDiff + "分钟前";
            } else if (hoursDiff < 24) {
                return hoursDiff + "小时前";
            } else if (daysDiff <= 2) {
                return daysDiff + "天前";
            } else {
                return sdf2.format(dateRegdate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return update_date;
    }

    public String getUpdateDiff() {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        Date dateRegdate = null;
        Date serverDate = null;
        try {
            dateRegdate = sdf1.parse(update_date);
            serverDate = sdf1.parse(curr_regdate);
            long secondDiff = (serverDate.getTime() - dateRegdate.getTime()) / 1000;
            long minutesDiff = secondDiff / 60;
            long hoursDiff = minutesDiff / 60;
            long daysDiff = hoursDiff / 24;
            if (secondDiff < 60) {
                return "1分钟前";
            } else if (minutesDiff < 60) {
                return minutesDiff + "分钟前";
            } else if (hoursDiff < 24) {
                return hoursDiff + "小时前";
            } else if (daysDiff <= 2) {
                return daysDiff + "天前";
            } else {
                return sdf2.format(dateRegdate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return regdate;
    }
}
