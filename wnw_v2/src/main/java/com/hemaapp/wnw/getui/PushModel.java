package com.hemaapp.wnw.getui;

import java.io.Serializable;

import xtom.frame.XtomObject;

/**
 * 消息推送
 * Created by HuHu on 2016/4/12.
 */
public class PushModel extends XtomObject implements Serializable {

    private static final long serialVersionUID = -2059126470520707606L;
    private String keyType;
    private String keyId;
    private String msg;

    public PushModel(String keyType, String keyId, String msg) {
        this.keyType = keyType;
        this.keyId = keyId;
        this.msg = msg;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
