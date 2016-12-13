package com.hemaapp.wnw.result;

import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.exception.DataParseException;

/**
 * 用户注册接口
 * Created by Hufanglin on 2016/3/1.
 */
public class ClientAddResult extends HemaBaseResult {

    private String token;
    private String infor;

    public ClientAddResult(JSONObject jsonObject) throws DataParseException {
        super(jsonObject);
        if (!jsonObject.isNull("infor")) {
            try {
                JSONObject inforObject = jsonObject.getJSONArray("infor").getJSONObject(0);
                token = inforObject.getString("token");
                infor = inforObject.getString("infor");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getToken() {
        return token;
    }

    public String getInfor() {
        return infor;
    }
}
