package com.hemaapp.wnw.db;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
/**
 * 搜索内容缓存
 * @author Wen
 * @author HuFanglin
 *
 */
public class SearchDBHelper extends DBHelper {
	private String tableName = SEARCH_LOG;
	private String columns = "id, type, content, searchdate";//type:0,搜索商品;

	public SearchDBHelper(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 获取搜索商品的缓存
	 * @param type 0,搜索商品;1,搜索投票;2:搜索校园广播
	 * @return
	 */
	public ArrayList<String> getSearch(String type)
	{
		ArrayList<String> listData = new ArrayList<>();
		String SQL = "select id, content from " + tableName +" where type = '" + type + "'  order by searchdate desc";
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery(SQL, null);
		if (cursor.getCount() > 0) {
			while(cursor.moveToNext())
			{
				listData.add(cursor.getString(1));
			}
		}
		return listData;
	}
	/**
	 * 写入搜索记录
	 * @param content
	 * @param type 0,搜索商品;
	 * @return true:成功
	 */
	public boolean insertSearch(String content, String type)
	{
		ArrayList<String> list = getSearchGoods(content, type);
		if(list == null || list.size() > 0)
		{//如果已存在，就修改时间
			String updateSQL = "update " + tableName + " set searchdate = CURRENT_TIMESTAMP where content = '" + content +"' and type='"+ type +"'";
			return exec(updateSQL);
		}
		String insertSQL = "insert into " + tableName + " (type, content, searchdate) values (?, ?, CURRENT_TIMESTAMP)";
		String [] params = {type, content};
		return exec(insertSQL, params);
	}
	
	private ArrayList<String> getSearchGoods(String content, String type)
	{
		ArrayList<String> listData = new ArrayList<>();
		String SQL = "select id, content from " + tableName +" where type = '" + type + "' and content = '" + content +"'";
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery(SQL, null);
		if (cursor.getCount() > 0) {
			while(cursor.moveToNext())
			{
				listData.add(cursor.getString(1));
			}
		}
		return listData;
	}
	/**
	 * 删除单条记录
	 * @param content
	 * @return
	 */
	public boolean DeleteContent(String content, String type)
	{
		String deleteSQL = "delete from " + tableName + " where content = '" + content +"' and type='"+type+"'";
		return exec(deleteSQL);
	}
	/**
	 * 清空表
	 * @param type 0,搜索商品;
	 * @return
	 */
	public boolean ClearTable(String type)
	{
		String deleteSQL = "delete from " + tableName +" where type='" + type +"'";
		return exec(deleteSQL);
	}
	
	private boolean exec(String SQL)
	{
		SQLiteDatabase db = getWritableDatabase();
		boolean success = true;
		try {
			db.execSQL(SQL);
		} catch (SQLException e) {
			success = false;
		}
		db.close();
		return success;
	}
	private boolean exec(String SQL, String str)
	{
		String[] object = {str};
		SQLiteDatabase db = getWritableDatabase();
		boolean success = true;
		try {
			db.execSQL(SQL, object);
		} catch (SQLException e) {
			success = false;
		}
		db.close();
		return success;
	}
	private boolean exec(String SQL, String[] object)
	{
		SQLiteDatabase db = getWritableDatabase();
		boolean success = true;
		try {
			db.execSQL(SQL, object);
		} catch (SQLException e) {
			success = false;
		}
		db.close();
		return success;
	}
}
