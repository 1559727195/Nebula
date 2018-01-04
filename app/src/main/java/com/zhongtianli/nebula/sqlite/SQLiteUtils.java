package com.zhongtianli.nebula.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static android.R.attr.data;

public class SQLiteUtils {
	public static final String DATABASE_NAME = "sepcond_memory_db";
	public static final String TABLE_NAME="specond";
	public static  final String  TABLE_NAME_LIU="specliu";
	private String TAG1 = "zhu";

	//创建数据库表
	public static DatabaseHelper createDBHelper(Context context){
		//创建一个DatabaseHelper对象
		DatabaseHelper dbHelper=new DatabaseHelper(context,DATABASE_NAME);//数据库名字
		return dbHelper;
	}

	//拿到数据库中所有的数据create table user(datetime varchar(30),content varchar(100),alerttime varchar(30))
	//该表有三个参数
	public  List<Map> getAll(DatabaseHelper dbHelper,String tabName) {
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		List<Map>list=new ArrayList<>();

		Cursor cursor=db.query(tabName,new String[]{"type", "deviceType",
							"deviceName","deviceMac"},null, null, null, null,null);
		while(cursor.moveToNext()){
			for(int i=0;i<cursor.getCount();i++){
				Map map=new HashMap();
				cursor.moveToPosition(i);
				map.put("type",cursor.getString(0));
				map.put("deviceType",cursor.getString(1));
				map.put("deviceName",cursor.getString(2));
				map.put("deviceMac",cursor.getString(3));//0->代表没有上传或者上传失败
				map.put("ProjectCode",cursor.getString(4));//项目编码
				Log.e(TAG1,cursor.getString(0)+","+cursor.getString(1)+"," +
						""+cursor.getString(2)+","+cursor.getString(3)+","+i);
				list.add(map);
			}
		}
		db.close();
		dbHelper.close();
		return  list;
	}

	//该表有三个参数
	public  List<ConcurrentHashMap> getProjectCodeDeviceList(DatabaseHelper dbHelper, String projectCode, String tabName) {
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		List<ConcurrentHashMap>list=new ArrayList<>();
		if (projectCode == null) {
			return  new ArrayList<>();
		}
		Cursor cursor=db.query(tabName,new String[]{}, "ProjectCode=?", new String[]{projectCode}, null, null, null);
		while(cursor.moveToNext()){
			for(int i=0;i<cursor.getCount();i++){
				ConcurrentHashMap map=new ConcurrentHashMap();
				cursor.moveToPosition(i);
				map.put("type",cursor.getString(0));
				map.put("deviceType",cursor.getString(1));
				map.put("deviceName",cursor.getString(2));
				map.put("deviceMac",cursor.getString(3));//0->代表没有上传或者上传失败
				Log.e(TAG1,cursor.getString(0)+","+cursor.getString(1)+"," +
						""+cursor.getString(2)+","+cursor.getString(3)+","+i);
				list.add(map);
			}
		}
		db.close();
		dbHelper.close();
		return  list;
	}


	//android条件查询
	public  List<Map> select(DatabaseHelper databaseHelper,String deviceMac,String tabName){
		SQLiteDatabase  db=databaseHelper.getReadableDatabase();
		Cursor  cursor=db.query(tabName, new String[]{}, "deviceMac=?", new String[]{deviceMac}, null, null, null);
		int count=cursor.getCount();//记录条数
		List<Map> list  = new ArrayList<>();
				while(cursor.moveToNext()){
			for(int i=0;i<cursor.getCount();i++){
				Map map = new HashMap();
				map.put("type",cursor.getString(0));
				map.put("deviceType",cursor.getString(1));
				map.put("deviceName",cursor.getString(2));
				map.put("deviceMac",cursor.getString(3));//0->代表没有上传或者上传失败
				map.put("ProjectCode",cursor.getString(4));//projectCode
				Log.e(TAG1,cursor.getString(0)+","+cursor.getString(1)+"," +
						""+cursor.getString(2)+","+cursor.getString(3)+","+cursor.getString(4)+","+i);
				list.add(map);
				cursor.moveToPosition(i);
			}
		}
        return   list;
	}

	//插入数据库的动作
	public void insert(DatabaseHelper databaseHelper, Map map, String tabName) {
		//生成ContentValues对象
		ContentValues values=new ContentValues();
		//向该对象当中插入键值对，其中键是列名，值是希望插入到这一列的值，值必须和数据库当中的数据类型一致
		values.put("deviceMac", (String) map.get("deviceMac"));
		values.put("deviceName",(String) map.get("deviceName"));
		values.put("deviceType",(String) map.get("deviceType"));//user.getalerttime()
		values.put("type",(String) map.get("type"));
		values.put("ProjectCode", (String) map.get("ProjectCode"));//添加项目编码
		SQLiteDatabase db=databaseHelper.getWritableDatabase();
		//调用insert方法，就可以将数据插入到数据库当中
		db.insert(tabName, null, values);
		databaseHelper.close();
		db.close();
	}

	//插入debug表，的打调试信息
	public  void insertDebug (DatabaseHelper databaseHelper,Map map,String tabName) {
		//生成ContentValues对象
		ContentValues values=new ContentValues();
		//向该对象当中插入键值对，其中键是列名，值是希望插入到这一列的值，值必须和数据库当中的数据类型一致
		values.put("send", (String) map.get("send"));
		values.put("receive",(String) map.get("receive"));
		SQLiteDatabase db=databaseHelper.getWritableDatabase();
		//调用insert方法，就可以将数据插入到数据库当中
		db.insert(tabName, null, values);
		databaseHelper.close();
		db.close();
	}

	//查询debug表，
	public List<Map> selectDebug (DatabaseHelper databaseHelper,String tabName) {
		SQLiteDatabase db=databaseHelper.getReadableDatabase();
		List<Map>list=new ArrayList<>();

		Cursor cursor=db.query(tabName,new String[]{"send", "receive"},null, null, null, null,null);
		while(cursor.moveToNext()){
			for(int i=0;i<cursor.getCount();i++){
				Map map=new HashMap();
				cursor.moveToPosition(i);
				map.put("send",cursor.getString(0));
				map.put("receive",cursor.getString(1));
				list.add(map);
			}
		}
		db.close();
		databaseHelper.close();
		return  list;
	}

	//执行删除debug操作,即删除debug蓝牙操作数据
	public void deleteDebug(DatabaseHelper databaseHelper,String tabName) {

		SQLiteDatabase db=databaseHelper.getWritableDatabase();
		//阐述表的所有数据
		/*db.delete("user",null,null);*/
		db.delete(tabName,null,null);
		databaseHelper.close();
		db.close();
	}

	//执行删除操作
	public void delete(DatabaseHelper databaseHelper, String deviceMac, String tabName, int size) {

		SQLiteDatabase db=databaseHelper.getWritableDatabase();
		//阐述表的所有数据
		/*db.delete("user",null,null);*/
		//从表中删除指定的一条数据
//		db.execSQL("DELETE FROM" + tabName + "WHERE deviceMac=" + deviceMac);
		db.delete(tabName,"deviceMac=?",new String[]{deviceMac});
		databaseHelper.close();
		db.close();
	}

	//执行更新操作
	public void upgrate(DatabaseHelper databaseHelper, Map map, String tabName) {
		SQLiteDatabase db=databaseHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("deviceMac", (String) map.get("deviceMac"));
		values.put("deviceName",(String) map.get("deviceName"));
		values.put("deviceType",(String) map.get("deviceType"));//user.getalerttime()
		values.put("type",(String) map.get("type"));
		values.put("ProjectCode",(String) map.get("ProjectCode"));
		db.update(tabName, values, "deviceMac=?", new String[]{(String) map.get("deviceMac")});
		databaseHelper.close();
		db.close();
     }
}