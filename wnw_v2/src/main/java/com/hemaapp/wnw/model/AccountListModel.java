package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import xtom.frame.XtomObject;

/**
 * 交易明细表
 * Created by HuHu on 2016/3/30.
 */
public class AccountListModel extends XtomObject {

    private String id;
    private String client_id;
    private String keytype;
    private String amount;
    private String balance;
    private String content;
    private String regdate;

    public AccountListModel(JSONObject jsonObject) {
        try {
            id = jsonObject.getString("id");
            client_id = jsonObject.getString("client_id");
            keytype = jsonObject.getString("keytype");
            amount = jsonObject.getString("amount");
            balance = jsonObject.getString("balance");
            content = jsonObject.getString("content");
            regdate = jsonObject.getString("regdate");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getId() {
        return id;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getKeytype() {
        return keytype;
    }

    public String getAmount() {
        return amount;
    }

    public double getDoubleAmount() {
        try {
            return Double.parseDouble(amount);
        } catch (Exception e) {
            return 0;
        }
    }

    public String getBalance() {
        return balance;
    }

    public String getContent() {
        return content;
    }

    public String getRegdate() {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        try {
            Date date = sdf1.parse(regdate);
            return sdf2.format(date) + " " + dateFm.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return regdate;
    }

}
