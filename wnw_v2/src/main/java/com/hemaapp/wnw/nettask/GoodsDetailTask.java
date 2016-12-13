package com.hemaapp.wnw.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyNetTask;
import com.hemaapp.wnw.model.GoodsDetailModel;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 获取商品详情信息接口
 * Created by Hufanglin on 2016/3/6.
 */
public class GoodsDetailTask extends MyNetTask {
    public GoodsDetailTask(MyHttpInformation information, HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<GoodsDetailModel> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public GoodsDetailModel parse(JSONObject jsonObject) throws DataParseException {
            return new GoodsDetailModel(jsonObject);
        }
    }
}
