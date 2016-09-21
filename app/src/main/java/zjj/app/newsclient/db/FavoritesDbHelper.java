/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.db;


import android.content.Context;

public class FavoritesDbHelper extends CommonDbHelper{

    public FavoritesDbHelper(Context context) {
        super(context,
                "favorites.db",
                "create table favorites (_id integer primary key autoincrement, title varchar(128) UNIQUE, date integer)");
    }
}
