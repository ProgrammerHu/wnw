package com.hemaapp.wnw.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;

/**
 * 推荐商品数据模型
 * Created by Hufanglin on 2016/3/2.
 */
public class RecommendItem extends XtomObject implements Parcelable {
    private String id;
    private String name;
    private String content;
    private String price;
    private String oldprice;
    private String paycount;
    private String imgurl;

    public RecommendItem(JSONObject jsonObject) {
        try {
            id = jsonObject.getString("id");
            name = jsonObject.getString("name");
            content = jsonObject.getString("content");
            price = jsonObject.getString("price");
            oldprice = jsonObject.getString("oldprice");
            paycount = jsonObject.getString("paycount");
            imgurl = jsonObject.getString("imgurl");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getPrice() {
        return price;
    }

    public String getOldprice() {
        return oldprice;
    }

    public String getPaycount() {
        return paycount;
    }

    public String getImgurl() {
        return imgurl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.content);
        dest.writeString(this.price);
        dest.writeString(this.oldprice);
        dest.writeString(this.paycount);
        dest.writeString(this.imgurl);
    }

    private RecommendItem(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.content = in.readString();
        this.price = in.readString();
        this.oldprice = in.readString();
        this.paycount = in.readString();
        this.imgurl = in.readString();
    }

    public static final Parcelable.Creator<RecommendItem> CREATOR = new Parcelable.Creator<RecommendItem>() {
        public RecommendItem createFromParcel(Parcel source) {
            return new RecommendItem(source);
        }

        public RecommendItem[] newArray(int size) {
            return new RecommendItem[size];
        }
    };
}
