package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;

/**
 * 店铺订阅列表
 * Created by Hufanglin on 2016/3/4.
 */
public class SubscribeList extends XtomObject {

    /**
     * nickname : 奋斗
     * avatar : http://115.28.238.61/uploadfiles/2015/07/201507071413235481_thumb.png
     * id : 236
     * sub_count : 1
     */

    private String nickname;
    private String avatar;
    private String id;
    private String sub_count;

    public SubscribeList(JSONObject jsonObject) {
        try {
            nickname = jsonObject.getString("nickname");
            avatar = jsonObject.getString("avatar");
            id = jsonObject.getString("id");
            sub_count = jsonObject.getString("sub_count");
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

    public void setSub_count(String sub_count) {
        this.sub_count = sub_count;
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

    public String getSub_count() {
        return sub_count;
    }
}
