package com.hemaapp.wnw.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyNetTask;
import com.hemaapp.wnw.model.MerchantBillRed;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 商家订单红点是否显示接口
 * Created by HuHu on 2016-09-13.
 */
public class MerchantBillRedTask extends MyNetTask {
    public MerchantBillRedTask(MyHttpInformation information, HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<MerchantBillRed> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public MerchantBillRed parse(JSONObject jsonObject) throws DataParseException {
            return new MerchantBillRed(jsonObject);
        }
    }
}
