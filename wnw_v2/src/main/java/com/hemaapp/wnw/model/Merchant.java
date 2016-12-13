package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;

/**
 * Created by Hufanglin on 2016/3/3.
 */
public class Merchant extends XtomObject {

    private String merchant_id = "";//店铺主键id
    private String nickname = "";//店铺昵称
    private String avatar = "";//店铺头像
    private String fans = "";//粉丝数
    private String sex = "";
    private String address = "";//地址
    private String major = "";//主营
    private String like = "";//个人爱好
    private String subscribe_flag = "";//订阅状态	0.否1.是

    public Merchant(JSONObject jsonObject) {
        try {
            merchant_id = jsonObject.getString("merchant_id");
            nickname = jsonObject.getString("nickname");
            avatar = jsonObject.getString("avatar");
            fans = jsonObject.getString("fans");
            sex = jsonObject.getString("sex");
            address = jsonObject.getString("address");
            major = jsonObject.getString("major");
            like = jsonObject.getString("like");
            subscribe_flag = jsonObject.getString("subscribe_flag");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public void setSubscribe_flag(String subscribe_flag) {
        this.subscribe_flag = subscribe_flag;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getFans() {
        return fans;
    }

    public String getSex() {
        return sex;
    }

    public String getAddress() {
        return address;
    }

    public String getMajor() {
        return major;
    }

    public String getLike() {
        return like;
    }

    public String getSubscribe_flag() {
        return subscribe_flag;
    }
}
