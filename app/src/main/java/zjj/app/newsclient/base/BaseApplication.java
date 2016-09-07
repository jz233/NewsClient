package zjj.app.newsclient.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import zjj.app.newsclient.BuildConfig;
import zjj.app.newsclient.domain.Channels;
import zjj.app.newsclient.domain.NewsList;
import zjj.app.newsclient.networking.BitmapCache;
import zjj.app.newsclient.utils.Constant;
import zjj.app.newsclient.utils.SharedPreferencesUtils;
import zjj.app.newsclient.utils.URLUtils;

public class BaseApplication extends Application {

    private BitmapCache bitmapCache;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static BaseApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        bitmapCache = new BitmapCache();
        requestQueue = getRequestQueue();
        imageLoader = new ImageLoader(requestQueue, bitmapCache);

        updateChannelsRequest();
    }

    public static synchronized BaseApplication getInstance(){
        return instance;
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(this);
        }

        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag){
        if(!TextUtils.isEmpty(tag)){
            req.setTag(tag);
        }

        getRequestQueue().add(req);

    }

    public void cancelAllRequests(Object tag){
        if(requestQueue != null){
            requestQueue.cancelAll(tag);
        }
    }


    /**
     * 每次启动更新一遍新闻分类channelId
     */
    public void updateChannelsRequest(){
        StringRequest request = new StringRequest(Request.Method.GET, Constant.URL_CHANNELS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (BuildConfig.DEBUG) Log.d("NewsClient", response);
                Channels channels = new Gson().fromJson(response, Channels.class);
                List<Channels.ShowapiResBodyBean.ChannelListBean> list = channels.getShowapi_res_body().getChannelList();
                SharedPreferences.Editor editor = SharedPreferencesUtils.getEditor(getApplicationContext());
                for(Channels.ShowapiResBodyBean.ChannelListBean item : list){
                    editor = editor.putString(item.getName(), item.getChannelId());
                }
                editor.apply();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "network error", Toast.LENGTH_SHORT).show();
                if (BuildConfig.DEBUG) Log.d("NewsClient", error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("apikey", Constant.APIKEY);
                return headers;
            }
        };

        addToRequestQueue(request, "UpdateChannels");
    }



    public ImageLoader getImageLoader(){
        return imageLoader;
    }


    public interface NewsClientListener{
        void OnNewsListResponse(NewsList newsList);
    }


    public void getJsonStringRequest(final Context context, String baseUrl, TreeMap<String, String> paramMap, String tag, final NewsClientListener listener) {
        String url = URLUtils.getUrl(baseUrl, paramMap);

        if (BuildConfig.DEBUG) Log.d("BaseApplication", url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (BuildConfig.DEBUG) Log.d("BaseApplication", response);
                        NewsList newsList = new Gson().fromJson(response, NewsList.class);
                        listener.OnNewsListResponse(newsList);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "network error", Toast.LENGTH_SHORT).show();
                Log.d("BaseApplication", "network error");
                if (error != null) {
                    Log.d("BaseApplication", error.getMessage());
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("apikey", Constant.APIKEY);

                return headers;
            }
        };

        BaseApplication.getInstance().addToRequestQueue(request, tag);
    }
}
