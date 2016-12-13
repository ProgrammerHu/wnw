package com.hemaapp.wnw.nettask;

import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyNetTask;
import com.hemaapp.wnw.model.CountListModel;
import com.hemaapp.wnw.model.CountryListModel;
import com.hemaapp.wnw.result.MyArrayResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 商家分类数量接口
 * Created by HuHu on 2016-09-13.
 */
public class CountListTask extends MyNetTask {
    public CountListTask(MyHttpInformation information, HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends MyArrayResult<CountListModel>{

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public CountListModel parse(JSONObject jsonObject) throws DataParseException {
            return new CountListModel(jsonObject);
        }
    }
}
