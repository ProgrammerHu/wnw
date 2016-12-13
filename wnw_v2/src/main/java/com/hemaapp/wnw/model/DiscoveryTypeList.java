package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;

/**
 * 发现页面标签
 * Created by HuHu on 2016/3/29.
 */
public class DiscoveryTypeList extends XtomObject {

    private String id;
    private String name;
    private String imgurl;
    private String english_name;

    public DiscoveryTypeList(JSONObject jsonObject) {
        try {
            id = get(jsonObject, "id");
            name = get(jsonObject, "name");
            imgurl = get(jsonObject, "imgurl");
            english_name = get(jsonObject, "english_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }
}
