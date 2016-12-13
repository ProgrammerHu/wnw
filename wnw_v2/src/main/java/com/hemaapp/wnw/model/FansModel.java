package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;

/**
 * 月粉丝排行
 * Created by HuHu on 2016/4/1.
 */
public class FansModel extends XtomObject implements Serializable {

    private static final long serialVersionUID = 5862154979657754364L;
    private String count;
    private String client_id;
    private String avatar;
    private String nickname;

    public FansModel(JSONObject jsonObject) {
        try {
            count = jsonObject.getString("count");
            client_id = jsonObject.getString("client_id");
            avatar = jsonObject.getString("avatar");
            nickname = jsonObject.getString("nickname");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCount() {
        try {
            double doubleCount = Double.parseDouble(count);
            int intCount = (int) (doubleCount * 100.0);
            return intCount + "%";
        } catch (Exception e) {

        }

        return count;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getNickname() {
        return nickname;
    }
}
