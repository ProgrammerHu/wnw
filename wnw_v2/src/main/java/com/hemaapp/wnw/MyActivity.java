package com.hemaapp.wnw;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaActivity;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.activity.LoginActivity;
import com.hemaapp.wnw.dialog.MyOneButtonDialog;
import com.hemaapp.wnw.model.City;
import com.hemaapp.wnw.model.User;
import com.hemaapp.wnw.result.DistrictAllGetResult;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomActivityManager;
import xtom.frame.exception.DataParseException;
import xtom.frame.net.XtomNetWorker;
import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.util.XtomToastUtil;

/**
 * 重写activity
 *
 * @author Wen
 * @author HuFanglin
 */
public abstract class MyActivity extends MyFragmentActivity {


}
