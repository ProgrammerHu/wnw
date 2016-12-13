package com.hemaapp.wnw.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyNetTask;
import com.hemaapp.wnw.model.City;
import com.hemaapp.wnw.result.DistrictAllGetResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * 获取所有城市接口
 * Created by Hufanglin on 2016/3/1.
 */
public class DistrictAllGetTask extends MyNetTask {
    public DistrictAllGetTask(MyHttpInformation information, HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new DistrictAllGetResult(jsonObject);
    }
}
