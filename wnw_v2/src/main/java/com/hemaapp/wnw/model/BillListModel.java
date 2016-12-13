package com.hemaapp.wnw.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import xtom.frame.XtomObject;

/**
 * 订单列表
 * Created by Hufanglin on 2016/3/14.
 */
public class BillListModel extends XtomObject {
    private String id;
    private String bill_sn;
    private String client_id;
    private String merchant_id;
    private String payflag;
    private String tradetype;
    private String statetype;
    private String returnflag;
    private String consignee;
    private String phone;
    private String address;
    private String goods_fee;
    private String promote_price;
    private String shipping_fee;
    private String total_fee;
    private String total_count;
    private String memo;
    private String shipping_name;
    private String shipping_num;
    private String regdate;
    private String buydate;
    private String senddate;
    private String recvdate;
    private String final_fee;
    private String nickname;
    private String total;
    private String coupon_fee;

    private String sendflag;
    private String time;
    private String limit_date;
    private String clearflag;
    private String commion_fee;
    private String ac_fee;
    private String refund_fee;
    private String refund_fee_max;
    private String refund_shipping;
    private String type;


    private List<ChildItemsEntity> childItems = new ArrayList<>();


    public BillListModel(JSONObject jsonObject) {
        try {
            id = get(jsonObject, "id");
            bill_sn = get(jsonObject, "bill_sn");
            client_id = get(jsonObject, "client_id");
            merchant_id = get(jsonObject, "merchant_id");
            payflag = get(jsonObject, "payflag");
            tradetype = get(jsonObject, "tradetype");
            statetype = get(jsonObject, "statetype");
            returnflag = get(jsonObject, "returnflag");
            consignee = get(jsonObject, "consignee");
            phone = get(jsonObject, "phone");
            address = get(jsonObject, "address");
            goods_fee = get(jsonObject, "goods_fee");
            promote_price = get(jsonObject, "promote_price");
            shipping_fee = get(jsonObject, "shipping_fee");
            if (isNull(shipping_fee) || "null".equals(shipping_fee)) {
                shipping_fee = "0.00";
            }
            total_fee = get(jsonObject, "total_fee");
            total_count = get(jsonObject, "total_count");
            memo = get(jsonObject, "memo");
            shipping_name = get(jsonObject, "shipping_name");
            shipping_num = get(jsonObject, "shipping_num");
            regdate = get(jsonObject, "regdate");
            buydate = get(jsonObject, "buydate");
            senddate = get(jsonObject, "senddate");
            recvdate = get(jsonObject, "recvdate");
            final_fee = get(jsonObject, "final_fee");
            nickname = get(jsonObject, "nickname");
            limit_date = get(jsonObject, "limit_date");
            type = get(jsonObject, "type");

            if (!jsonObject.isNull("total")) {
                total = get(jsonObject, "total");
            }
            if (!jsonObject.isNull("coupon_fee")) {
                coupon_fee = get(jsonObject, "coupon_fee");
            }
            if (!jsonObject.isNull("childItems")) {
                JSONArray array = jsonObject.getJSONArray("childItems");
                for (int i = 0; i < array.length(); i++) {
                    childItems.add(new ChildItemsEntity(array.getJSONObject(i)));
                }
            }
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

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
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

    public void setReturnflag(String returnflag) {
        this.returnflag = returnflag;
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

    public void setGoods_fee(String goods_fee) {
        this.goods_fee = goods_fee;
    }

    public void setPromote_price(String promote_price) {
        this.promote_price = promote_price;
    }

    public void setShipping_fee(String shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setShipping_name(String shipping_name) {
        this.shipping_name = shipping_name;
    }

    public void setShipping_num(String shipping_num) {
        this.shipping_num = shipping_num;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public void setBuydate(String buydate) {
        this.buydate = buydate;
    }

    public void setSenddate(String senddate) {
        this.senddate = senddate;
    }

    public void setRecvdate(String recvdate) {
        this.recvdate = recvdate;
    }


    public void setFinal_fee(String final_fee) {
        this.final_fee = final_fee;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCoupon_fee() {
        return coupon_fee;
    }

    public void setChildItems(List<ChildItemsEntity> childItems) {
        this.childItems = childItems;
    }

    public String getId() {
        return id;
    }

    public String getBill_sn() {
        return bill_sn;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getMerchant_id() {
        return merchant_id;
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

    public String getReturnflag() {
        return returnflag;
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

    public String getGoods_fee() {
        /*double fee = 0;
        for (ChildItemsEntity child : getChildItems()) {
            try {
                fee += Double.parseDouble(child.getBuycount()) * Double.parseDouble(child.getPrice());
            } catch (Exception e) {
            }
        }
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(fee);*/
        return goods_fee;
    }

    public String getPromote_price() {
        return promote_price;
    }

    public String getShipping_fee() {
        return shipping_fee;
    }

    public String getTotal_fee() {//自己算总价
        /*double fee = 0;
        for (ChildItemsEntity child : getChildItems()) {
            try {
                fee += Double.parseDouble(child.getBuycount()) * Double.parseDouble(child.getPrice());
            } catch (Exception e) {
            }
        }
        try {
            fee += (Double.parseDouble(getShipping_fee()) - Double.parseDouble(getCoupon_fee()));
        } catch (Exception e) {
        }
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(fee);*/

        return total_fee;
    }

    /**
     * 获取不考虑抵用券的金额
     *
     * @return
     */
   /* public String getTotal_feeButCoupon() {
        double fee = 0;
        for (ChildItemsEntity child : getChildItems()) {
            try {
                fee += Double.parseDouble(child.getBuycount()) * Double.parseDouble(child.getPrice());
            } catch (Exception e) {
            }
        }
        try {
            fee += Double.parseDouble(getShipping_fee());
        } catch (Exception e) {
        }
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(fee);
    }*/
    public String getMemo() {
        return memo;
    }

    public String getShipping_name() {
        if ("null".equals(shipping_name.toLowerCase())) {
            return "";
        }
        return shipping_name;
    }

    public String getShipping_num() {
        if ("null".equals(shipping_num.toLowerCase())) {
            return "";
        }
        return shipping_num;
    }

    public String getRegdate() {
        return regdate;
    }

    public String getBuydate() {
        return buydate;
    }

    public String getSenddate() {
        return senddate;
    }

    public String getRecvdate() {
        return recvdate;
    }

    public String getFinal_fee() {
        return final_fee;
    }

    public String getTotal() {
        return total;
    }


    public String getTotal_count() {
//        if (isNull(total_count) || "0".equals(total_count)) {
//
//        }
//        int count = 0;
//        for (ChildItemsEntity child : getChildItems()) {
//            if ("2".equals(child.getItemtype())) {
//                continue;
//            }
//            try {
//                count += Integer.parseInt(child.getBuycount());
//            } catch (Exception e) {
//
//            }
//        }
//        return String.valueOf(count);

        return total_count;
    }

    public String getNickname() {
        return nickname;
    }

    public List<ChildItemsEntity> getChildItems() {
        return childItems;
    }

    public void setSendflag(String sendflag) {
        this.sendflag = sendflag;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLimit_date(String limit_date) {
        this.limit_date = limit_date;
    }

    public void setClearflag(String clearflag) {
        this.clearflag = clearflag;
    }

    public void setCommion_fee(String commion_fee) {
        this.commion_fee = commion_fee;
    }

    public void setAc_fee(String ac_fee) {
        this.ac_fee = ac_fee;
    }

    public void setRefund_fee(String refund_fee) {
        this.refund_fee = refund_fee;
    }

    public void setRefund_fee_max(String refund_fee_max) {
        this.refund_fee_max = refund_fee_max;
    }

    public void setRefund_shipping(String refund_shipping) {
        this.refund_shipping = refund_shipping;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSendflag() {
        return sendflag;
    }

    public String getTime() {
        return time;
    }

    public String getLimit_date() {
        return limit_date;
    }

    public String getClearflag() {
        return clearflag;
    }

    public String getCommion_fee() {
        return commion_fee;
    }

    public String getAc_fee() {
        return ac_fee;
    }

    public String getRefund_fee() {
        return refund_fee;
    }

    public String getRefund_fee_max() {
        return refund_fee_max;
    }

    public String getRefund_shipping() {
        return refund_shipping;
    }

    public String getType() {
        return type;
    }

    public static class ChildItemsEntity extends XtomObject {
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
        private String itemtype;//	0.正常1.进行中2.成功3.失败
        private String servicetype;
        private String replytype;
        private String regdate;
        private String applydate;
        private String reason;
        private String description;
        private String handledate;
        private String memo;
        private String imgcount;
        private String imgurl;
        private String imgurlbig;

        private String handle;
        private String coupon_fee;
        private String refund_fee;
        private String flag;
        private String keytype;

        public ChildItemsEntity(JSONObject jsonObject) {
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
                handledate = get(jsonObject, "handledate");
                memo = get(jsonObject, "memo");
                imgcount = get(jsonObject, "imgcount");
                imgurl = get(jsonObject, "imgurl");
                imgurlbig = get(jsonObject, "imgurlbig");

                handle = get(jsonObject, "handle");
                coupon_fee = get(jsonObject, "coupon_fee");
                refund_fee = get(jsonObject, "refund_fee");
                flag = get(jsonObject, "flag");
                keytype = get(jsonObject, "keytype");
            } catch (JSONException e) {
                e.printStackTrace();
            }

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

        public void setHandledate(String handledate) {
            this.handledate = handledate;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public void setImgcount(String imgcount) {
            this.imgcount = imgcount;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public void setImgurlbig(String imgurlbig) {
            this.imgurlbig = imgurlbig;
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

        public String getHandledate() {
            return handledate;
        }

        public String getMemo() {
            return memo;
        }

        public String getImgcount() {
            return imgcount;
        }

        public String getImgurl() {
            return imgurl;
        }

        public String getImgurlbig() {
            return imgurlbig;
        }

        public String getHandle() {
            return handle;
        }

        public void setHandle(String handle) {
            this.handle = handle;
        }

        public String getCoupon_fee() {
            return coupon_fee;
        }

        public void setCoupon_fee(String coupon_fee) {
            this.coupon_fee = coupon_fee;
        }

        public String getRefund_fee() {
            return refund_fee;
        }

        public void setRefund_fee(String refund_fee) {
            this.refund_fee = refund_fee;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getKeytype() {
            return keytype;
        }

        public void setKeytype(String keytype) {
            this.keytype = keytype;
        }
    }
}
