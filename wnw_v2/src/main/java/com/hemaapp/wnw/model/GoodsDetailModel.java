package com.hemaapp.wnw.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import xtom.frame.XtomObject;

/**
 * 商品详情 Created by Hufanglin on 2016/3/6.
 */
public class GoodsDetailModel extends XtomObject implements Serializable {

    private static final long serialVersionUID = 1532707465915761987L;
    private String name;
    private String id;
    private String price;
    private String oldprice;
    private String content;
    private String paycount;
    private String leftcount;
    private String group_flag;
    private String imgcount;
    private String imgurl;
    private String imgurlbig;
    private String expressfee;
    private String nickname;
    private String avatar;
    private String fans;
    private String merchant_id;
    private String love_flag;
    private String promote_price;
    /**
     * id : 351 client_id : 0 name : keytype : 3 keyid : 1 imgurl :
     * http://115.28.238.61/uploadfiles/2015/07/201507161322191775_thumb.png
     * imgurlbig :
     * http://115.28.238.61/uploadfiles/2015/07/201507161322191775.jpg videourl
     * : null videosize : 0.0 second : 0 orderby : 0 regdate : 2015-07-16
     * 13:22:19
     */

    private ArrayList<ImgItemsEntity> imgItems = new ArrayList<>();
    /**
     * id : 2 name : 规格 atrr : 150ml
     */

    private ArrayList<AtrrItemsEntity> atrrItems = new ArrayList<>();
    /**
     * rule_id : 2 mix : 2 mix_two : 2 price : 100.00
     */

    private ArrayList<PriceItemsEntity> priceItems = new ArrayList<>();
    /**
     * saleflag : 0
     * keytype : 1
     * time : 0000-00-00
     * limit_count :
     * time_detail :
     * time_end :
     * country_name :
     */

    private String saleflag;
    private String keytype;
    private String time;
    private String limit_count;
    private String time_detail;
    private String time_end;
    private String country_name;
    private String curr_date;

    public GoodsDetailModel(JSONObject jsonObject) {
        try {
            name = jsonObject.getString("name");
            id = jsonObject.getString("id");
            price = jsonObject.getString("price");
            oldprice = jsonObject.getString("oldprice");
            content = jsonObject.getString("content");
            paycount = jsonObject.getString("paycount");
            leftcount = jsonObject.getString("leftcount");
            group_flag = jsonObject.getString("group_flag");
            imgcount = jsonObject.getString("imgcount");
            imgurl = jsonObject.getString("imgurl");
            imgurlbig = jsonObject.getString("imgurlbig");
            expressfee = jsonObject.getString("expressfee");
            nickname = jsonObject.getString("nickname");
            avatar = jsonObject.getString("avatar");
            fans = jsonObject.getString("fans");
            merchant_id = jsonObject.getString("merchant_id");
            love_flag = jsonObject.getString("love_flag");

            saleflag = jsonObject.getString("saleflag");
            keytype = jsonObject.getString("keytype");
            time = jsonObject.getString("time");
            limit_count = jsonObject.getString("limit_count");
            time_detail = jsonObject.getString("time_detail");
            time_end = jsonObject.getString("time_end");
            country_name = jsonObject.getString("country_name");
            curr_date = get(jsonObject, "curr_date");

            if (!jsonObject.isNull("promote_price"))
                promote_price = jsonObject.getString("promote_price");
            if (!jsonObject.isNull("imgItems") && !isNull(jsonObject.getString("imgItems"))) {
                JSONArray jsonArray = jsonObject.getJSONArray("imgItems");
                for (int i = 0; i < jsonArray.length(); i++) {
                    imgItems.add(new ImgItemsEntity(jsonArray.getJSONObject(i)));
                }

            }
            if (!jsonObject.isNull("atrrItems") && !isNull(jsonObject.getString("atrrItems"))) {
                JSONArray jsonArray = jsonObject.getJSONArray("atrrItems");
                for (int i = 0; i < jsonArray.length(); i++) {
                    atrrItems.add(new AtrrItemsEntity(jsonArray
                            .getJSONObject(i)));
                }
            }
            if (!jsonObject.isNull("priceItems") && !isNull(jsonObject.getString("priceItems"))) {
                JSONArray jsonArray = jsonObject.getJSONArray("priceItems");
                for (int i = 0; i < jsonArray.length(); i++) {
                    priceItems.add(new PriceItemsEntity(jsonArray
                            .getJSONObject(i)));
                }
            }
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

    public String getPromote_price() {
        return promote_price;
    }

    public void setPromote_price(String promote_price) {
        this.promote_price = promote_price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setOldprice(String oldprice) {
        this.oldprice = oldprice;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPaycount(String paycount) {
        this.paycount = paycount;
    }

    public void setLeftcount(String leftcount) {
        this.leftcount = leftcount;
    }

    public void setGroup_flag(String group_flag) {
        this.group_flag = group_flag;
    }

    public void setImgcount(String imgcount) {
        this.imgcount = imgcount;
    }

    public String getExpressfee() {
        return expressfee;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public void setLove_flag(String love_flag) {
        this.love_flag = love_flag;
    }

    public void setImgItems(ArrayList<ImgItemsEntity> imgItems) {
        this.imgItems = imgItems;
    }

    public void setAtrrItems(ArrayList<AtrrItemsEntity> atrrItems) {
        this.atrrItems = atrrItems;
    }

    public void setPriceItems(ArrayList<PriceItemsEntity> priceItems) {
        this.priceItems = priceItems;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public String getOldprice() {
        return oldprice;
    }

    public String getContent() {
        return content;
    }

    public String getPaycount() {
        return paycount;
    }

    public String getLeftcount() {
        return leftcount;
    }

    public String getGroup_flag() {
        return group_flag;
    }

    public String getImgcount() {
        return imgcount;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getFans() {
        return fans;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public String getLove_flag() {
        return love_flag;
    }

    public ArrayList<ImgItemsEntity> getImgItems() {
        return imgItems;
    }

    public ArrayList<AtrrItemsEntity> getAtrrItems() {
        return atrrItems;
    }

    public ArrayList<PriceItemsEntity> getPriceItems() {
        return priceItems;
    }

    public void setSaleflag(String saleflag) {
        this.saleflag = saleflag;
    }

    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLimit_count(String limit_count) {
        this.limit_count = limit_count;
    }

    public void setTime_detail(String time_detail) {
        this.time_detail = time_detail;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getSaleflag() {
        return saleflag;
    }

    public String getKeytype() {
        return keytype;
    }

    public String getTime() {
        return time;
    }

    public String getLimit_count() {
        return limit_count;
    }

    public String getTime_detail() {
        return time_detail;
    }

    public String getTime_end() {
        return time_end;
    }

    public String getCountry_name() {
        return country_name;
    }

    public String getCurr_date() {
        return curr_date;
    }

    public void setCurr_date(String curr_date) {
        this.curr_date = curr_date;
    }

    public static class ImgItemsEntity {
        private String id;
        private String client_id;
        private String name;
        private String keytype;
        private String keyid;
        private String imgurl;
        private String imgurlbig;
        private String videourl;
        private String videosize;
        private String second;
        private String orderby;
        private String regdate;

        public ImgItemsEntity(JSONObject jsonObject) {
            try {
                id = jsonObject.getString("id");
                client_id = jsonObject.getString("client_id");
                name = jsonObject.getString("name");
                keytype = jsonObject.getString("keytype");
                keyid = jsonObject.getString("keyid");
                imgurl = jsonObject.getString("imgurl");
                imgurlbig = jsonObject.getString("imgurlbig");
                orderby = jsonObject.getString("orderby");
                regdate = jsonObject.getString("regdate");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setClient_id(String client_id) {
            this.client_id = client_id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setKeytype(String keytype) {
            this.keytype = keytype;
        }

        public void setKeyid(String keyid) {
            this.keyid = keyid;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public void setImgurlbig(String imgurlbig) {
            this.imgurlbig = imgurlbig;
        }

        public void setVideourl(String videourl) {
            this.videourl = videourl;
        }

        public void setVideosize(String videosize) {
            this.videosize = videosize;
        }

        public void setSecond(String second) {
            this.second = second;
        }

        public void setOrderby(String orderby) {
            this.orderby = orderby;
        }

        public void setRegdate(String regdate) {
            this.regdate = regdate;
        }

        public String getId() {
            return id;
        }

        public String getClient_id() {
            return client_id;
        }

        public String getName() {
            return name;
        }

        public String getKeytype() {
            return keytype;
        }

        public String getKeyid() {
            return keyid;
        }

        public String getImgurl() {
            return imgurl;
        }

        public String getImgurlbig() {
            return imgurlbig;
        }

        public String getVideourl() {
            return videourl;
        }

        public String getVideosize() {
            return videosize;
        }

        public String getSecond() {
            return second;
        }

        public String getOrderby() {
            return orderby;
        }

        public String getRegdate() {
            return regdate;
        }
    }

    /**
     * 参数选项
     */
    public static class AtrrItemsEntity {
        private String id;
        private String name;
        private ArrayList<AttrChildItemsEntity> atrrChildItems = new ArrayList<>();

        public AtrrItemsEntity(JSONObject jsonObject) {
            try {
                id = jsonObject.getString("id");
                name = jsonObject.getString("name");
                if (!jsonObject.isNull("childItems")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("childItems");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        atrrChildItems.add(new AttrChildItemsEntity(jsonArray.getJSONObject(i)));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public ArrayList<AttrChildItemsEntity> getAtrr() {
            return atrrChildItems;
        }
    }

    /**
     * 标签种类下的元素
     */
    public static class AttrChildItemsEntity implements Serializable {
        private static final long serialVersionUID = -7100853363751899320L;
        private String name;
        private String attr_id;
        private boolean is_selected;

        public AttrChildItemsEntity(JSONObject jsonObject) {
            try {
                name = jsonObject.getString("name");
                attr_id = jsonObject.getString("attr_id");
                is_selected = false;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public boolean getIs_selected() {
            return is_selected;
        }

        public void setIs_selected(boolean is_selected) {
            this.is_selected = is_selected;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAttr_id() {
            return attr_id;
        }

        public void setAttr_id(String attr_id) {
            this.attr_id = attr_id;
        }
    }

    public static class PriceItemsEntity implements Serializable {
        private static final long serialVersionUID = -5879126469782089498L;
        private String rule_id;
        private String mix;
        private String mix_two;
        private String price;
        private String leftcount;

        public PriceItemsEntity(JSONObject jsonObject) {
            try {
                rule_id = jsonObject.getString("rule_id");
                mix = jsonObject.getString("mix");
                mix_two = jsonObject.getString("mix_two");
                price = jsonObject.getString("price");
                leftcount = jsonObject.getString("leftcount");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public String getLeftcount() {
            return leftcount;
        }

        public void setRule_id(String rule_id) {
            this.rule_id = rule_id;
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

        public String getRule_id() {
            return rule_id;
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
    }
}
