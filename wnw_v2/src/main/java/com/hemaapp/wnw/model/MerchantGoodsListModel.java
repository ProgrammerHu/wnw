package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;

/**
 * 商家商品列表
 * Created by HuHu on 2016-09-08.
 */
public class MerchantGoodsListModel extends XtomObject implements Serializable {

    private static final long serialVersionUID = 4825360963376706077L;

    private String id;
    private String name;
    private String paycount;
    private String price;
    private String oldprice;
    private String imgcount;
    private String imgurl;
    private String imgurlbig;
    private String leftcount;
    private String merchant_id;
    private String group_flag;
    private String limit_count;
    private String time;
    private String saleflag;
    private String time_start;
    private String time_end;

    private boolean IsSelected = false;

    public MerchantGoodsListModel(JSONObject jsonObject) {
        IsSelected = false;
        try {
            id = get(jsonObject, "id");
            name = get(jsonObject, "name");
            paycount = get(jsonObject, "paycount");
            price = get(jsonObject, "price");
            oldprice = get(jsonObject, "oldprice");
            imgcount = get(jsonObject, "imgcount");
            imgurl = get(jsonObject, "imgurl");
            imgurlbig = get(jsonObject, "imgurlbig");
            leftcount = get(jsonObject, "leftcount");
            merchant_id = get(jsonObject, "merchant_id");
            group_flag = get(jsonObject, "group_flag");
            limit_count = get(jsonObject, "limit_count");
            time = get(jsonObject, "time");
            saleflag = get(jsonObject, "saleflag");
            time_start = get(jsonObject, "time_start");
            time_end = get(jsonObject, "time_end");

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

    public void setPaycount(String paycount) {
        this.paycount = paycount;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setOldprice(String oldprice) {
        this.oldprice = oldprice;
    }

    public void setImgcount(String imgcount) {
        this.imgcount = imgcount;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public void setImgurlbig(String imgurlbig) {
        this.imgurlbig = imgurlbig;
    }

    public void setLeftcount(String leftcount) {
        this.leftcount = leftcount;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public void setGroup_flag(String group_flag) {
        this.group_flag = group_flag;
    }

    public void setLimit_count(String limit_count) {
        this.limit_count = limit_count;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setSaleflag(String saleflag) {
        this.saleflag = saleflag;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public String getImgcount() {
        return imgcount;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getImgurlbig() {
        return imgurlbig;
    }

    public String getLeftcount() {
        return leftcount;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public String getGroup_flag() {
        return group_flag;
    }

    public String getLimit_count() {
        return limit_count;
    }

    public String getTime() {
        return time;
    }

    public String getSaleflag() {
        return saleflag;
    }

    public String getTime_start() {
        return time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public boolean isSelected() {
        return IsSelected;
    }

    public void setSelected(boolean selected) {
        IsSelected = selected;
    }
}
