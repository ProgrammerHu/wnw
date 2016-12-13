package com.hemaapp.wnw.result;


import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.exception.DataParseException;

import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

/**
 * Created by Hufanglin on 2016/2/20.
 */
public class TempTokenResult extends HemaBaseResult {

    private String temp_token;

    public TempTokenResult(JSONObject jsonObject) throws DataParseException {
        super(jsonObject);
        try {
            if (!jsonObject.isNull("infor") && !jsonObject.getJSONArray("infor").getJSONObject(0).isNull("temp_token")) {
                temp_token = jsonObject.getJSONArray("infor").getJSONObject(0).getString("temp_token");
            }
        } catch (JSONException e) {
            log_e(e.getMessage());
        }
    }

    public String getTempToken() {
        return temp_token;
    }
}
