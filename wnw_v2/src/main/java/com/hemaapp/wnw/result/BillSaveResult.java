package com.hemaapp.wnw.result;

import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.exception.DataParseException;

/**
 * 订单保存的返回结果
 * Created by Hufanglin on 2016/3/15.
 */
public class BillSaveResult extends HemaBaseResult {

    private String bill_ids;//订单id	已用逗号分开
    private double total_fee;

    public BillSaveResult(JSONObject jsonObject) throws DataParseException {
        super(jsonObject);
        if (!jsonObject.isNull("infor")) {
            try {
                bill_ids = jsonObject.getJSONArray("infor").getJSONObject(0).getString("bill_ids");
                total_fee = jsonObject.getJSONArray("infor").getJSONObject(0).getDouble("total_fee");
            } catch (JSONException e) {
                e.printStackTrace();
                bill_ids = "";
                total_fee = 0;
            }
        }
    }

    public String getBill_ids() {
        return bill_ids;
    }

    public double getTotal_fee() {
        return total_fee;
    }
}
