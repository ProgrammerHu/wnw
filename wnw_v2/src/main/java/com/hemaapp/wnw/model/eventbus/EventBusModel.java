package com.hemaapp.wnw.model.eventbus;

import java.io.Serializable;

import xtom.frame.XtomObject;

/**
 * EventBus传递数据使用
 * Created by Hufanglin on 2016/2/5.
 */

public class EventBusModel extends XtomObject implements Serializable {
    private static final long serialVersionUID = 2457040098627212163L;
    private boolean state;
    private String type;
    private String content = "";

    public EventBusModel(boolean state, String type) {
        this.state = state;
        this.type = type;
    }

    public EventBusModel(boolean state, String type, String content) {
        this(state, type);
        this.content = content;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}