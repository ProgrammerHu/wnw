package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;

/**
 * 留言回复
 *
 * @author CoderHu
 * @author HuFanglin
 * @DateTime 2016年3月9日
 */
public class ReplyModel extends XtomObject {
    private String merchant_id;
    private String id;
    private String client_id;
    private String title;
    private String content;
    private String flag;
    private String regdate;
    private String avatar;
    private String nickname;
    private String looktype = "1";
    private String from;

    private boolean isSelected = false;// ture已选择，false未选择

    public ReplyModel(JSONObject jsonObject) {
        try {
            merchant_id = get(jsonObject, "merchant_id");
            id = get(jsonObject, "id");
            client_id = get(jsonObject, "client_id");
            title = get(jsonObject, "title");
            content = get(jsonObject, "content");
            flag = get(jsonObject, "flag");
            regdate = get(jsonObject, "regdate");
            avatar = get(jsonObject, "avatar");
            nickname = get(jsonObject, "nickname");
            if (!jsonObject.isNull("looktype")) {
                looktype = get(jsonObject, "looktype");
            }
            from = get(jsonObject, "from");
            isSelected = false;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getLooktype() {
        return looktype;
    }

    public void setLooktype(String looktype) {
        this.looktype = looktype;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public String getId() {
        return id;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getFlag() {
        return flag;
    }

    public String getRegdate() {
        return regdate;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
