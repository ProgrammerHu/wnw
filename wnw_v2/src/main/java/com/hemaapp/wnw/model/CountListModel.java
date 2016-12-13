package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;

/**
 * 商家分类数量接口
 * Created by HuHu on 2016-09-13.
 */
public class CountListModel extends XtomObject {
    private String id;
    private String name;
    private String parentid;
    private String nodepath;
    private String imgurl;
    private String imgurlbig;
    private String orderby;
    private String regdate;
    private String count;

    public CountListModel(JSONObject jsonObject) {
        try {
            id = get(jsonObject, "id");
            name = get(jsonObject, "name");
            parentid = get(jsonObject, "parentid");
            nodepath = get(jsonObject, "nodepath");
            imgurl = get(jsonObject, "imgurl");
            imgurlbig = get(jsonObject, "imgurlbig");
            orderby = get(jsonObject, "orderby");
            regdate = get(jsonObject, "regdate");
            count = get(jsonObject, "count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public CountListModel(String id, String name, String count) {
        this.id = id;
        this.name = name;
        this.count = count;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public void setNodepath(String nodepath) {
        this.nodepath = nodepath;
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

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getParentid() {
        return parentid;
    }

    public String getNodepath() {
        return nodepath;
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

    public String getRegdate() {
        return regdate;
    }

    public int getCount() {
        try {
            return Integer.parseInt(count);
        } catch (Exception e) {
            return 0;
        }
    }
}
