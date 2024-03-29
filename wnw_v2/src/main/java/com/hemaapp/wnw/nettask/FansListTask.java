package com.hemaapp.wnw.nettask;

import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyNetTask;
import com.hemaapp.wnw.model.FansListModel;
import com.hemaapp.wnw.result.MyArrayResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 粉丝列表接口
 * Created by HuHu on 2016-09-23.
 */
public class FansListTask extends MyNetTask {
    public FansListTask(MyHttpInformation information, HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends MyArrayResult<FansListModel> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public FansListModel parse(JSONObject jsonObject) throws DataParseException {
            return new FansListModel(jsonObject);
        }
    }
}
