package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;

/**
 * 海外城市列表
 * Created by HuHu on 2016-09-06.
 */
public class CountryListModel extends XtomObject {

    private String id;
    private String name;
    private String imgurl;
    private String imgurlbig;
    private String orderby;

    public CountryListModel(JSONObject jsonObject) {
        try {
            id = get(jsonObject, "id");
            name = get(jsonObject, "name");
            imgurl = get(jsonObject, "imgurl");
            imgurlbig = get(jsonObject, "imgurlbig");
            orderby = get(jsonObject, "orderby");
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

    public void setImgurlbig(String imgurlbig) {
        this.imgurlbig = imgurlbig;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
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

    public String getImgurlbig() {
        return imgurlbig;
    }

    public String getOrderby() {
        return orderby;
    }
}
