package com.hemaapp.wnw.nettask;

import java.util.HashMap;

import org.json.JSONObject;

import xtom.frame.exception.DataParseException;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyNetTask;
import com.hemaapp.wnw.model.UnionTrade;

/**
 * 获取银联交易签名串
 * @author CoderHu
 * @author HuFanglin
 * @DateTime 2016年3月7日
 */
public class UnionTradeTask extends MyNetTask {

	public UnionTradeTask(MyHttpInformation information,
			HashMap<String, String> params) {
		super(information, params);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object parse(JSONObject jsonObject) throws DataParseException {
		// TODO Auto-generated method stub
		return new Result(jsonObject);
	}

	private class Result extends HemaArrayResult<UnionTrade> {

		public Result(JSONObject jsonObject) throws DataParseException {
			super(jsonObject);
			// TODO Auto-generated constructor stub
		}

		@Override
		public UnionTrade parse(JSONObject jsonObject)
				throws DataParseException {
			// TODO Auto-generated method stub
			return new UnionTrade(jsonObject);
		}

	}

}
