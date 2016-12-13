package com.hemaapp.luna_demo.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by HuHu on 2016/4/24.
 */
public class LagouModel {

    private int positionId;
    private String positionName;
    private String city;
    private String createTime;
    private String salary;
    private int companyId;
    private String companyLogo;
    private String companyName;
    private String positionFirstType;

    public LagouModel(JSONObject jsonObject) throws JSONException {
        positionId = jsonObject.getInt("positionId");
        companyId = jsonObject.getInt("companyId");
        positionName = jsonObject.getString("positionName");
        city = jsonObject.getString("city");
        createTime = jsonObject.getString("createTime");
        salary = jsonObject.getString("salary");
        companyLogo = jsonObject.getString("companyLogo");
        companyName = jsonObject.getString("companyName");
        positionFirstType = jsonObject.getString("positionFirstType");
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setPositionFirstType(String positionFirstType) {
        this.positionFirstType = positionFirstType;
    }

    public int getPositionId() {
        return positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public String getCity() {
        return city;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getSalary() {
        return salary;
    }

    public int getCompanyId() {
        return companyId;
    }

    public String getCompanyLogo() {
        return "http://www.lagou.com/" + companyLogo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getPositionFirstType() {
        return positionFirstType;
    }
}
