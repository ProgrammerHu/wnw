package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;

/**
 * 广告模型
 * Created by Hufanglin on 2016/2/22.
 */
public class AdModel extends XtomObject {

    private String id;
    private String name;
    /**
     * 广告链接类型	1：根据keyid跳转到文章详情
     * 2：根据keyid跳转问卷详情
     * 3：根据keyid跳转webview页
     */
    private String keytype;
    private String keyid;
    private String imgurl;
    private String imgurlbig;
    /**
     * 是否显示 1,显示
     */
    private String is_show;
    private String orderby;
    private String regdate;

    public AdModel(String id, String name, String keytype, String keyid,
                   String imgurl, String imgurlbig, String is_show,
                   String orderby, String regdate) {
        this.id = id;
        this.name = name;
        this.keytype = keytype;
        this.keyid = keyid;
        this.imgurl = imgurl;
        this.imgurlbig = imgurlbig;
        this.is_show = is_show;
        this.orderby = orderby;
        this.regdate = regdate;
    }

    public AdModel(JSONObject jsonObject) {
        try {
            id = jsonObject.getString("id");
            name = jsonObject.getString("name");
            keytype = jsonObject.getString("keytype");
            keyid = jsonObject.getString("keyid");
            imgurl = jsonObject.getString("imgurl");
            imgurlbig = jsonObject.getString("imgurlbig");
            is_show = jsonObject.getString("is_show");
            orderby = jsonObject.getString("orderby");
            regdate = jsonObject.getString("regdate");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeytype() {
        return keytype;
    }

    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

    public String getKeyid() {
        return keyid;
    }

    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getImgurlbig() {
        return imgurlbig;
    }

    public void setImgurlbig(String imgurlbig) {
        this.imgurlbig = imgurlbig;
    }

    public String getIs_show() {
        return is_show;
    }

    public void setIs_show(String is_show) {
        this.is_show = is_show;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

}
