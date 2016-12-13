package com.hemaapp.wnw.nettask;

import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyNetTask;
import com.hemaapp.wnw.result.TempTokenResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 获取临时Token的线程
 * Created by Hufanglin on 2016/2/20.
 */
public class CodeVerifyTask extends MyNetTask {

    public CodeVerifyTask(MyHttpInformation information,
                          HashMap<String, String> params) {
        super(information, params);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        // TODO Auto-generated method stub
        return new TempTokenResult(jsonObject);
    }

}