package com.hemaapp.wnw.nettask;

import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyNetTask;
import com.hemaapp.wnw.result.MerchantAccountListResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 入账记录接口
 * Created by HuHu on 2016-09-19.
 */
public class MerchantAccountListTask extends MyNetTask {
    public MerchantAccountListTask(MyHttpInformation information, HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new MerchantAccountListResult(jsonObject);
    }

}
