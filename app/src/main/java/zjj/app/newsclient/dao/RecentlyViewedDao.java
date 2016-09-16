/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import zjj.app.newsclient.BuildConfig;
import zjj.app.newsclient.db.RecentlyViewedDbHelper;
import zjj.app.newsclient.domain.RecentlyViewedItem;
import zjj.app.newsclient.utils.Constant;

public class RecentlyViewedDao {

    private RecentlyViewedDbHelper helper;

    public RecentlyViewedDao(Context context) {
        helper = new RecentlyViewedDbHelper(context);
    }

    public ArrayList<RecentlyViewedItem> getAll(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(Constant.TABLE_RECENT, null, null, null, null, null, "date DESC");
        ArrayList<RecentlyViewedItem> items = new ArrayList<>();
        RecentlyViewedItem item;
        while (cursor.moveToNext()){
            item = new RecentlyViewedItem();
            item.setId(cursor.getInt(0));
            item.setTitle(cursor.getString(1));
            item.setDate(cursor.getLong(2));

            items.add(item);
        }
        cursor.close();
        db.close();

        return items;
    }

    public int getCount(SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT COUNT(title) AS num FROM " + Constant.TABLE_RECENT +";", null);
        int result = 0;
        if(cursor != null && cursor.getCount()>0){
            cursor.moveToNext();
            result = cursor.getInt(cursor.getColumnIndex("num"));
        }
        if (BuildConfig.DEBUG) Log.d("RecentlyViewedDao", "get count:" + result);
        cursor.close();
        return result;
    }

    public long add(String title, long date){
        SQLiteDatabase db = helper.getWritableDatabase();
        //检查总数是否到限制(15)
        if(getCount(db) == 15){
            deleteEarliest(db);
        }
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("date", date);
        long result = db.insertWithOnConflict(Constant.TABLE_RECENT, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        if (BuildConfig.DEBUG) Log.d("RecentlyViewedDao", "try to do insert");
        if(result == -1){
            result = db.update(Constant.TABLE_RECENT, values, "title = ?", new String[]{title});
            if (BuildConfig.DEBUG) Log.d("RecentlyViewedDao", "updated");
        }
        db.close();

        return result;
    }

    public SQLiteDatabase deleteEarliest(SQLiteDatabase db){
        db.execSQL("DELETE FROM recent WHERE date = (SELECT MIN(date) FROM recent)");
        if (BuildConfig.DEBUG) Log.d("RecentlyViewedDao", "recent record over 15 deleted");
        return db;
    }
   /* public int getLatestViewedId(){
        int result = -1;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(Constant.TABLE_RECENT, new String[]{"_id"}, null, null, null, null, "date DESC");
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToNext();
            result = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return result;
    }*/
}
