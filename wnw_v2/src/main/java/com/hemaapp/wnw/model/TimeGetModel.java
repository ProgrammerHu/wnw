package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import xtom.frame.XtomObject;

/**
 * 获取档期
 * Created by HuHu on 2016-09-12.
 */
public class TimeGetModel extends XtomObject {

    private String id;
    private String time;
    private String time_start;
    private String time_end;
    private String curr_date;

    public TimeGetModel(JSONObject jsonObject) {
        try {
            id = get(jsonObject, "id");
            time = get(jsonObject, "time");
            time_start = get(jsonObject, "time_start");
            time_end = get(jsonObject, "time_end");
            curr_date = get(jsonObject, "curr_date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public void setCurr_date(String curr_date) {
        this.curr_date = curr_date;
    }

    public String getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public Date getTime_start() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(time_start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public Date getTime_end() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(time_end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public Date getCurr_date() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(curr_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * 格式化剩余时间
     *
     * @param format
     * @param secondDiff 秒为单位的偏移量
     * @return
     */
    public String getBalanceTime(String format, int secondDiff) {
        long diff = getTime_end().getTime() - getCurr_date().getTime() - 8 * 3600 * 1000 - secondDiff * 1000;
        Date date = new Date(diff);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 获取剩余时间long
     * @param secondDiff
     * @return
     */
    public long getBalanceTime(int secondDiff) {
        long diff = getTime_end().getTime() - getCurr_date().getTime() - secondDiff * 1000;
        return diff;
    }

    /**
     * 格式化开始时间
     *
     * @param format
     * @return
     */
    public String getStartTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(getTime_start());
    }
}
