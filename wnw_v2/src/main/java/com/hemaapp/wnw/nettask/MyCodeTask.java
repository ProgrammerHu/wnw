package com.hemaapp.wnw.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyNetTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 我的二维码
 * Created by HuHu on 2016-09-05.
 */
public class MyCodeTask extends MyNetTask {
    public MyCodeTask(MyHttpInformation information, HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<String> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public String parse(JSONObject jsonObject) throws DataParseException {
            try {
                return get(jsonObject, "qrcode");
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        }
    }
}
