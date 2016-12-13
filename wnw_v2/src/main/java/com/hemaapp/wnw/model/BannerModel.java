package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;

/**
 * 图片轮播的数据模型
 * Created by Hufanglin on 2016/3/2.
 */
public class BannerModel extends XtomObject {
    private String id;
    private String content;
    private String blog_id;
    private String imgurl;
    private String imgurlbig;
    private String type;
//    private String orderby;
//    private String online;

    public BannerModel(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getString("id");
            if (!jsonObject.isNull("content"))
                this.content = jsonObject.getString("content");
            this.blog_id = jsonObject.getString("blog_id");
            this.imgurl = jsonObject.getString("imgurl");
            this.imgurlbig = jsonObject.getString("imgurlbig");
            this.type = jsonObject.getString("type");
//            this.orderby = jsonObject.getString("orderby");
//            this.online = jsonObject.getString("online");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setBlog_id(String blog_id) {
        this.blog_id = blog_id;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public void setImgurlbig(String imgurlbig) {
        this.imgurlbig = imgurlbig;
    }

    public void setType(String type) {
        this.type = type;
    }

    /*public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public void setOnline(String online) {
        this.online = online;
    }*/

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getBlog_id() {
        return blog_id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getImgurlbig() {
        return imgurlbig;
    }

    public String getType() {
        return type;
    }
/*
    public String getOrderby() {
        return orderby;
    }

    public String getOnline() {
        return online;
    }*/
}
