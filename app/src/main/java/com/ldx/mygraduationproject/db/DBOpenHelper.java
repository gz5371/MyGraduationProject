package com.ldx.mygraduationproject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by freeFreAme on 2019/1/30.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "StepCounter.db"; //数据库名称
    private static final int DB_VERSION = 1;//数据库版本,大于0

    //用于创建Banner表
    private static final String CREATE_BANNER = "create table step ("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "curDate TEXT, "
            + "totalSteps TEXT)";


    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BANNER);//执行用户步数表语句
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
