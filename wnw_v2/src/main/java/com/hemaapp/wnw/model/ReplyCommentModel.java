package com.hemaapp.wnw.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 评论列表模型
 *
 * @author CoderHu
 * @author HuFanglin
 * @DateTime 2016年3月11日
 */
public class ReplyCommentModel extends XtomObject {

    private String nickname;
    private String avatar;
    private String id;
    private String client_id;
    private String type;
    private String replytype;
    private String keytype;
    private String keyid;
    private String content;
    private String parentid;
    private String regdate;
    private String curr_date;
    private String imgcount;
    private ArrayList<Image> imgItems = new ArrayList<>();
    private String reply;
    private String reply_flag;
    /**
     * 2016年9月19日18:13:23 新增
     */
    private String reply_date;
    private String reply_id;
    private String reply_name;


    public ReplyCommentModel(JSONObject jsonObject) {
        try {
            nickname = jsonObject.getString("nickname");
            avatar = jsonObject.getString("avatar");
            id = jsonObject.getString("id");
            client_id = jsonObject.getString("client_id");
            type = jsonObject.getString("type");
            replytype = jsonObject.getString("replytype");
            keytype = jsonObject.getString("keytype");
            keyid = jsonObject.getString("keyid");
            content = jsonObject.getString("content");
            parentid = jsonObject.getString("parentid");
            regdate = jsonObject.getString("regdate");
            reply_date = get(jsonObject, "reply_date");
            reply_id = get(jsonObject, "reply_id");
            reply_name = get(jsonObject, "reply_name");

            if (!jsonObject.isNull("reply")) {
                reply = jsonObject.getString("reply");
            }
            if (!jsonObject.isNull("reply_flag")) {
                reply_flag = jsonObject.getString("reply_flag");
            }
            if (!jsonObject.isNull("curr_date")) {
                curr_date = jsonObject.getString("curr_date");
            } else {
                SimpleDateFormat df = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");// 设置日期格式
                curr_date = df.format(new Date());
            }
            if (!jsonObject.isNull("imgcount")) {
                imgcount = jsonObject.getString("imgcount");
            }

            if (!jsonObject.isNull("imgItems") && !isNull(jsonObject.getString("imgItems"))) {
                JSONArray jsonArray = jsonObject.getJSONArray("imgItems");
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        imgItems.add(new Image(jsonArray.getJSONObject(i)));
                    } catch (DataParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getImgcount() {
        return imgcount;
    }

    public ArrayList<Image> getImgItems() {
        return imgItems;
    }

    public String getNickname() {
        if (isNull(nickname)) {
            return "";
        }
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getId() {
        return id;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getType() {
        return type;
    }

    public String getReplytype() {
        return replytype;
    }

    public String getKeytype() {
        return keytype;
    }

    public String getKeyid() {
        return keyid;
    }

    public String getContent() {
        return content;
    }

    public String getParentid() {
        return parentid;
    }

    public String getRegdate() {
        return regdate;
    }

    public String getTimeDiff() {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        Date dateRegdate = null;
        Date serverDate = null;
        try {
            dateRegdate = sdf1.parse(regdate);
            serverDate = sdf1.parse(curr_date);
            long secondDiff = (serverDate.getTime() - dateRegdate.getTime()) / 1000;
            long minutesDiff = secondDiff / 60;
            long hoursDiff = minutesDiff / 60;
            long daysDiff = hoursDiff / 24;
            if (secondDiff < 60) {
                return "1分钟前";
            } else if (minutesDiff < 60) {
                return minutesDiff + "分钟前";
            } else if (hoursDiff < 24) {
                return hoursDiff + "小时前";
            } else if (daysDiff <= 2) {
                return daysDiff + "天前";
            } else {
                return sdf2.format(dateRegdate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return regdate;
    }

    public String getReply_flag() {
        return reply_flag;
    }

    public String getReply() {
        return reply;
    }

    public void setReply_date(String reply_date) {
        this.reply_date = reply_date;
    }

    public void setReply_id(String reply_id) {
        this.reply_id = reply_id;
    }

    public void setReply_name(String reply_name) {
        this.reply_name = reply_name;
    }

    public String getReply_date() {
        return reply_date;
    }

    public String getReply_id() {
        return reply_id;
    }

    public String getReply_name() {
        if (isNull(reply_name)) {
            return "";
        }
        return reply_name;
    }
}
