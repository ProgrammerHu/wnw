package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;

/**
 * 属性规格列表
 * Created by HuHu on 2016-09-08.
 */
public class AttrListModel extends XtomObject {

    private String id;
    private String blog_id;
    private String mix;
    private String mix_two;
    private String price;
    private String oldprice;
    private String leftcount;
    private String rule_name;
    private String mix_name;
    private String mix_two_name;

    public AttrListModel(JSONObject jsonObject) {
        try {
            id = get(jsonObject, "id");
            blog_id = get(jsonObject, "blog_id");
            mix = get(jsonObject, "mix");
            mix_two = get(jsonObject, "mix_two");
            price = get(jsonObject, "price");
            oldprice = get(jsonObject, "oldprice");
            leftcount = get(jsonObject, "leftcount");
            rule_name = get(jsonObject, "rule_name");
            mix_name = get(jsonObject, "mix_name");
            mix_two_name = get(jsonObject, "mix_two_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBlog_id(String blog_id) {
        this.blog_id = blog_id;
    }

    public void setMix(String mix) {
        this.mix = mix;
    }

    public void setMix_two(String mix_two) {
        this.mix_two = mix_two;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setOldprice(String oldprice) {
        this.oldprice = oldprice;
    }

    public void setLeftcount(String leftcount) {
        this.leftcount = leftcount;
    }

    public void setRule_name(String rule_name) {
        this.rule_name = rule_name;
    }

    public void setMix_name(String mix_name) {
        this.mix_name = mix_name;
    }

    public void setMix_two_name(String mix_two_name) {
        this.mix_two_name = mix_two_name;
    }

    public String getId() {
        return id;
    }

    public String getBlog_id() {
        return blog_id;
    }

    public String getMix() {
        return mix;
    }

    public String getMix_two() {
        return mix_two;
    }

    public String getPrice() {
        return price;
    }

    public String getOldprice() {
        return oldprice;
    }

    public String getLeftcount() {
        return leftcount;
    }

    public String getRule_name() {
        return rule_name;
    }

    public String getMix_name() {
        return mix_name;
    }

    public String getMix_two_name() {
        return mix_two_name;
    }
}
