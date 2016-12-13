package com.hemaapp.wnw.nettask;

import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 通用请求线程（不需要返回结果的，只需要获取是否成功）
 * @author Wen
 * @author HuFanglin
 *
 */
public class CurrentTask extends MyNetTask{

    public CurrentTask(MyHttpInformation information,
                       HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new HemaBaseResult(jsonObject);
    }

}
