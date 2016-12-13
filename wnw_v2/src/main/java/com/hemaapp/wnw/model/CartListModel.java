package com.hemaapp.wnw.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

import xtom.frame.XtomObject;

/**
 * 购物车列表
 * Created by Hufanglin on 2016/3/15.
 */
public class CartListModel extends XtomObject implements Serializable, Cloneable {

    private static final long serialVersionUID = -8927857000382814915L;

    private String merchant_id;
    private String merchant_name;
    private String promote_price;
    private double my_pay_count;
    private double my_all_expressfee;
    private boolean isSelected = false;
    private String keytype;

    /**
     * 获取商品数量
     *
     * @return
     */
    public int getChildCount() {
        int count = 0;
        for (ChildItemsEntity child : childItems) {
            try {
                int mCount = Integer.parseInt(child.getBuycount());
                count += mCount;
            } catch (Exception e) {
            }
        }
        return count;
    }

    public CartListModel clone() {
        CartListModel cloneObj = null;
        try {
            cloneObj = (CartListModel) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return cloneObj;
    }

    private ArrayList<ChildItemsEntity> childItems = new ArrayList<>();

    public CartListModel(String merchant_id, String merchant_name, String promote_price, ChildItemsEntity child, String keytype) {
        this.merchant_id = merchant_id;
        this.merchant_name = merchant_name;
        this.promote_price = promote_price;
        this.keytype = keytype;
        childItems.add(child);
    }

    public CartListModel(String merchant_id, String merchant_name, String promote_price, ArrayList<ChildItemsEntity> childItems) {
        this.merchant_id = merchant_id;
        this.merchant_name = merchant_name;
        this.promote_price = promote_price;
        this.childItems.addAll(childItems);
    }

    public CartListModel(JSONObject jsonObject) {
        try {
            merchant_id = jsonObject.getString("merchant_id");
            merchant_name = jsonObject.getString("merchant_name");
            promote_price = jsonObject.getString("promote_price");
            isSelected = false;
            if (!jsonObject.isNull("childItems")) {
                JSONArray jsonArray = jsonObject.getJSONArray("childItems");
                for (int i = 0; i < jsonArray.length(); i++) {
                    childItems.add(new ChildItemsEntity(jsonArray.getJSONObject(i)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新价格和运费
     */
    private void refreshPayCount() {
        if (childItems == null || childItems.size() == 0) {
            return;
        }
        my_pay_count = 0;//总价归零
        my_all_expressfee = 0;//运费归零
        for (ChildItemsEntity item : childItems) {
            double price = Double.valueOf(item.getPrice());
            double expressfee = Double.valueOf(item.getExpressfee());
            double buycount = Double.valueOf(item.getBuycount());
            my_pay_count += (price * buycount);
            my_all_expressfee += (expressfee * buycount);
        }
        if (isNull(promote_price) || "0".equals(promote_price) || "null".equals(promote_price)
                || my_pay_count < Integer.valueOf(promote_price)) {//不包邮，正常计算运费||总价款<包邮起点，不包邮
            //什么也不做了吧
            my_pay_count += my_all_expressfee;
        } else {//运费是0
            my_all_expressfee = 0;
        }
    }

    /**
     * 获取总价格
     *
     * @return String[0]支付总额 String[1]运费
     */
    public String[] getMyCount() {
        refreshPayCount();//先刷新数据
        String[] strings = new String[2];
        DecimalFormat df = new DecimalFormat("0.00");
        strings[0] = df.format(my_pay_count);
        strings[1] = df.format(my_all_expressfee);
        return strings;
    }

    public double getMy_pay_count() {
        refreshPayCount();//先刷新数据
        return my_pay_count;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * 批量修改子集
     *
     * @param isSelected
     */
    public void setAllChildrenIsSelect(boolean isSelected) {
        for (ChildItemsEntity item : childItems) {
            item.setIsSelected(isSelected);
        }
    }

    public String getPromote_price() {
        return promote_price;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public void setChildItems(ArrayList<ChildItemsEntity> childItems) {
        this.childItems = childItems;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public ArrayList<ChildItemsEntity> getChildItems() {
        return childItems;
    }

    public String getKeytype() {
        return keytype;
    }

    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

    public static class ChildItemsEntity implements Serializable, Cloneable {
        private static final long serialVersionUID = 1786948937074358951L;
        private String id;
        private String keyid;
        private String name;
        private String rule_id;
        private String rule;
        private String buycount;
        private String statetype;
        private String imgurl;
        private String imgurlbig;
        private String price;
        private String saleflag;
        private String expressfee;
        private boolean isSelected = false;//虚拟字段

        public ChildItemsEntity(String id, String name, String rule_id, String rule, String buycount,
                                String statetype, String imgurl, String imgurlbig, String price, String saleflag,
                                String expressfee) {
            this.id = id;
            this.name = name;
            this.rule_id = rule_id;
            this.rule = rule;
            this.buycount = buycount;
            this.statetype = statetype;
            this.imgurl = imgurl;
            this.imgurlbig = imgurlbig;
            this.price = price;
            this.saleflag = saleflag;
            this.expressfee = expressfee;
        }

        public ChildItemsEntity(JSONObject jsonObject) {
            try {
                id = jsonObject.getString("id");
                if (!jsonObject.isNull("keyid")) {
                    keyid = jsonObject.getString("keyid");
                }
                name = jsonObject.getString("name");
                rule_id = jsonObject.getString("rule_id");
                rule = jsonObject.getString("rule");
                buycount = jsonObject.getString("buycount");
                statetype = jsonObject.getString("statetype");
                imgurl = jsonObject.getString("imgurl");
                imgurlbig = jsonObject.getString("imgurlbig");
                price = jsonObject.getString("price");
                saleflag = jsonObject.getString("saleflag");
                expressfee = jsonObject.getString("expressfee");
                isSelected = false;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setIsSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getKeyid() {
            return keyid;
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

        public void setBuycount(String buycount) {
            this.buycount = buycount;
        }

        public void setStatetype(String statetype) {
            this.statetype = statetype;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public void setImgurlbig(String imgurlbig) {
            this.imgurlbig = imgurlbig;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public void setSaleflag(String saleflag) {
            this.saleflag = saleflag;
        }

        public String getId() {
            return id;
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

        public String getBuycount() {
            return buycount;
        }

        public String getStatetype() {
            return statetype;
        }

        public String getImgurl() {
            return imgurl;
        }

        public String getImgurlbig() {
            return imgurlbig;
        }

        public String getPrice() {
            return price;
        }

        public String getSaleflag() {
            return saleflag;
        }

        public String getExpressfee() {
            return expressfee;
        }
    }
}
