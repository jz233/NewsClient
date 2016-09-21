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


import java.util.List;

import zjj.app.newsclient.db.CommonDbHelper;
import zjj.app.newsclient.db.FavoritesDbHelper;
import zjj.app.newsclient.domain.FavoritesItem;
import zjj.app.newsclient.utils.Constant;

public class FavoritesDao extends CommonDao<FavoritesItem>{

    private final FavoritesDbHelper helper;

    public FavoritesDao(Context context) {
        helper = new FavoritesDbHelper(context);
    }

    @Override
    public long add(ContentValues values) {
        SQLiteDatabase db = helper.getWritableDatabase();
        long rowId = db.insert(Constant.TABLE_FAVORITES, null, values);
        db.close();
        return rowId;
    }

    @Override
    public List<FavoritesItem> getAll(String orderBy) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(Constant.TABLE_FAVORITES, null, null, null, null, null, orderBy);
        List<FavoritesItem> list = FavoritesItem.fromCursor(cursor);
        cursor.close();
        db.close();

        return list;
    }

    public int deleteByTitle(String title) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int affectedRows = db.delete(Constant.TABLE_FAVORITES, "title = ?", new String[]{title});
        db.close();
        return affectedRows;
    }

}
