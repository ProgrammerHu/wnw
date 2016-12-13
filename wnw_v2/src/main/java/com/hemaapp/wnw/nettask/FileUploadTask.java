package com.hemaapp.wnw.nettask;

import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyNetTask;
import com.hemaapp.wnw.model.FileUploadResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by Hufanglin on 2016/2/22.
 */
public class FileUploadTask extends MyNetTask {
    public FileUploadTask(MyHttpInformation information, HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

	private class Result extends HemaArrayResult<FileUploadResult> {

		public Result(JSONObject jsonObject) throws DataParseException {
			super(jsonObject);
		}

		@Override
		public FileUploadResult parse(JSONObject jsonObject)
				throws DataParseException {
			return new FileUploadResult(jsonObject);
		}

	}
}
