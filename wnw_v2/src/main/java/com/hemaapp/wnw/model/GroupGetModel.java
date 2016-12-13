package com.hemaapp.wnw.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import xtom.frame.XtomObject;

/**
 * 开团界面数据
 * Created by HuHu on 2016/3/25.
 */
public class GroupGetModel extends XtomObject {

    private String id;
    private String name;
    private String imgurl;
    private String imgurlbig;
    private String rule_name;
    private String leftcount;
    private List<ChildItemsEntity> childItems = new ArrayList<>();

    public GroupGetModel(JSONObject jsonObject) {
        try {
            id = jsonObject.getString("id");
            name = jsonObject.getString("name");
            imgurlbig = jsonObject.getString("imgurlbig");
            imgurl = jsonObject.getString("imgurl");
            rule_name = jsonObject.getString("rule_name");
            leftcount = jsonObject.getString("leftcount");
            if (!jsonObject.isNull("childItems")) {
                JSONArray childs = jsonObject.getJSONArray("childItems");
                for (int i = 0; i < childs.length(); i++) {
                    ChildItemsEntity child = new ChildItemsEntity(childs.getJSONObject(i));
                    childItems.add(child);
                    if (i == 0) {
                        child.setIsSelect(true);
                    }
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

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public void setImgurlbig(String imgurlbig) {
        this.imgurlbig = imgurlbig;
    }

    public void setRule_name(String rule_name) {
        this.rule_name = rule_name;
    }

    public void setChildItems(List<ChildItemsEntity> childItems) {
        this.childItems = childItems;
    }

    public String getId() {
        return id;
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

    public String getRule_name() {
        return rule_name;
    }

    public String getLeftcount() {
        return leftcount;
    }

    public List<ChildItemsEntity> getChildItems() {
        return childItems;
    }

    public static class ChildItemsEntity {
        private String id;
        private String merchant_id;
        private String blog_id;
        private String person;
        private String limit_person;
        private String rule_id;
        private String rule_name;
        private String price;
        private String group_price;
        private String hour;

        private boolean isSelect;

        public ChildItemsEntity(JSONObject jsonObject) {
            try {
                id = jsonObject.getString("id");
                merchant_id = jsonObject.getString("merchant_id");
                blog_id = jsonObject.getString("blog_id");
                person = jsonObject.getString("person");
                limit_person = jsonObject.getString("limit_person");
                rule_id = jsonObject.getString("rule_id");
                rule_name = jsonObject.getString("rule_name");
                price = jsonObject.getString("price");
                group_price = jsonObject.getString("group_price");
                hour = jsonObject.getString("hour");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            isSelect = false;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setIsSelect(boolean isSelect) {
            this.isSelect = isSelect;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setMerchant_id(String merchant_id) {
            this.merchant_id = merchant_id;
        }

        public void setBlog_id(String blog_id) {
            this.blog_id = blog_id;
        }

        public void setPerson(String person) {
            this.person = person;
        }

        public void setLimit_person(String limit_person) {
            this.limit_person = limit_person;
        }

        public void setRule_id(String rule_id) {
            this.rule_id = rule_id;
        }

        public void setRule_name(String rule_name) {
            this.rule_name = rule_name;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public void setGroup_price(String group_price) {
            this.group_price = group_price;
        }

        public void setHour(String hour) {
            this.hour = hour;
        }

        public String getId() {
            return id;
        }

        public String getMerchant_id() {
            return merchant_id;
        }

        public String getBlog_id() {
            return blog_id;
        }

        public String getPerson() {
            return person;
        }

        public String getLimit_person() {
            return limit_person;
        }

        public String getRule_id() {
            return rule_id;
        }

        public String getRule_name() {
            return rule_name;
        }

        public String getPrice() {
            return price;
        }

        public String getGroup_price() {
            return group_price;
        }

        public String getHour() {
            return hour;
        }
    }
}
