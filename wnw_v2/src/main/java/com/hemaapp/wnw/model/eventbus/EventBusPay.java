package com.hemaapp.wnw.model.eventbus;


import java.io.Serializable;

import xtom.frame.XtomObject;

/**
 * 支付的广播模型
 * Created by Hufanglin on 2016/3/14.
 */
public class EventBusPay extends XtomObject implements Serializable {

    private static final long serialVersionUID = -4324167030867630720L;
    private boolean state;
    private String type;
    private String id = "";

    public EventBusPay(boolean state, String type, String id) {
        this.state = state;
        this.type = type;
        this.id = id;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
