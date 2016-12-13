package com.hemaapp.wnw.nettask;

import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyNetTask;
import com.hemaapp.wnw.model.GeneralListModel;
import com.hemaapp.wnw.result.MyArrayResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 退款列表接口
 * Created by Hufanglin on 2016/3/18.
 */
public class GeneralListTask extends MyNetTask {
    public GeneralListTask(MyHttpInformation information, HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends MyArrayResult<GeneralListModel> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public GeneralListModel parse(JSONObject jsonObject) throws DataParseException {
            return new GeneralListModel(jsonObject);
        }
    }
}
