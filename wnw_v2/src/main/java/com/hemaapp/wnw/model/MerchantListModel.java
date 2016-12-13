package com.hemaapp.wnw.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import xtom.frame.XtomObject;

/**
 * 后台商铺列表
 * Created by HuHu on 2016/3/28.
 */
public class MerchantListModel extends XtomObject {

    private String avatar;
    private String nickname;
    private String id;
    private String content;
    private String imgItems;
    private ArrayList<GoodsItemsEntity> goodsItems = new ArrayList<>();

    public MerchantListModel(JSONObject jsonObject) {
        try {
            avatar = jsonObject.getString("avatar");
            nickname = jsonObject.getString("nickname");
            id = jsonObject.getString("id");
            content = jsonObject.getString("content");
            imgItems = jsonObject.getString("imgItems");
            if (!jsonObject.isNull("goodsItems")) {
                JSONArray jsonArray = jsonObject.getJSONArray("goodsItems");
                for (int i = 0; i < jsonArray.length() && i < 3; i++) {//限制最多三张图片，我管你给我传几张
                    goodsItems.add(new GoodsItemsEntity(jsonArray.getJSONObject(i)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImgItems(String imgItems) {
        this.imgItems = imgItems;
    }


    public String getAvatar() {
        return avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getImgItems() {
        return imgItems;
    }

    public ArrayList<GoodsItemsEntity> getGoodsItems() {
        return goodsItems;
    }

    public static class GoodsItemsEntity {
        private String id;
        private String imgurl;

        public GoodsItemsEntity(JSONObject jsonObject) {
            try {
                id = jsonObject.getString("id");
                imgurl = jsonObject.getString("imgurl");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getId() {
            return id;
        }

        public String getImgurl() {
            return imgurl;
        }
    }
}
