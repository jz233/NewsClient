/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.domain;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class FavoritesItem {
    private int id;
    private String title;
    private long date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public static List<FavoritesItem> fromCursor(Cursor cursor){
        List<FavoritesItem> list = null;
        FavoritesItem item;
        if(cursor != null && cursor.getCount()>0){
            list = new ArrayList<>();
            while (cursor.moveToNext()){
                item  = new FavoritesItem();
                item.setId(cursor.getInt(0));
                item.setTitle(cursor.getString(1));
                item.setDate(cursor.getLong(2));
                list.add(item);
            }
        }
        cursor.close();
        return list;
    }
}
