package com.hemaapp.wnw.result;

import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.exception.DataParseException;

/**
 * 订单红点是否显示结果集
 * Created by HuHu on 2016/3/29.
 */
public class BillRedResult extends HemaBaseResult {
    private boolean[] result = new boolean[4];//保存结果集，分别保存0，1，2，3位是否显示红点,true显示，false不显示

    public BillRedResult(JSONObject jsonObject) throws DataParseException {
        super(jsonObject);
        if (!jsonObject.isNull("infor")) {
            try {
                JSONObject infor = jsonObject.getJSONArray("infor").getJSONObject(0);
                result[0] = !"0".equals(infor.getString("one"));
                result[1] = !"0".equals(infor.getString("two"));
                result[2] = !"0".equals(infor.getString("three"));
                result[3] = !"0".equals(infor.getString("four"));
            } catch (JSONException e) {
                e.printStackTrace();
                for (int i = 0; i < result.length; i++) {
                    result[i] = false;
                }
            }
        }
    }

    public boolean[] getResult() {
        return result;
    }
}
