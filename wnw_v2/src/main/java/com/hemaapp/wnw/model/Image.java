package com.hemaapp.wnw.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by Hufanglin on 2016/3/2.
 */
public class Image extends XtomObject implements Parcelable {
    private String id;
    private String client_id;
    private String title = "";
    private String content = "";
    private String imgurl;
    private String imgurlbig;
    private String orderby;

    public Image(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                this.id = this.get(jsonObject, "id");
                this.client_id = this.get(jsonObject, "client_id");
                if (jsonObject.isNull("title")) {
                    this.title = this.get(jsonObject, "title");
                }
                if (jsonObject.isNull("content")) {
                    this.content = this.get(jsonObject, "content");
                }
                this.imgurl = this.get(jsonObject, "imgurl");
                this.imgurlbig = this.get(jsonObject, "imgurlbig");
                this.orderby = this.get(jsonObject, "orderby");
            } catch (JSONException var3) {
                throw new DataParseException(var3);
            }
        }

    }

    public Image(String id, String client_id, String title, String content, String imgurl, String imgurlbig, String orderby) {
        this.id = id;
        this.client_id = client_id;
        this.title = title;
        this.content = content;
        this.imgurl = imgurl;
        this.imgurlbig = imgurlbig;
        this.orderby = orderby;
    }

    public String toString() {
        return "Image [id=" + this.id + ", client_id=" + this.client_id + ", title=" + this.title + ", content=" + this.content + ", imgurl=" + this.imgurl + ", imgurlbig=" + this.imgurlbig + ", orderby=" + this.orderby + "]";
    }

    public String getId() {
        return this.id;
    }

    public String getClient_id() {
        return this.client_id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public String getImgurl() {
        return this.imgurl;
    }

    public String getImgurlbig() {
        return this.imgurlbig;
    }

    public String getOrderby() {
        return this.orderby;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.client_id);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.imgurl);
        dest.writeString(this.imgurlbig);
        dest.writeString(this.orderby);
    }

    private Image(Parcel in) {
        this.id = in.readString();
        this.client_id = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.imgurl = in.readString();
        this.imgurlbig = in.readString();
        this.orderby = in.readString();
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
