package com.hemaapp.wnw.nettask;

import java.util.HashMap;

import org.json.JSONObject;

import xtom.frame.exception.DataParseException;

import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyNetTask;
import com.hemaapp.wnw.model.GoodsBigListModel;
import com.hemaapp.wnw.result.MyArrayResult;

/**
 * 商品列表接口
 * 
 * @author CoderHu
 * @author HuFanglin
 * @DateTime 2016年3月9日
 */
public class GoodsBigListTask extends MyNetTask {

	public GoodsBigListTask(MyHttpInformation information,
			HashMap<String, String> params) {
		super(information, params);
	}

	@Override
	public Object parse(JSONObject jsonObject) throws DataParseException {
		return new Result(jsonObject);
	}

	private class Result extends MyArrayResult<GoodsBigListModel> {

		public Result(JSONObject jsonObject) throws DataParseException {
			super(jsonObject);
		}

		@Override
		public GoodsBigListModel parse(JSONObject jsonObject)
				throws DataParseException {
			return new GoodsBigListModel(jsonObject);
		}

	}
}
