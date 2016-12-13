package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;

/**
 * 团类型列表
 * Created by HuHu on 2016-09-09.
 */
public class GroupTypeListModel extends XtomObject {

    private String id;
    private String name;
    private String person;
    private String limit_person;
    private boolean isSelected = false;

    public GroupTypeListModel(JSONObject jsonObject) {
        isSelected = false;
        try {
            id = get(jsonObject, "id");
            name = get(jsonObject, "name");
            person = get(jsonObject, "person");
            limit_person = get(jsonObject, "limit_person");
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

    public void setPerson(String person) {
        this.person = person;
    }

    public void setLimit_person(String limit_person) {
        this.limit_person = limit_person;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPerson() {
        return person;
    }

    public String getLimit_person() {
        return limit_person;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
