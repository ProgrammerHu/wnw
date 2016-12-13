package com.hemaapp.wnw.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;

/**
 * Created by Hufanglin on 2016/2/20.
 */
public class Tag extends XtomObject implements Parcelable {

    private String id;
    private String name;
    private boolean isSelected;

    public Tag(JSONObject jsonObject) {
        isSelected = false;
        try {
            this.id = jsonObject.getString("id");
            this.name = jsonObject.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Tag(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeByte(isSelected ? (byte) 1 : (byte) 0);
    }

    private Tag(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<Tag> CREATOR = new Creator<Tag>() {
        public Tag createFromParcel(Parcel source) {
            return new Tag(source);
        }

        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };
}