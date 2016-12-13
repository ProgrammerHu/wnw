package com.hemaapp.wnw.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 退款详情模型
 * Created by HuHu on 2016/3/28.
 */
public class RefundGetModel extends XtomObject {
    private String bill_sn;
    private String id;
    private String bill_id;
    private String client_id;
    private String blog_id;
    private String reply_id;
    private String name;
    private String rule_id;
    private String rule;
    private String price;
    private String buycount;
    private String expressfee;
    private String shipping_fee;
    private String itemtype;
    private String servicetype;
    private String replytype;
    private String regdate;
    private String applydate;
    private String reason;
    private String description;
    private String imgcount;
    private String consignee;
    private String phone;
    private String address;
    private String imgurl;
    private String imgurlbig;
    private String nickname;
    private String mobile;
    private String coupon_fee;
    private String total;
    private String shipping_num;
    private String shipping_name;
    private String tradetype;
    private ArrayList<Image> imgItems = new ArrayList<>();

    public RefundGetModel(JSONObject jsonObject) {
        try {
            id = get(jsonObject, "id");
            bill_id = get(jsonObject, "bill_id");
            client_id = get(jsonObject, "client_id");
            blog_id = get(jsonObject, "blog_id");
            reply_id = get(jsonObject, "reply_id");
            name = get(jsonObject, "name");
            rule_id = get(jsonObject, "rule_id");
            rule = get(jsonObject, "rule");
            price = get(jsonObject, "price");
            buycount = get(jsonObject, "buycount");
            expressfee = get(jsonObject, "expressfee");
            shipping_fee = get(jsonObject, "shipping_fee");
            itemtype = get(jsonObject, "itemtype");
            servicetype = get(jsonObject, "servicetype");
            replytype = get(jsonObject, "replytype");
            regdate = get(jsonObject, "regdate");
            applydate = get(jsonObject, "applydate");
            reason = get(jsonObject, "reason");
            description = get(jsonObject, "description");
            imgcount = get(jsonObject, "imgcount");
            bill_sn = get(jsonObject, "bill_sn");
            consignee = get(jsonObject, "consignee");
            phone = get(jsonObject, "phone");
            address = get(jsonObject, "address");
            imgurl = get(jsonObject, "imgurl");
            imgurlbig = get(jsonObject, "imgurlbig");
            nickname = get(jsonObject, "nickname");
            mobile = get(jsonObject, "mobile");
            coupon_fee = get(jsonObject, "coupon_fee");
            total = get(jsonObject, "total");
            shipping_num = get(jsonObject, "shipping_num");
            shipping_name = get(jsonObject, "shipping_name");
            tradetype = get(jsonObject, "tradetype");
            replytype = get(jsonObject, "replytype");

            if (!jsonObject.isNull("imgItems")) {
                JSONArray jsonArray = jsonObject.getJSONArray("imgItems");
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        imgItems.add(new Image(jsonArray.getJSONObject(i)));
                    } catch (DataParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getBill_sn() {
        return bill_sn;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setBlog_id(String blog_id) {
        this.blog_id = blog_id;
    }

    public void setReply_id(String reply_id) {
        this.reply_id = reply_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRule_id(String rule_id) {
        this.rule_id = rule_id;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setBuycount(String buycount) {
        this.buycount = buycount;
    }

    public void setExpressfee(String expressfee) {
        this.expressfee = expressfee;
    }

    public void setShipping_fee(String shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public void setServicetype(String servicetype) {
        this.servicetype = servicetype;
    }

    public void setReplytype(String replytype) {
        this.replytype = replytype;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public void setApplydate(String applydate) {
        this.applydate = applydate;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImgcount(String imgcount) {
        this.imgcount = imgcount;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public void setImgurlbig(String imgurlbig) {
        this.imgurlbig = imgurlbig;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getId() {
        return id;
    }

    public String getTotal() {
        return total;
    }

    public String getBill_id() {

        return bill_id;
    }


    public String getClient_id() {
        return client_id;
    }

    public String getBlog_id() {
        return blog_id;
    }

    public String getReply_id() {
        return reply_id;
    }

    public String getName() {
        return name;
    }

    public String getRule_id() {
        return rule_id;
    }

    public String getRule() {
        return rule;
    }

    public String getPrice() {
        return price;
    }

    public String getBuycount() {
        return buycount;
    }

    public String getExpressfee() {
        return expressfee;
    }

    public String getShipping_fee() {
        return shipping_fee;
    }

    public String getItemtype() {
        return itemtype;
    }

    public String getServicetype() {
        return servicetype;
    }

    public String getReplytype() {
        return replytype;
    }

    public String getRegdate() {
        return regdate;
    }

    public String getApplydate() {
        return applydate;
    }

    public String getReason() {
        return reason;
    }

    public String getDescription() {
        return description;
    }

    public String getImgcount() {
        return imgcount;
    }

    public String getConsignee() {
        return consignee;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getImgurlbig() {
        return imgurlbig;
    }

    public String getNickname() {
        return nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public ArrayList<Image> getImgItems() {
        return imgItems;
    }

    public String getCoupon_fee() {
        return coupon_fee;
    }

    public String getShipping_num() {
        return shipping_num;
    }

    public String getShipping_name() {
        return shipping_name;
    }

    public String getTradetype() {
        return tradetype;
    }
}
