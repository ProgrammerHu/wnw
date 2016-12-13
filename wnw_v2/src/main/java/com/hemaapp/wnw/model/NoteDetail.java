package com.hemaapp.wnw.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 帖子详情数据模型
 * Created by Hufanglin on 2016/3/2.
 */
public class NoteDetail extends XtomObject implements Serializable, Parcelable {

    private String nickname;
    private String avatar;
    private String id;
    private String client_id;
    private String title;
    private String content;
    private String replycount;
    private String love_count;
    private String praise_count;
    private String imgcount;
    private String imgurl;
    private String imgurlbig;
    private String table_id;
    private String regdate;
    private String curr_regdate;
    private String flag;
    private String love_flag;
    private String subscribe_flag;
    private List<Image> imgItems = new ArrayList<>();
    private List<Tag> tableItems = new ArrayList<>();
    private List<RecommendItem> recomendItems = new ArrayList<>();

    private String con;
    private String update_date;
    private String table_name;
    private String keyid;
    private String good;
    private String edit_flag;
    private String visitcount;
    private String goods_id;

    public NoteDetail(JSONObject jsonObject) {
        try {
            nickname = get(jsonObject, "nickname");
            avatar = get(jsonObject, "avatar");
            id = get(jsonObject, "id");
            client_id = get(jsonObject, "client_id");
            title = get(jsonObject, "title");
            content = get(jsonObject, "content");
            replycount = get(jsonObject, "replycount");
            love_count = get(jsonObject, "love_count");
            praise_count = get(jsonObject, "praise_count");
            imgcount = get(jsonObject, "imgcount");
            imgurl = get(jsonObject, "imgurl");
            imgurlbig = get(jsonObject, "imgurlbig");
            table_id = get(jsonObject, "table_id");
            regdate = get(jsonObject, "regdate");
            curr_regdate = get(jsonObject, "curr_regdate");
            flag = get(jsonObject, "flag");
            love_flag = get(jsonObject, "love_flag");
            subscribe_flag = get(jsonObject, "subscribe_flag");

            con = get(jsonObject, "con");
            update_date = get(jsonObject, "update_date");
            table_name = get(jsonObject, "table_name");
            keyid = get(jsonObject, "keyid");
            good = get(jsonObject, "good");
            edit_flag = get(jsonObject, "edit_flag");
            visitcount = get(jsonObject, "visitcount");
            goods_id = get(jsonObject, "goods_id");

            if (!jsonObject.isNull("imgItems") && !isNull(jsonObject.getString("imgItems"))) {//有图片轮播
                JSONArray imageArrray = jsonObject.getJSONArray("imgItems");
                for (int i = 0; i < imageArrray.length(); i++) {
                    try {
                        Image image = new Image(imageArrray.getJSONObject(i));
                        imgItems.add(image);
                    } catch (DataParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (!jsonObject.isNull("tableItems") && !isNull(jsonObject.getString("tableItems"))) {//有标签
                JSONArray tagArray = jsonObject.getJSONArray("tableItems");
                for (int i = 0; i < tagArray.length(); i++) {
                    Tag tag = new Tag(tagArray.getJSONObject(i));
                    tableItems.add(tag);
                }
            }
            if (!jsonObject.isNull("recomendItems") && !isNull(jsonObject.getString("recomendItems"))) {//有推荐商品
                JSONArray recomendArray = jsonObject.getJSONArray("recomendItems");
                for (int i = 0; i < recomendArray.length(); i++) {
                    RecommendItem recommendItem = new RecommendItem(recomendArray.getJSONObject(i));
                    recomendItems.add(recommendItem);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setReplycount(String replycount) {
        this.replycount = replycount;
    }

    public void setLove_count(String love_count) {
        this.love_count = love_count;
    }

    public void setPraise_count(String praise_count) {
        this.praise_count = praise_count;
    }

    public void setImgcount(String imgcount) {
        this.imgcount = imgcount;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public void setImgurlbig(String imgurlbig) {
        this.imgurlbig = imgurlbig;
    }

    public void setTable_id(String table_id) {
        this.table_id = table_id;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setLove_flag(String love_flag) {
        this.love_flag = love_flag;
    }

    public void setSubscribe_flag(String subscribe_flag) {
        this.subscribe_flag = subscribe_flag;
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

    public String getClient_id() {
        return client_id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getReplycount() {
        return replycount;
    }

    public String getLove_count() {
        return love_count;
    }

    public String getPraise_count() {
        return praise_count;
    }

    public String getImgcount() {
        return imgcount;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getImgurlbig() {
        return imgurlbig;
    }

    public String getTable_id() {
        return table_id;
    }

    public String getRegdate() {
        return regdate;
    }

    public List<Image> getImgItems() {
        return imgItems;
    }

    public String getFlag() {
        return flag;
    }

    public String getLove_flag() {
        return love_flag;
    }

    public String getSubscribe_flag() {
        return subscribe_flag;
    }

    public List<Tag> getTableItems() {
        return tableItems;
    }

    public List<RecommendItem> getRecomendItems() {
        return recomendItems;
    }

    public String getCon() {
        return con;
    }

    public void setCon(String con) {
        this.con = con;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getKeyid() {
        return keyid;
    }

    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }

    public String getGood() {
        return good;
    }

    public void setGood(String good) {
        this.good = good;
    }

    public String getEdit_flag() {
        return edit_flag;
    }

    public void setEdit_flag(String edit_flag) {
        this.edit_flag = edit_flag;
    }

    public String getVisitcount() {
        return visitcount;
    }

    public void setVisitcount(String visitcount) {
        this.visitcount = visitcount;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    /**
     * 获取时间差
     *
     * @return
     */
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nickname);
        dest.writeString(this.avatar);
        dest.writeString(this.id);
        dest.writeString(this.client_id);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.replycount);
        dest.writeString(this.love_count);
        dest.writeString(this.praise_count);
        dest.writeString(this.imgcount);
        dest.writeString(this.imgurl);
        dest.writeString(this.imgurlbig);
        dest.writeString(this.table_id);
        dest.writeString(this.regdate);
        dest.writeString(this.flag);
        dest.writeString(this.love_flag);
        dest.writeString(this.subscribe_flag);
        dest.writeTypedList(imgItems);
        dest.writeTypedList(tableItems);
        dest.writeTypedList(recomendItems);
    }

    private NoteDetail(Parcel in) {
        this.nickname = in.readString();
        this.avatar = in.readString();
        this.id = in.readString();
        this.client_id = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.replycount = in.readString();
        this.love_count = in.readString();
        this.praise_count = in.readString();
        this.imgcount = in.readString();
        this.imgurl = in.readString();
        this.imgurlbig = in.readString();
        this.table_id = in.readString();
        this.regdate = in.readString();
        this.flag = in.readString();
        this.love_flag = in.readString();
        this.subscribe_flag = in.readString();
        in.readTypedList(imgItems, Image.CREATOR);
        in.readTypedList(tableItems, Tag.CREATOR);
        in.readTypedList(recomendItems, RecommendItem.CREATOR);
    }

    public static final Parcelable.Creator<NoteDetail> CREATOR = new Parcelable.Creator<NoteDetail>() {
        public NoteDetail createFromParcel(Parcel source) {
            return new NoteDetail(source);
        }

        public NoteDetail[] newArray(int size) {
            return new NoteDetail[size];
        }
    };
}
