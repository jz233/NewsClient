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

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    public static SharedPreferences getSharedPreferences(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

    public static SharedPreferences.Editor getEditor(Context context, String name) {
        return getSharedPreferences(context, name).edit();
    }

    public static void putStringSet(Context context, String key, Set<String> values) {
        getEditor(context).putStringSet(key, values).apply();
    }

    public static void putStringSet(Context context, String name, String key, Set<String> values) {
        getEditor(context, name).putStringSet(key, values).apply();
    }

    public static Set<String> getStringSet(Context context, String key, Set<String> defaultValues) {
        return getSharedPreferences(context).getStringSet(key, defaultValues);
    }

    public static Set<String> getStringSet(Context context, String name, String key, Set<String> defaultValues) {
        return getSharedPreferences(context, name).getStringSet(key, defaultValues);
    }

    public static void putString(Context context, String key, String value) {
        getEditor(context).putString(key, value).apply();
    }

    public static void putString(Context context, String name, String key, String value) {
        getEditor(context, name).putString(key, value).apply();
    }

    public static String getString(Context context, String key, String defaultValue) {
        return getSharedPreferences(context).getString(key, defaultValue);
    }

    public static String getString(Context context, String name, String key, String defaultValue) {
        return getSharedPreferences(context, name).getString(key, defaultValue);
    }

    public static void putLong(Context context, String key, long value) {
        getEditor(context).putLong(key, value).apply();
    }

    public static void putLong(Context context, String name, String key, long value) {
        getEditor(context, name).putLong(key, value).apply();
    }

    public static long getLong(Context context, String key, long defaultValue) {
        return getSharedPreferences(context).getLong(key, defaultValue);
    }

    public static long getLong(Context context, String name, String key, long defaultValue) {
        return getSharedPreferences(context, name).getLong(key, defaultValue);
    }


}
