/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.utils;

import android.webkit.URLUtil;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class URLUtils {

    public static String getUrl(String baseUrl, TreeMap<String, String> paramMap){
        if(paramMap!=null){
            Set<Map.Entry<String, String>> entries = paramMap.entrySet();
            baseUrl += "?";
            String part;
            for(Map.Entry<String, String> entry : entries){
                part = entry.getKey() + "=" + entry.getValue() + "&";
                baseUrl += part;
            }
            baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf("&"));

        }

        return baseUrl;
    }
}
