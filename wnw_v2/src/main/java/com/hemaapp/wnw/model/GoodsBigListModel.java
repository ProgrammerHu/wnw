package com.hemaapp.wnw.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;

/**
 * 我的足迹的商品列表
 *
 * @author CoderHu
 * @author HuFanglin
 * @DateTime 2016年3月9日
 */
public class GoodsBigListModel extends XtomObject {

    private String imgurl;
    private String id;
    private String name;
    private String paycount;
    private String price;
    private String oldprice;
    private String imgcount;
    private String imageBig;
    private String leftcount;
    private String regdate;
    private String flag;

    public GoodsBigListModel(JSONObject jsonObject) {
        try {
            imgurl = jsonObject.getString("imgurl");
            id = jsonObject.getString("id");
            name = jsonObject.getString("name");
            paycount = jsonObject.getString("paycount");
            price = jsonObject.getString("price");
            oldprice = jsonObject.getString("oldprice");
            imgcount = jsonObject.getString("imgcount");
            if (!jsonObject.isNull("leftcount")) {
                leftcount = jsonObject.getString("leftcount");
            }
            if (!jsonObject.isNull("regdate")) {
                regdate = jsonObject.getString("regdate");
            }
            if (!jsonObject.isNull("flag")) {
                flag = jsonObject.getString("flag");
            }

            if (!jsonObject.isNull("imgItems")) {
                JSONArray jsonArray = jsonObject.getJSONArray("imgItems");
                if (jsonArray.length() > 0) {
                    imageBig = jsonArray.getJSONObject(0)
                            .getString("imgurlbig");
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getImgcount() {
        return imgcount;
    }

    public String getImageBig() {
        return imageBig;
    }

    public String getLeftCount() {
        return leftcount;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public String getRegdate() {

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date tempDate = sdf1.parse(regdate);
            return sdf2.format(tempDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return regdate;
    }

    public String getFlag() {
        return flag;
    }
}
