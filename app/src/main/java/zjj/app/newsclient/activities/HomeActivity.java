package zjj.app.newsclient.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import zjj.app.newsclient.BuildConfig;
import zjj.app.newsclient.R;
import zjj.app.newsclient.adapters.NewsPagerAdapter;
import zjj.app.newsclient.base.BaseActivity;
import zjj.app.newsclient.base.BaseApplication;
import zjj.app.newsclient.domain.NewsList;
import zjj.app.newsclient.fragments.FocusesFragment;
import zjj.app.newsclient.networking.NewsClient;
import zjj.app.newsclient.utils.Constant;
import zjj.app.newsclient.utils.UIUtils;
import zjj.app.newsclient.utils.URLUtils;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private NewsClientListener listener;
    private FragmentManager fm;
    private final static int TYPE_FOCUSES = 0;
    private final static int TYPE_NEWEST = 1;
    private LinearLayout ll_focuses;
    private LinearLayout ll_newest;


    @Override
    public void initView() {
        setContentView(R.layout.activity_home);
        fm = getSupportFragmentManager();

        setupBottomNavBar();

        setCheckedFragment(TYPE_FOCUSES);

    }

    private void setCheckedFragment(int type) {
        fm.beginTransaction()
                .replace(R.id.fragment_container, FocusesFragment.newInstance(type), "FocusesFragment")
                .commit();

    }

    private void setupBottomNavBar() {
        ll_focuses = (LinearLayout) findViewById(R.id.ll_focuses);
        ll_newest = (LinearLayout) findViewById(R.id.ll_newest);
    }

    @Override
    public void initListener() {
        ll_focuses.setOnClickListener(this);
        ll_newest.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(R.id.ll_focuses == id){
            setCheckedFragment(TYPE_FOCUSES);
        }else if(R.id.ll_newest == id){
            setCheckedFragment(TYPE_NEWEST);
        }
    }

    public interface NewsClientListener{
        void OnNewsListResponse(NewsList newsList);
    }

    public void getNewsRequest(String baseUrl, TreeMap<String, String> paramMap, final NewsClientListener listener){
        String url = URLUtils.getUrl(baseUrl, paramMap);

        if (BuildConfig.DEBUG) Log.d("HomeActivity", url);
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
                Toast.makeText(HomeActivity.this, "network error", Toast.LENGTH_SHORT).show();
                Log.d("NewsClient", "network error");
                if(error != null){
                    Log.d("NewsClient", error.getMessage());
                }
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

        BaseApplication.getInstance().getRequestQueue().add(request);
    }
}
