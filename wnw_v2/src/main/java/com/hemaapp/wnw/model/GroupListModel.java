package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;

/**
 * 团类型列表
 * Created by HuHu on 2016-09-09.
 */
public class GroupListModel extends XtomObject implements Serializable {

    private static final long serialVersionUID = 1126112397453335682L;
    private String id;
    private String type_id;
    private String rule_id;
    private String group_price;
    private String hour;
    private String person;
    private String limit_person;
    private String rule_name;
    private String name;

    public GroupListModel(JSONObject jsonObject) {
        try {
            id = get(jsonObject, "id");
            type_id = get(jsonObject, "type_id");
            rule_id = get(jsonObject, "rule_id");
            group_price = get(jsonObject, "group_price");
            hour = get(jsonObject, "hour");
            person = get(jsonObject, "person");
            limit_person = get(jsonObject, "limit_person");
            rule_name = get(jsonObject, "rule_name");
            name = get(jsonObject, "name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public GroupListModel(String rule_id, String rule_name) {
        this.rule_id = rule_id;
        this.rule_name = rule_name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public void setRule_id(String rule_id) {
        this.rule_id = rule_id;
    }

    public void setGroup_price(String group_price) {
        this.group_price = group_price;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public void setLimit_person(String limit_person) {
        this.limit_person = limit_person;
    }

    public void setRule_name(String rule_name) {
        this.rule_name = rule_name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getType_id() {
        return type_id;
    }

    public String getRule_id() {
        return rule_id;
    }

    public String getGroup_price() {
        return group_price;
    }

    public String getHour() {
        return hour;
    }

    public String getPerson() {
        return person;
    }

    public String getLimit_person() {
        return limit_person;
    }

    public String getRule_name() {
        return rule_name;
    }

    public String getName() {
        return name;
    }
}
