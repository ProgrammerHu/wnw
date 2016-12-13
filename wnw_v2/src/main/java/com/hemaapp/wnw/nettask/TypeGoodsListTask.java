package com.hemaapp.wnw.nettask;

import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyNetTask;
import com.hemaapp.wnw.model.TypeGoodsList;
import com.hemaapp.wnw.result.MyArrayResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 分类商品列表接口
 * Created by HuHu on 2016/3/21.
 */
public class TypeGoodsListTask extends MyNetTask {
    public TypeGoodsListTask(MyHttpInformation information, HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends MyArrayResult<TypeGoodsList>{

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public TypeGoodsList parse(JSONObject jsonObject) throws DataParseException {
            return new TypeGoodsList(jsonObject);
        }
    }
}
