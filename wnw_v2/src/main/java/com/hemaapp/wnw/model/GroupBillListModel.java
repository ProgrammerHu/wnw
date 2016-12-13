package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;

/**
 * 团购订单列表
 * Created by HuHu on 2016/3/28.
 */
public class GroupBillListModel extends XtomObject {

    private String person;
    private String rule;
    private String nickname;
    private String merchant_id;
    private String id;
    private String price;
    private String name;
    private String imgurl;
    private String imgurlbig;
    private String blog_id;
    private String total_fee;
    private String shippingfee;
    private String tradetype;


    public GroupBillListModel(JSONObject jsonObject) {
        try {
            person = jsonObject.getString("person");
            rule = jsonObject.getString("rule");
            nickname = jsonObject.getString("nickname");
            merchant_id = jsonObject.getString("merchant_id");
            id = jsonObject.getString("id");
            price = jsonObject.getString("price");

            name = jsonObject.getString("name");
            imgurl = jsonObject.getString("imgurl");
            imgurlbig = jsonObject.getString("imgurlbig");
            blog_id = jsonObject.getString("blog_id");
            total_fee = jsonObject.getString("total_fee");
            shippingfee = jsonObject.getString("shippingfee");
            tradetype = jsonObject.getString("tradetype");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getPrice() {
        return price;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
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

    public void setBlog_id(String blog_id) {
        this.blog_id = blog_id;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public void setShippingfee(String shippingfee) {
        this.shippingfee = shippingfee;
    }

    public void setTradetype(String tradetype) {
        this.tradetype = tradetype;
    }

    public String getPerson() {
        return person;
    }

    public String getRule() {
        return rule;
    }

    public String getNickname() {
        return nickname;
    }

    public String getMerchant_id() {
        return merchant_id;
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

    public String getBlog_id() {
        return blog_id;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public String getShippingfee() {
        return shippingfee;
    }

    public String getTradetype() {
        return tradetype;
    }
}
