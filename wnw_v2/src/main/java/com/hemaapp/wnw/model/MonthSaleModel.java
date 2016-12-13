package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;

/**
 * 月销量排行
 * Created by HuHu on 2016/3/29.
 */
public class MonthSaleModel extends XtomObject {

    private String id;
    private String name;
    private String imgurl;
    private String imgurlbig;
    private String paycount;
    private String price;
    private String oldprice;
    private String regdate;

    public MonthSaleModel(JSONObject jsonObject) {
        try {
            id = jsonObject.getString("id");
            name = jsonObject.getString("name");
            imgurl = jsonObject.getString("imgurl");
            imgurlbig = jsonObject.getString("imgurlbig");
            paycount = jsonObject.getString("paycount");
            price = jsonObject.getString("price");
            oldprice = jsonObject.getString("oldprice");
            regdate = jsonObject.getString("regdate");
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

    public void setPaycount(String paycount) {
        this.paycount = paycount;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setOldprice(String oldprice) {
        this.oldprice = oldprice;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
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

    public String getPaycount() {
        return paycount;
    }

    public String getPrice() {
        return price;
    }

    public String getOldprice() {
        return oldprice;
    }

    public String getRegdate() {
        return regdate;
    }
}
