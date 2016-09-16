/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.activities;

import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import zjj.app.newsclient.BuildConfig;
import zjj.app.newsclient.R;
import zjj.app.newsclient.base.AppController;
import zjj.app.newsclient.base.BaseActivity;
import zjj.app.newsclient.dao.RecentlyViewedDao;
import zjj.app.newsclient.domain.NewsList;
import zjj.app.newsclient.utils.Constant;
import zjj.app.newsclient.utils.SharedPreferencesUtils;

public class NewsDetailsActivity extends BaseActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView tv_content;
    private RecentlyViewedDao dao;
    private Handler handler;
    private SwipeRefreshLayout swipe_refresh_layout;

    @Override
    public void initView() {
        setContentView(R.layout.activity_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tv_content = (TextView) findViewById(R.id.tv_content);
        swipe_refresh_layout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipe_refresh_layout.setColorSchemeResources(R.color.colorPrimary);

    }

    @Override
    public void initListener() {
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDetails();
            }
        });
    }

    @Override
    public void initData() {
        handler = new Handler(Looper.getMainLooper());
        getDetails();
    }

    private void getDetails() {
        swipe_refresh_layout.setRefreshing(true);

        String title = getIntent().getStringExtra("title");
        if(!TextUtils.isEmpty(title)){

            AppController.getInstance().enqueueGetRequest(getParams(title), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String body = response.body().string();
                    NewsList newsList = new Gson().fromJson(body, NewsList.class);
                    final NewsList.ShowapiResBodyBean.PagebeanBean.ContentlistBean bean = newsList.getShowapi_res_body().getPagebean().getContentlist().get(0);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(swipe_refresh_layout.isRefreshing()){
                                swipe_refresh_layout.setRefreshing(false);
                            }
                            tv_content.setText(Html.fromHtml(bean.getHtml()));
                            addToRecentViewed(bean);
                        }
                    });
                }
            });
        }else{
            //模拟载入等待效果
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        Thread.sleep(2000);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(swipe_refresh_layout.isRefreshing()){
                                    swipe_refresh_layout.setRefreshing(false);
                                }
                                NewsList.ShowapiResBodyBean.PagebeanBean.ContentlistBean bean = getIntent().getParcelableExtra("bean");
                                tv_content.setText(Html.fromHtml(bean.getHtml()));
                                addToRecentViewed(bean);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        }
    }

    private TreeMap<String, String> getParams(String title) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("title", title);
        params.put("needContent", "1");
        params.put("needHtml", "1");

        return params;
    }

    private void addToRecentViewed(final NewsList.ShowapiResBodyBean.PagebeanBean.ContentlistBean bean) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                dao = new RecentlyViewedDao(NewsDetailsActivity.this);
                dao.add(bean.getTitle(), System.currentTimeMillis());
            }
        }.start();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(android.R.id.home == itemId){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 调用私有方法, 强制菜单中显示图标
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if(menu instanceof MenuBuilder){
            try {
                Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                method.setAccessible(true);
                method.invoke(menu, true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
