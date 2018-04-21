package com.android.tweedle.heartrate.dataHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class HandleData  {
    private MyDatabaseHelper dbHelper;
    private Context mcontext;
    private SQLiteDatabase db;
    public HandleData(Context context){
        mcontext = context;
        //创建数据库和表
        dbHelper = new MyDatabaseHelper(mcontext,"HeartRateData.db",null,1);
        db = dbHelper.getWritableDatabase();
//        db.close();
    }
    //向用户表中插入数据
    public long insertDataToUsers(String name,String psw,String signature){
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("password",psw);
        values.put("signature","个性宣言");
        long flag = db.insert("users",null,values);
        values.clear();//清除value
        db.close();//释放资源
        return flag;

    }
    //更改user用户表密码
    public int updateUsers(String name ,String psw){
        ContentValues values = new ContentValues();
        values.put("password",psw);
        int rowNum = db.update("users",values,"name = ?",new String[]{name});
        db.close();
        return rowNum;
    }

    public MyDatabaseHelper getDbHelper() {
        return dbHelper;
    }

    public void setDbHelper(MyDatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }
}
