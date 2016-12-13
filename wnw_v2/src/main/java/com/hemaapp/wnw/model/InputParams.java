package com.hemaapp.wnw.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 输入参数模型
 * Created by Hufanglin on 2016/2/22.
 */
public class InputParams implements Parcelable {

    private String title;
    private String hint;
    private String content;
    private int max_lenght;
    private int input_type;

    /**
     * 输入参数模型
     * @param title 页面标题
     * @param hint 输入框提示语
     * @param content 输入框默认值
     * @param max_lenght 输入框最大长度
     * @param input_type 输入格式
     */
    public InputParams(String title, String hint, String content, int max_lenght, int input_type) {
        this.title = title;
        this.hint = hint;
        this.content = content;
        this.max_lenght = max_lenght;
        this.input_type = input_type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.hint);
        dest.writeString(this.content);
        dest.writeInt(this.max_lenght);
        dest.writeInt(this.input_type);
    }

    private InputParams(Parcel in) {
        this.title = in.readString();
        this.hint = in.readString();
        this.content = in.readString();
        this.max_lenght = in.readInt();
        this.input_type = in.readInt();
    }

    public static final Parcelable.Creator<InputParams> CREATOR = new Parcelable.Creator<InputParams>() {
        public InputParams createFromParcel(Parcel source) {
            return new InputParams(source);
        }

        public InputParams[] newArray(int size) {
            return new InputParams[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getHint() {
        return hint;
    }

    public String getContent() {
        return content;
    }

    public int getMax_lenght() {
        return max_lenght;
    }

    public int getInput_type() {
        return input_type;
    }
}
