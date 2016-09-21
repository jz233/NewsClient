/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.dao;


import android.content.ContentValues;

import java.util.List;

import zjj.app.newsclient.db.CommonDbHelper;

public abstract class CommonDao<T> {


    public T getOne(){
        return null;
    }

    public List<T> getAll(String orderBy){
        return null;
    }

    public int getCount(){
        return 0;
    }

    public long add(ContentValues values){
        return 0;
    }

    public int delete(){
        return 0;
    }

}
