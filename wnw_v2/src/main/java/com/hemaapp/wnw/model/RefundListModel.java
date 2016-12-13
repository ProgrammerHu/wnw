package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;

/**
 * 退款列表模型
 * Created by HuHu on 2016/3/28.
 */
public class RefundListModel extends XtomObject {

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
    private String imgurl;
    private String imgurlbig;

    public RefundListModel(JSONObject jsonObject) {
        try {
            bill_sn = jsonObject.getString("bill_sn");
            id = jsonObject.getString("id");
            bill_id = jsonObject.getString("bill_id");
            client_id = jsonObject.getString("client_id");
            blog_id = jsonObject.getString("blog_id");
            reply_id = jsonObject.getString("reply_id");
            name = jsonObject.getString("name");
            rule_id = jsonObject.getString("rule_id");
            rule = jsonObject.getString("rule");
            price = jsonObject.getString("price");
            buycount = jsonObject.getString("buycount");
            expressfee = jsonObject.getString("expressfee");
            shipping_fee = jsonObject.getString("shipping_fee");
            itemtype = jsonObject.getString("itemtype");
            servicetype = jsonObject.getString("servicetype");
            replytype = jsonObject.getString("replytype");
            regdate = jsonObject.getString("regdate");
            applydate = jsonObject.getString("applydate");
            reason = jsonObject.getString("reason");
            description = jsonObject.getString("description");
            imgcount = jsonObject.getString("imgcount");
            imgurl = jsonObject.getString("imgurl");
            imgurlbig = jsonObject.getString("imgurlbig");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getImgurlbig() {
        return imgurlbig;
    }

    public void setBill_sn(String bill_sn) {
        this.bill_sn = bill_sn;
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

    public String getBill_sn() {
        return bill_sn;
    }

    public String getId() {
        return id;
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
}
