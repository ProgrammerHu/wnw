package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;

/**
 * 粉丝列表
 * Created by HuHu on 2016-09-23.
 */
public class FansListModel extends XtomObject {
    private String id;
    private String sex;
    private String avatar;
    private String avatarbig;
    private String username;
    private String nickname;
    private String regdate;
    private String inviter_nickname;
    private String inviter_username;
    private String imgItems;

    public FansListModel(JSONObject jsonObject) {
        try {
            id = get(jsonObject, "id");
            sex = get(jsonObject, "sex");
            avatar = get(jsonObject, "avatar");
            avatarbig = get(jsonObject, "avatarbig");
            username = get(jsonObject, "username");
            nickname = get(jsonObject, "nickname");
            regdate = get(jsonObject, "regdate");
            inviter_nickname = get(jsonObject, "inviter_nickname");
            inviter_username = get(jsonObject, "inviter_username");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setAvatarbig(String avatarbig) {
        this.avatarbig = avatarbig;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public void setInviter_nickname(String inviter_nickname) {
        this.inviter_nickname = inviter_nickname;
    }

    public void setInviter_username(String inviter_username) {
        this.inviter_username = inviter_username;
    }

    public void setImgItems(String imgItems) {
        this.imgItems = imgItems;
    }

    public String getId() {
        return id;
    }

    public String getSex() {
        return sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAvatarbig() {
        return avatarbig;
    }

    public String getUsername() {
        if (username.length() == 11) {
            return username.substring(0, 3) + "****" + username.substring(username.length() - 4, username.length());
        }
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getRegdate() {
        return regdate;
    }

    public String getInviter_nickname() {
        return inviter_nickname;
    }

    public String getInviter_username() {
        if (!isNull(inviter_username) && inviter_username.length() == 11) {
            return inviter_username.substring(0, 3) + "****" + inviter_username.substring(inviter_username.length() - 4, inviter_username.length());
        }
        return "";
    }

    public String getImgItems() {
        return imgItems;
    }
}
