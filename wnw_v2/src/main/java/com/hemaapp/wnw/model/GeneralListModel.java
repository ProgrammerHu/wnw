package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;

/**
 * 退款列表数据模型
 * Created by Hufanglin on 2016/3/18.
 */
public class GeneralListModel extends XtomObject {

    private String id;
    private String name;
    private String orderby;

    public GeneralListModel(JSONObject jsonObject) {
        try {
            id= jsonObject.getString("id");
            name= jsonObject.getString("name");
            orderby= jsonObject.getString("orderby");
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
