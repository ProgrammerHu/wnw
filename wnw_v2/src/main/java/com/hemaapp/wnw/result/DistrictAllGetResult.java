package com.hemaapp.wnw.result;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.wnw.model.City;

import org.json.JSONObject;

import xtom.frame.exception.DataParseException;

/**
 * 获取所有城市
 * Created by Hufanglin on 2016/3/6.
 */
public class DistrictAllGetResult extends HemaArrayResult<City> {

    private JSONObject myJsonObject;

    public DistrictAllGetResult(JSONObject jsonObject) throws DataParseException {
        super(jsonObject);
        this.myJsonObject = jsonObject;
    }

    @Override
    public City parse(JSONObject jsonObject) throws DataParseException {
        return new City(jsonObject);
    }

    public String getJsonObject() {
        return myJsonObject.toString();
    }
}
