package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;

/**
 * 抵用券列表
 * Created by HuHu on 2016/3/30.
 */
public class CouponListModel extends XtomObject implements Serializable {

    private static final long serialVersionUID = -2292900244526628915L;
    private String id;
    private String client_id;
    private String coupon_id;
    private String useflag;
    private String keyid;
    private String usedate;
    private String getdate;
    private String total;
    private String limit;
    private String startdate;
    private String enddate;
    private String imgItems;

    public CouponListModel(JSONObject jsonObject) {
        try {
            id = get(jsonObject, "id");
            client_id = get(jsonObject, "client_id");
            coupon_id = get(jsonObject, "coupon_id");
            useflag = get(jsonObject, "useflag");
            keyid = get(jsonObject, "keyid");
            usedate = get(jsonObject, "usedate");
            getdate = get(jsonObject, "getdate");
            total = get(jsonObject, "total");
            limit = get(jsonObject, "limit");
            startdate = get(jsonObject, "startdate");
            enddate = get(jsonObject, "enddate");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public void setUseflag(String useflag) {
        this.useflag = useflag;
    }

    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }

    public void setUsedate(String usedate) {
        this.usedate = usedate;
    }

    public void setGetdate(String getdate) {
        this.getdate = getdate;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public void setImgItems(String imgItems) {
        this.imgItems = imgItems;
    }

    public String getId() {
        return id;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public String getUseflag() {
        return useflag;
    }

    public String getKeyid() {
        return keyid;
    }

    public String getUsedate() {
        return usedate;
    }

    public String getGetdate() {
        return getdate;
    }

    public String getTotal() {
        return total;
    }

    public String getLimit() {
        return limit;
    }

    public String getStartdate() {
        return startdate.replace("-", ".");
    }

    public String getEnddate() {
        return enddate.replace("-", ".");
    }

    public String getImgItems() {
        return imgItems;
    }
}
