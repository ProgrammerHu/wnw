package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;

/**
 * 分类商品列表
 * Created by HuHu on 2016/3/21.
 */
public class TypeGoodsList extends XtomObject {

    /**
     * imgurl : http://101.200.77.159/uploadfiles/2015/04/201504181007413844.jpg
     * id : 6
     * paycount : 7
     * price : 100.00
     * oldprice : 140.00
     * name : 精纯矿物奇妙新颜隔离乳霜
     */

    private String imgurl;
    private String imgurlbig;
    private String id;
    private String paycount;
    private String leftcount;
    private String price;
    private String oldprice;
    private String name;

    public TypeGoodsList(JSONObject jsonObject) {
        try {
            imgurl = jsonObject.getString("imgurl");
            id = jsonObject.getString("id");
            paycount = jsonObject.getString("paycount");
            price = jsonObject.getString("price");
            oldprice = jsonObject.getString("oldprice");
            name = jsonObject.getString("name");
            imgurlbig = jsonObject.getString("imgurlbig");
            leftcount = jsonObject.getString("leftcount");
        } catch (JSONException e) {
            e.printStackTrace();
            imgurlbig = imgurl;
        }
    }

    public String getLeftcount() {
        return leftcount;
    }

    public void setLeftcount(String leftcount) {
        this.leftcount = leftcount;
    }

    public String getImgurlbig() {
        return imgurlbig;
    }

    public void setImgurlbig(String imgurlbig) {
        this.imgurlbig = imgurlbig;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPaycount(String paycount) {
        this.paycount = paycount;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setOldprice(String oldprice) {
        this.oldprice = oldprice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getId() {
        return id;
    }

    public String getPaycount() {
        return paycount;
    }

    public String getPrice() {
        return price;
    }

    public String getOldprice() {
        return oldprice;
    }

    public String getName() {
        return name;
    }
}
