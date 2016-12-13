package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;

/**
 * 银行列表
 * Created by HuHu on 2016-06-07.
 */
public class Bank extends XtomObject implements Serializable {
    private static final long serialVersionUID = 5730745708339415874L;
    private String id;
    private String name;
    private String orderby;

    public Bank(JSONObject jsonObject) {
        try {
            id = get(jsonObject, "id");
            name = get(jsonObject, "name");
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

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOrderby() {
        return orderby;
    }
}
