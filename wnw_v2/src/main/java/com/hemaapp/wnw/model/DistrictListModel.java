package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;

/**
 * 地区列表接口
 * Created by HuHu on 2016-09-16.
 */
public class DistrictListModel extends XtomObject {
    private String id;
    private String name;

    private boolean isSelected = false;

    public DistrictListModel(JSONObject jsonObject) {
        isSelected = false;
        try {
            id = get(jsonObject, "id");
            name = get(jsonObject, "name");
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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
