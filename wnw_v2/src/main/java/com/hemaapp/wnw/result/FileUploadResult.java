package com.hemaapp.wnw.result;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by Hufanglin on 2016/2/22.
 */
public class FileUploadResult extends XtomObject implements Parcelable {

    private String item1;// 请查看标准化api文档
    private String item2;// 请查看标准化api文档

    public FileUploadResult(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                item1 = get(jsonObject, "item1");
                item2 = get(jsonObject, "item2");

                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "FileUploadResult [item1=" + item1 + ", item2=" + item2 + "]";
    }

    /**
     * @return the item1
     */
    public String getItem1() {
        return item1;
    }

    /**
     * @return the item2
     */
    public String getItem2() {
        return item2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.item1);
        dest.writeString(this.item2);
    }

    private FileUploadResult(Parcel in) {
        this.item1 = in.readString();
        this.item2 = in.readString();
    }

    public static final Parcelable.Creator<FileUploadResult> CREATOR = new Parcelable.Creator<FileUploadResult>() {
        public FileUploadResult createFromParcel(Parcel source) {
            return new FileUploadResult(source);
        }

        public FileUploadResult[] newArray(int size) {
            return new FileUploadResult[size];
        }
    };
}
