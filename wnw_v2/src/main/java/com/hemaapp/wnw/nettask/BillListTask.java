package com.hemaapp.wnw.nettask;

import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyNetTask;
import com.hemaapp.wnw.model.BillListModel;
import com.hemaapp.wnw.result.MyArrayResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 订单列表接口
 * Created by Hufanglin on 2016/3/14.
 */
public class BillListTask extends MyNetTask {
    public BillListTask(MyHttpInformation information, HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends MyArrayResult<BillListModel> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public BillListModel parse(JSONObject jsonObject) throws DataParseException {
            return new BillListModel(jsonObject);
        }
    }
}
