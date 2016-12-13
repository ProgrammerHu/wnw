package com.hemaapp.wnw.result;

import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.model.VipListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import xtom.frame.exception.DataParseException;

/**
 * 会员列表
 * Created by HuHu on 2016-09-18.
 */
public class VipListResult extends HemaBaseResult {
    private int totalCount;
    private String totalFee;
    private ArrayList<VipListModel> objects = new ArrayList<>();

    public VipListResult(JSONObject jsonObject) throws DataParseException {
        super(jsonObject);

        try {
            if (!jsonObject.isNull("infor")
                    && !isNull(jsonObject.getString("infor"))) {
                JSONObject jsonInfor = jsonObject.getJSONObject("infor");
                if (!jsonInfor.isNull("totalCount") && !isNull(jsonInfor.getString("totalCount"))) {
                    totalCount = jsonInfor.getInt("totalCount");
                }
                if (!jsonInfor.isNull("totalFee") && !isNull(jsonInfor.getString("totalFee"))) {
                    totalFee = jsonInfor.getString("totalFee");
                }
                if (!jsonInfor.isNull("listItems") && !isNull(jsonInfor.getString("listItems"))) {
                    JSONArray jsonList = jsonInfor.getJSONArray("listItems");
                    int size = jsonList.length();
                    for (int i = 0; i < size; i++) {
                        objects.add(new VipListModel(jsonList.getJSONObject(i)));
                    }
                }

            }
        } catch (JSONException e) {
            throw new DataParseException(e);
        }
    }

    public ArrayList<VipListModel> getObjects() {
        return objects;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public String getTotalFee() {
        return totalFee;
    }
}
