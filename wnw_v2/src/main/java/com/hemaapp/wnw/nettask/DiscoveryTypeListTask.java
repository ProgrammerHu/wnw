package com.hemaapp.wnw.nettask;

import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyNetTask;
import com.hemaapp.wnw.model.DiscoveryTypeList;
import com.hemaapp.wnw.result.MyArrayResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 商品分类列表接口
 * Created by HuHu on 2016/3/29.
 */
public class DiscoveryTypeListTask extends MyNetTask {
    public DiscoveryTypeListTask(MyHttpInformation information, HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends MyArrayResult<DiscoveryTypeList> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public DiscoveryTypeList parse(JSONObject jsonObject) throws DataParseException {
            return new DiscoveryTypeList(jsonObject);
        }
    }
}
