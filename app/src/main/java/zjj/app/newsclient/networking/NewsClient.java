package zjj.app.newsclient.networking;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import zjj.app.newsclient.utils.Constant;
import zjj.app.newsclient.utils.SharedPreferencesUtils;
import zjj.app.newsclient.utils.URLUtils;

public class NewsClient extends Fragment {

    private BitmapCache bitmapCache;
    private RequestQueue requestQueue;
    private ImageLoader loader;
    private NewsClientListener listener;

    public NewsClient(){}

    public interface NewsClientListener{
        void OnNewsListResponse(NewsList newsList);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            listener = (NewsClientListener) context;
        }catch (Exception e){
            throw new ClassCastException("需要实现NewsClientListener接口");
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        bitmapCache = new BitmapCache();
        loader = new ImageLoader(requestQueue, bitmapCache);


        setRetainInstance(true);

    }


    public void cancelAll(){
        requestQueue.cancelAll(null);
    }

    public ImageLoader getImageLoader(){
        return loader;
    }

    public BitmapCache getBitmapCache(){
        return bitmapCache;
    }


    public void getNewsRequest(String baseUrl, TreeMap<String, String> paramMap){
        String url = URLUtils.getUrl(baseUrl, paramMap);

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (BuildConfig.DEBUG) Log.d("NewsClient", response);
                            NewsList newsList = new Gson().fromJson(response, NewsList.class);

                            listener.OnNewsListResponse(newsList);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity().getApplicationContext(), "network error", Toast.LENGTH_SHORT).show();
                            if (BuildConfig.DEBUG) Log.d("NewsClient", error.getMessage());
                        }
                    })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("apikey", Constant.APIKEY);

                return headers;
            }
        };

        requestQueue.add(request);
    }


    //按Home显示桌面后切换到其他程序时(只是将进程切换至后台，未关闭)
    //onDestroy在点back键关闭fragment或整个程序时调用
    @Override
    public void onStop() {
        cancelAll();
        requestQueue = null;

        super.onStop();
    }

}
