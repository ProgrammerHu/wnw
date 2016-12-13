package com.hemaapp.wnw.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import xtom.frame.XtomObject;

/**
 * 团购订单详情
 * Created by HuHu on 2016/3/28.
 */
public class GroupBillGetModel extends XtomObject {


//    private String person;
    private String price;
    private String rule;
    private String nickname;
    private String merchant_id;
    private String name;
    private String imgurl;
    private String imgurlbig;
    private String blog_id;
    private String id;
    private String group_id;
    private String bill_sn;
    private String regdate;
    private String total_fee;
    private String shippingfee;
    private String tradetype;
    private String consignee;
    private String phone;
    private String address;
    private String shipping_name;
    private String shipping_num;

    private ArrayList<ChildItemsEntity> childItems = new ArrayList<>();

    public GroupBillGetModel(JSONObject jsonObject) {
        try {
//            person = jsonObject.getString("person");
            price = jsonObject.getString("price");
            rule = jsonObject.getString("rule");
            nickname = jsonObject.getString("nickname");
            merchant_id = jsonObject.getString("merchant_id");
            name = jsonObject.getString("name");
            imgurl = jsonObject.getString("imgurl");
            imgurlbig = jsonObject.getString("imgurlbig");
            blog_id = jsonObject.getString("blog_id");
            id = jsonObject.getString("id");
            group_id = jsonObject.getString("group_id");
            bill_sn = jsonObject.getString("bill_sn");
            regdate = jsonObject.getString("regdate");
            total_fee = jsonObject.getString("total_fee");
            shippingfee = jsonObject.getString("shippingfee");
            tradetype = jsonObject.getString("tradetype");
            consignee = jsonObject.getString("consignee");
            phone = jsonObject.getString("phone");
            address = jsonObject.getString("address");
            if (!jsonObject.isNull("childItems")) {
                JSONArray jsonArray = jsonObject.getJSONArray("childItems");
                for (int i = 0; i < jsonArray.length(); i++) {
                    childItems.add(new ChildItemsEntity(jsonArray.getJSONObject(i)));
                }
            }
            if (!jsonObject.isNull("shipping_name")) {
                shipping_name = jsonObject.getString("shipping_name");
            }
            if (!jsonObject.isNull("shipping_num")) {
                shipping_num = jsonObject.getString("shipping_num");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getShipping_name() {
        if ("null".equals(shipping_name)) {
            return "";
        }
        return shipping_name;
    }

    public String getShipping_num() {
        if ("null".equals(shipping_num)) {
            return "";
        }
        return shipping_num;
    }

    public String getPrice() {
        return price;
    }

//    public void setPerson(String person) {
//        this.person = person;
//    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public void setImgurlbig(String imgurlbig) {
        this.imgurlbig = imgurlbig;
    }

    public void setBlog_id(String blog_id) {
        this.blog_id = blog_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public void setBill_sn(String bill_sn) {
        this.bill_sn = bill_sn;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public void setShippingfee(String shippingfee) {
        this.shippingfee = shippingfee;
    }

    public void setTradetype(String tradetype) {
        this.tradetype = tradetype;
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

//    public String getPerson() {
//        return person;
//    }

    public String getRule() {
        return rule;
    }

    public String getNickname() {
        return nickname;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public String getName() {
        return name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getImgurlbig() {
        return imgurlbig;
    }

    public String getBlog_id() {
        return blog_id;
    }

    public String getId() {
        return id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public String getBill_sn() {
        return bill_sn;
    }

    public String getRegdate() {
        return regdate;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public String getShippingfee() {
        return shippingfee;
    }

    public String getTradetype() {
        return tradetype;
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

    public ArrayList<ChildItemsEntity> getChildItems() {
        return childItems;
    }

    public static class ChildItemsEntity {
        private String id;
        private String bill_sn;
        private String group_id;
        private String client_id;
        private String type;
        private String payflag;
        private String tradetype;
        private String statetype;
        private String goods_fee;
        private String promote_price;
        private String shippingfee;
        private String total_fee;
        private String consignee;
        private String phone;
        private String address;
        private String regdate;
        private String buydate;
        private String nickname;
        private String avatar;

        public ChildItemsEntity(JSONObject jsonObject) {
            try {
                id = jsonObject.getString("id");
                bill_sn = jsonObject.getString("bill_sn");
                group_id = jsonObject.getString("group_id");
                client_id = jsonObject.getString("client_id");
                type = jsonObject.getString("type");
                payflag = jsonObject.getString("payflag");
                tradetype = jsonObject.getString("tradetype");
                statetype = jsonObject.getString("statetype");
                goods_fee = jsonObject.getString("goods_fee");
                promote_price = jsonObject.getString("promote_price");
                shippingfee = jsonObject.getString("shippingfee");
                total_fee = jsonObject.getString("total_fee");
                consignee = jsonObject.getString("consignee");
                phone = jsonObject.getString("phone");
                address = jsonObject.getString("address");
                regdate = jsonObject.getString("regdate");
                buydate = jsonObject.getString("buydate");
                nickname = jsonObject.getString("nickname");
                avatar = jsonObject.getString("avatar");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setBill_sn(String bill_sn) {
            this.bill_sn = bill_sn;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

        public void setClient_id(String client_id) {
            this.client_id = client_id;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setPayflag(String payflag) {
            this.payflag = payflag;
        }

        public void setTradetype(String tradetype) {
            this.tradetype = tradetype;
        }

        public void setStatetype(String statetype) {
            this.statetype = statetype;
        }

        public void setGoods_fee(String goods_fee) {
            this.goods_fee = goods_fee;
        }

        public void setPromote_price(String promote_price) {
            this.promote_price = promote_price;
        }

        public void setShippingfee(String shippingfee) {
            this.shippingfee = shippingfee;
        }

        public void setTotal_fee(String total_fee) {
            this.total_fee = total_fee;
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

        public void setRegdate(String regdate) {
            this.regdate = regdate;
        }

        public void setBuydate(String buydate) {
            this.buydate = buydate;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getId() {
            return id;
        }

        public String getBill_sn() {
            return bill_sn;
        }

        public String getGroup_id() {
            return group_id;
        }

        public String getClient_id() {
            return client_id;
        }

        public String getType() {
            return type;
        }

        public String getPayflag() {
            return payflag;
        }

        public String getTradetype() {
            return tradetype;
        }

        public String getStatetype() {
            return statetype;
        }

        public String getGoods_fee() {
            return goods_fee;
        }

        public String getPromote_price() {
            return promote_price;
        }

        public String getShippingfee() {
            return shippingfee;
        }

        public String getTotal_fee() {
            return total_fee;
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

        public String getRegdate() {
            return regdate;
        }

        public String getBuydate() {
            return buydate;
        }

        public String getNickname() {
            return nickname;
        }

        public String getAvatar() {
            return avatar;
        }
    }
}
