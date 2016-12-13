package com.hemaapp.wnw.result;

import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import xtom.frame.exception.DataParseException;

/**
 * 适合自己的结果集组织形式
 * Created by Hufanglin on 2016/2/16.
 */
public abstract class MyArrayResult<T> extends HemaBaseResult {
    private int totalCount;
    private ArrayList<T> objects = new ArrayList<T>();

    public MyArrayResult(JSONObject jsonObject) throws DataParseException {
        super(jsonObject);

        try {
            if (!jsonObject.isNull("infor")
                    && !isNull(jsonObject.getString("infor"))) {
                JSONObject jsonInfor = jsonObject.getJSONObject("infor");
                if (!jsonInfor.isNull("totalCount")
                        && !isNull(jsonInfor.getString("totalCount"))) {
                    totalCount = jsonInfor.getInt("totalCount");
                }
                if (!jsonInfor.isNull("listItems")
                        && !isNull(jsonInfor.getString("listItems"))) {
                    JSONArray jsonList = jsonInfor.getJSONArray("listItems");
                    int size = jsonList.length();
                    for (int i = 0; i < size; i++) {
                        objects.add(parse(jsonList.getJSONObject(i)));
                    }
                }

            }
        } catch (JSONException e) {
            throw new DataParseException(e);
        }
    }

    /**
     * 获取服务器返回的实例集合
     *
     * @return 服务器返回的实例集合
     */
    public ArrayList<T> getObjects() {
        return objects;
    }

    /**
     * 返回结果集数量
     *
     * @return
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * 该方法将JSONObject解析为具体的数据实例
     */
    public abstract T parse(JSONObject jsonObject) throws DataParseException;
}
