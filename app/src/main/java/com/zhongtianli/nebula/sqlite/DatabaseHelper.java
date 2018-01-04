package com.zhongtianli.nebula.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by Administrator on 2016/8/7.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    private static final int VERSION = 2;//现在数据库版本是2，因为添加了一个debug表
    //下次添加表或者添加表字段之前，VERSION = 2 不能变，否则会报错
    //使用SqlLiteOpenHelper创建的表的：目录路径是放在私有目录下的/data/data下的
     final String CREATE_DEBUG_TABLE = "create table debug (send varchar(500),receive varchar(500))";
    private String TAG = "robin-data";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context,String name,int version){
        this(context,name,null,version);
    }

        //添加两个构造方法的好处，就是可选择的传参-
    public DatabaseHelper(Context context,String name){
        this(context,name,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//只有把版本号,更新时才会进入执行onCreate()方法
       ///使用execSQL函数执行SQL语句-创建表
        db.execSQL("create table projectCode (type varchar(30),deviceType varchar(100),deviceName varchar(500),deviceMac varchar(100)" +
                ",ProjectCode varchar(500))");
        db.execSQL("DROP TABLE IF EXISTS debug");//在这里创建debug，防止应用被卸载，
        db.execSQL(CREATE_DEBUG_TABLE);
    }

    //deviceMac,deviceName,deviceType,type
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //进到这里来，说明有newVersion新版本了
        if (newVersion > oldVersion) {//如果有新版本的数据库，就执行onUpgrade更新方法，在这里添加，创建debug，防止应用没有被卸载，而是更新
            db.execSQL("DROP TABLE IF EXISTS debug");
            db.execSQL(CREATE_DEBUG_TABLE);
        }
    }
}
