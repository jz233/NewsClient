/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.base;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http.RetryAndFollowUpInterceptor;
import zjj.app.newsclient.BuildConfig;
import zjj.app.newsclient.activities.HomeActivity;
import zjj.app.newsclient.domain.Channels;
import zjj.app.newsclient.utils.Constant;
import zjj.app.newsclient.utils.SharedPreferencesUtils;

public class AppController extends Application {

    private static final OkHttpClient client = new OkHttpClient();
    private static AppController instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Glide.get(this).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
    }

    public static synchronized AppController getInstance(){return instance;}
    public static synchronized OkHttpClient getClient() {
        return client;
    }

    public void enqueueGetRequest(TreeMap<String, String> params, Callback callback) {
        HttpUrl url = composeUrl(params);
        Request request = new Request.Builder().get().url(url).addHeader("apikey", Constant.APIKEY).build();
        client.newCall(request).enqueue(callback);
    }

    private void executeGetRequest() {

    }


    private HttpUrl composeUrl(TreeMap<String, String> params) {
        Set<Map.Entry<String, String>> entries = params.entrySet();
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder = builder.scheme("http").host("apis.baidu.com")
                .addPathSegments("showapi_open_bus/channel_news/search_news");
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : entries) {
                builder = builder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

    public void doUpdateChannelsRequest(Callback callback) {
        Request request = new Request.Builder().get()
                .url("http://apis.baidu.com/showapi_open_bus/channel_news/channel_news")
                .addHeader("apikey", Constant.APIKEY)
                .build();
        client.newCall(request).enqueue(callback);

    }


}
