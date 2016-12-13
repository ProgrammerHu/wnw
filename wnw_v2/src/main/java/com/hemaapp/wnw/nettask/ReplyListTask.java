package com.hemaapp.wnw.nettask;

import java.util.HashMap;

import org.json.JSONObject;

import xtom.frame.exception.DataParseException;

import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyNetTask;
import com.hemaapp.wnw.model.ReplyCommentModel;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.result.ReplyListResult;

/**
 * 评论列表接口
 * 
 * @author CoderHu
 * @author HuFanglin
 * @DateTime 2016年3月11日
 */
public class ReplyListTask extends MyNetTask {

	public ReplyListTask(MyHttpInformation information,
			HashMap<String, String> params) {
		super(information, params);
	}

	@Override
	public Object parse(JSONObject jsonObject) throws DataParseException {
		return new ReplyListResult(jsonObject);
	}

}
