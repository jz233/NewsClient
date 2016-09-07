/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class SharedPreferencesUtils {

    public static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getEditor(Context context){
        return getSharedPreferences(context).edit();
    }

    public static void putStringSet(Context context, String key, Set<String> values){
        getEditor(context).putStringSet(key, values).apply();
    }

    public static Set<String> getStringSet(Context context, String key, Set<String> defaultValues){
        return getSharedPreferences(context).getStringSet(key, defaultValues);
    }

    public static void putString(Context context, String key, String value){
        getEditor(context).putString(key, value).apply();
    }

    public static String getString(Context context, String key, String defaultValue){
        return getSharedPreferences(context).getString(key, defaultValue);
    }


}
