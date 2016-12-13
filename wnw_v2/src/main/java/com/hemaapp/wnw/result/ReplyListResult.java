package com.hemaapp.wnw.result;

import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.model.ReplyCommentModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import xtom.frame.exception.DataParseException;

/**
 * 评论列表返回结果
 * Created by HuHu on 2016/3/24.
 */
public class ReplyListResult extends HemaBaseResult {
    private ArrayList<ReplyCommentModel> listData = new ArrayList<>();
    private double level;
    private String totalCount;

    public ReplyListResult(JSONObject jsonObject) throws DataParseException {
        super(jsonObject);
        if (!jsonObject.isNull("infor")) {
            try {
                JSONObject inforObject = jsonObject.getJSONObject("infor");
                if (!inforObject.isNull("totalCount")) {
                    totalCount = inforObject.getString("totalCount");
                }
                if (!inforObject.isNull("level")) {
                    level = inforObject.getDouble("level");
                }
                if (!inforObject.isNull("listItems")) {
                    JSONArray jsonArray = inforObject.getJSONArray("listItems");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        listData.add(new ReplyCommentModel(jsonArray.getJSONObject(i)));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<ReplyCommentModel> getListData() {
        return listData;
    }

    public double getLevel() {
        return level;
    }

    public String getTotalCount() {
        return totalCount;
    }
}
