package com.hemaapp.wnw.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 商家商品列表
 * Created by HuHu on 2016-09-08.
 */
public class MerchantGoodsGetModel extends MerchantGoodsListModel implements Serializable {
    private static final long serialVersionUID = 1062649512751446040L;

    private String type_id;
    private String content;
    private String expressfee;
    private String keytype;
    private String time_id;
    private String country_id;
    private String country_name;
    private String time_detail;
    private String type_name;
    private ArrayList<String[]> imgItem = new ArrayList<>();
    private ArrayList<GoodsParamsModel> atrrItems = new ArrayList<>();


    public MerchantGoodsGetModel(JSONObject jsonObject) {
        super(jsonObject);
        try {
            type_id = get(jsonObject, "type_id");
            type_name = get(jsonObject, "type_name");
            country_name = get(jsonObject, "country_name");
            expressfee = get(jsonObject, "expressfee");
            content = get(jsonObject, "content");

            keytype = get(jsonObject, "keytype");
            time_id = get(jsonObject, "time_id");
            country_id = get(jsonObject, "country_id");
            country_name = get(jsonObject, "country_name");
            type_name = get(jsonObject, "type_name");
            time_detail = get(jsonObject, "time_detail");

            if (!jsonObject.isNull("imgItems") && !isNull(jsonObject.getString("imgItems"))) {
                JSONArray jsonArray = jsonObject.getJSONArray("imgItems");
                for (int i = 0; i < jsonArray.length(); i++) {
                    String[] img = new String[2];
                    img[0] = jsonArray.getJSONObject(i).getString("imgurlbig");
                    img[1] = jsonArray.getJSONObject(i).getString("imgurl");
                    imgItem.add(img);
                }
            }
            if (!jsonObject.isNull("atrrItems") && !isNull(jsonObject.getString("atrrItems"))) {
                JSONArray jsonArray = jsonObject.getJSONArray("atrrItems");
                for (int i = 0; i < jsonArray.length(); i++) {
                    atrrItems.add(new GoodsParamsModel(jsonArray.getJSONObject(i)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String[]> getImgItem() {
        ArrayList<String[]> result = new ArrayList<>();
        String[] image = new String[2];
        image[0] = getImgurlbig();
        image[1] = getImgurl();
        result.add(image);
        result.addAll(imgItem);
        return result;
    }

    public void setImgItem(ArrayList<String[]> imgItem) {
        this.imgItem = imgItem;
    }

    public ArrayList<GoodsParamsModel> getAtrrItems() {
        return atrrItems;
    }

    public void setAtrrItems(ArrayList<GoodsParamsModel> atrrItems) {
        this.atrrItems = atrrItems;
    }

    public String getatrrItemsStr() {
        String result = "";
        for (GoodsParamsModel model : atrrItems) {
            if (isNull(result)) {
                result = model.getParam_name();
            } else {
                result += "," + model.getParam_name();
            }
        }
        return result;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setExpressfee(String expressfee) {
        this.expressfee = expressfee;
    }

    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

    public void setTime_id(String time_id) {
        this.time_id = time_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getType_id() {
        return type_id;
    }

    public String getContent() {
        return content;
    }

    public String getExpressfee() {
        return expressfee;
    }

    public String getKeytype() {
        return keytype;
    }

    public String getTime_id() {
        return time_id;
    }

    public String getCountry_id() {
        return country_id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public String getType_name() {
        return type_name;
    }

    public String getTime_detail() {
        return time_detail;
    }

    public void setTime_detail(String time_detail) {
        this.time_detail = time_detail;
    }

    public void clearParamsIds() {
        for (GoodsParamsModel model : atrrItems) {
            model.setParam_id("0");
            for (GoodsParamsModel.ParamsChild child : model.getChildren()) {
                child.setId("0");
            }
        }
    }
}
