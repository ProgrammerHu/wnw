package com.hemaapp.wnw.nettask;

import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyNetTask;
import com.hemaapp.wnw.model.SysInitInfo;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 系统初始化
 */
public class InitTask extends MyNetTask {

    public InitTask(MyHttpInformation information,
                    HashMap<String, String> params) {
        super(information, params);
    }

    public InitTask(MyHttpInformation information,
                    HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<SysInitInfo> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public SysInitInfo parse(JSONObject jsonObject)
                throws DataParseException {
            return new SysInitInfo(jsonObject);
        }

    }
}

