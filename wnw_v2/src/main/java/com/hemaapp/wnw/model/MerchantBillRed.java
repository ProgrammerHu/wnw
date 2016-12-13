package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;

/**
 * 商家订单红点是否显示接口
 * Created by HuHu on 2016-09-13.
 */
public class MerchantBillRed extends XtomObject {

    private String one;
    private String two;
    private String three;
    private String four;
    private String five;
    private String six;
    private String seven;
    private String eight;

    public MerchantBillRed(JSONObject jsonObject) {
        try {
            one = get(jsonObject, "one");
            two = get(jsonObject, "two");
            three = get(jsonObject, "three");
            four = get(jsonObject, "four");
            five = get(jsonObject, "five");
            six = get(jsonObject, "six");
            seven = get(jsonObject, "seven");
            eight = get(jsonObject, "eight");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setOne(String one) {
        this.one = one;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    public void setThree(String three) {
        this.three = three;
    }

    public void setFour(String four) {
        this.four = four;
    }

    public void setFive(String five) {
        this.five = five;
    }

    public void setSix(String six) {
        this.six = six;
    }

    public void setSeven(String seven) {
        this.seven = seven;
    }

    public void setEight(String eight) {
        this.eight = eight;
    }

    public String getOne() {
        return one;
    }

    public String getTwo() {
        return two;
    }

    public String getThree() {
        return three;
    }

    public String getFour() {
        return four;
    }

    public String getFive() {
        return five;
    }

    public String getSix() {
        return six;
    }

    public String getSeven() {
        return seven;
    }

    public String getEight() {
        return eight;
    }
}
