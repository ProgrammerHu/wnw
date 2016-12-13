package com.hemaapp.wnw.nettask;

import java.util.HashMap;

import org.json.JSONObject;

import xtom.frame.exception.DataParseException;

import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyNetTask;
import com.hemaapp.wnw.model.SystemNotice;
import com.hemaapp.wnw.result.MyArrayResult;

/**
 * 获取消息通知列表接口
 * 
 * @author CoderHu
 * @author HuFanglin
 * @DateTime 2016年3月8日
 */
public class NoticeListTask extends MyNetTask {

	public NoticeListTask(MyHttpInformation information,
			HashMap<String, String> params) {
		super(information, params);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object parse(JSONObject jsonObject) throws DataParseException {
		// TODO Auto-generated method stub
		return new Result(jsonObject);
	}

	private class Result extends MyArrayResult<SystemNotice> {

		public Result(JSONObject jsonObject) throws DataParseException {
			super(jsonObject);
		}

		@Override
		public SystemNotice parse(JSONObject jsonObject)
				throws DataParseException {
			return new SystemNotice(jsonObject);
		}

	}

}
