package com.hemaapp.wnw.nettask;

import java.util.HashMap;

import org.json.JSONObject;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyNetTask;
import com.hemaapp.wnw.model.AlipayTrade;

import xtom.frame.exception.DataParseException;


/**
 * 获取支付宝交易签名
 */
public class AlipayTradeTask extends MyNetTask {

	public AlipayTradeTask(MyHttpInformation information,
			HashMap<String, String> params) {
		super(information, params);
	}

	public AlipayTradeTask(MyHttpInformation information,
			HashMap<String, String> params, HashMap<String, String> files) {
		super(information, params, files);
	}

	@Override
	public Object parse(JSONObject jsonObject) throws DataParseException {
		return new Result(jsonObject);
	}

	private class Result extends HemaArrayResult<AlipayTrade> {

		public Result(JSONObject jsonObject) throws DataParseException {
			super(jsonObject);
		}

		@Override
		public AlipayTrade parse(JSONObject jsonObject)
				throws DataParseException {
			return new AlipayTrade(jsonObject);
		}

	}
}

