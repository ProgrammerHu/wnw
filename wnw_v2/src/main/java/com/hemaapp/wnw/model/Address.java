package com.hemaapp.wnw.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;

/**
 * 收货地址列表 Created by Hufanglin on 2016/3/7.
 */
public class Address extends XtomObject implements Serializable {

    private static final long serialVersionUID = 6768721716949902420L;
    private String id;
    private String client_id;
    private String name;
    private String tel;
    private String province_id;
    private String city_id;
    private String district_id;
    private String position;
    private String address;
    private String defaultflag;
    private String namepath;

    public Address() {
        this.id = "0";
    }

    public Address(JSONObject jsonObject) {
        try {
            id = jsonObject.getString("id");
            client_id = jsonObject.getString("client_id");
            name = jsonObject.getString("name");
            tel = jsonObject.getString("tel");
            province_id = jsonObject.getString("province_id");
            city_id = jsonObject.getString("city_id");
            district_id = jsonObject.getString("district_id");
            position = jsonObject.getString("position");
            address = jsonObject.getString("address");
            defaultflag = jsonObject.getString("defaultflag");
            namepath = jsonObject.getString("namepath");
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

    public void setName(String name) {
        this.name = name;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDefaultflag(String defaultflag) {
        this.defaultflag = defaultflag;
    }

    public void setNamepath(String namepath) {
        this.namepath = namepath;
    }

    public String getId() {
        return id;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public String getProvince_id() {
        return province_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public String getPosition() {
        return position;
    }

    public String getAddress() {
        return address;
    }

    public String getDefaultflag() {
        return defaultflag;
    }

    public String getNamepath() {
        return namepath;
    }
}
