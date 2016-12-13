package com.hemaapp.wnw.model;

import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;

/**
 * 消息通知
 * 
 * @author CoderHu
 * @author HuFanglin
 * @DateTime 2016年3月8日
 */
public class SystemNotice extends XtomObject {
	private String id;
	private String keyid;
	private String keytype;
	private String content;
	private String regdate;
	private String looktype;
	private String from_id;

	private boolean isSelected = false;//ture已选择，false未选择

	public SystemNotice(JSONObject jsonObject) {
		try {
			id = jsonObject.getString("id");
			keytype = jsonObject.getString("keytype");
			keyid = jsonObject.getString("keyid");
			content = jsonObject.getString("content");
			regdate = jsonObject.getString("regdate");
			looktype = jsonObject.getString("looktype");
			from_id = jsonObject.getString("from_id");
			isSelected = false;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getLooktype() {
		return looktype;
	}

	public void setLooktype(String looktype) {
		this.looktype = looktype;
	}

	public String getId() {
		return id;
	}

	public String getKeytype() {
		return keytype;
	}

	public String getContent() {
		return content;
	}

	public String getRegdate() {
		return regdate;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getKeyid() {
		return keyid;
	}

	public String getFrom_id() {
		return from_id;
	}
}
