package com.hemaapp.wnw.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyNetTask;
import com.hemaapp.wnw.model.MerchantGoodsGetModel;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 商家获取商品详情信息接口
 * Created by HuHu on 2016-09-08.
 */
public class MerchantBlogGetTask extends MyNetTask {
    public MerchantBlogGetTask(MyHttpInformation information, HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<MerchantGoodsGetModel> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public MerchantGoodsGetModel parse(JSONObject jsonObject) throws DataParseException {
            return new MerchantGoodsGetModel(jsonObject);
        }
    }
}
