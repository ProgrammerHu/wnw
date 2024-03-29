package com.hemaapp.wnw.nettask;

import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyNetTask;
import com.hemaapp.wnw.model.TimeGetModel;
import com.hemaapp.wnw.result.MyArrayResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 档期列表接口
 * Created by HuHu on 2016-09-13.
 */
public class TimeListTask extends MyNetTask {
    public TimeListTask(MyHttpInformation information, HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends MyArrayResult<TimeGetModel> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public TimeGetModel parse(JSONObject jsonObject) throws DataParseException {
            return new TimeGetModel(jsonObject);
        }
    }
}
