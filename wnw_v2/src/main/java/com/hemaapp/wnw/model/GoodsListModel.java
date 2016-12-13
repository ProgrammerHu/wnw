package com.hemaapp.wnw.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 店铺主页没的商品列表
 *
 * @author CoderHu
 * @author HuFanglin
 * @DateTime 2016年3月9日
 */
public class GoodsListModel extends XtomObject {

    private String imgurl;
    private String id;
    private String name;
    private String paycount;
    private String price;
    private String oldprice;
    private String imgcount;
    private String leftcount;
    private ArrayList<Image> imgItems = new ArrayList<>();
    private int selectImgIndex = 0;

    private String imgurlbig;
    private String merchant_id;
    private String limit_count;
    private String time;
    private String time_detail;
    private String curr_date;
    private String time_start;


    public GoodsListModel(JSONObject jsonObject) {
        try {
            imgurl = jsonObject.getString("imgurl");
            id = jsonObject.getString("id");
            name = jsonObject.getString("name");
            paycount = jsonObject.getString("paycount");
            price = jsonObject.getString("price");
            oldprice = jsonObject.getString("oldprice");
            imgcount = get(jsonObject, "imgcount");
            leftcount = get(jsonObject, "leftcount");
            if (!jsonObject.isNull("imgItems") && !isNull(jsonObject.getString("imgItems"))) {
                JSONArray jsonArray = jsonObject.getJSONArray("imgItems");
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        imgItems.add(new Image(jsonArray.getJSONObject(i)));
                    } catch (DataParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            imgurlbig = get(jsonObject, "imgurlbig");
            merchant_id = get(jsonObject, "merchant_id");
            limit_count = get(jsonObject, "limit_count");
            time = get(jsonObject, "time");

            time_detail = get(jsonObject, "time_detail");
            curr_date = get(jsonObject, "curr_date");
            time_start = get(jsonObject, "time_start");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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

    public String getImgcount() {
        return imgcount;
    }

    public String getLeftcount() {
        return leftcount;
    }

    public ArrayList<Image> getImgItems() {
        return imgItems;
    }

    public int getSelectImgIndex() {
        return selectImgIndex;
    }

    public void setSelectImgIndex(int selectImgIndex) {
        this.selectImgIndex = selectImgIndex;
    }

    public void setImgurlbig(String imgurlbig) {
        this.imgurlbig = imgurlbig;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public void setLimit_count(String limit_count) {
        this.limit_count = limit_count;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImgurlbig() {
        return imgurlbig;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public String getLimit_count() {
        return limit_count;
    }

    public String getTime() {
        return time;
    }

    public String getTime_detail() {
        return time_detail;
    }

    public void setTime_detail(String time_detail) {
        this.time_detail = time_detail;
    }

    public String getCurr_date() {
        return curr_date;
    }

    public void setCurr_date(String curr_date) {
        this.curr_date = curr_date;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

//    public int getHours(){
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            Date now = sdf.parse(curr_date);
//            Date start = sdf.parse(time_start);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }
}
