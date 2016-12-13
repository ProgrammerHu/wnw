package com.hemaapp.luna_demo.calendar;

public class WorkDay{

	private String yera;
	private String month;
	private String day;
	private String week;
	private boolean work;
	private boolean enable;
	private boolean checked;
	
	public String getYera() {
		return yera;
	}
	public void setYera(String yera) {
		this.yera = yera;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public boolean isWork() {
		return work;
	}
	public void setWork(boolean work) {
		this.work = work;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getString() {
		return month+"/"+day+"/"+yera;
	}
}
