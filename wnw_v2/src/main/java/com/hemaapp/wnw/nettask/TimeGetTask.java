package com.hemaapp.wnw.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyNetTask;
import com.hemaapp.wnw.model.TimeGetModel;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 获取档期
 * Created by HuHu on 2016-09-12.
 */
public class TimeGetTask extends MyNetTask {
    public TimeGetTask(MyHttpInformation information, HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);

    }

    private class Result extends HemaArrayResult<TimeGetModel> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public TimeGetModel parse(JSONObject jsonObject) throws DataParseException {
            return new TimeGetModel(jsonObject);
        }
    }
}
