/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.activities;

import android.content.ContentValues;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import zjj.app.newsclient.BuildConfig;
import zjj.app.newsclient.R;
import zjj.app.newsclient.base.AppController;
import zjj.app.newsclient.base.BaseActivity;
import zjj.app.newsclient.dao.FavoritesDao;
import zjj.app.newsclient.dao.RecentlyViewedDao;
import zjj.app.newsclient.domain.NewsList;

public class NewsDetailsActivity extends BaseActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
//    private TextView tv_content;
    private WebView wv_content;
    private RecentlyViewedDao rDao;
    private Handler handler;
    private SwipeRefreshLayout swipe_refresh_layout;
    private String title;
    private FavoritesDao fDao;

    @Override
    public void initView() {
        setContentView(R.layout.activity_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

//        tv_content = (TextView) findViewById(R.id.tv_content);
        wv_content = (WebView) findViewById(R.id.wv_content);
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

        final String title = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            this.title = title;
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
                            if (swipe_refresh_layout.isRefreshing()) {
                                swipe_refresh_layout.setRefreshing(false);
                            }
//                            tv_content.setText(Html.fromHtml(bean.getHtml()));
                            addToRecentViewed(title);
                        }
                    });
                }
            });
        } else {

            if (swipe_refresh_layout.isRefreshing()) {
                swipe_refresh_layout.setRefreshing(false);
            }
            NewsList.ShowapiResBodyBean.PagebeanBean.ContentlistBean bean = getIntent().getParcelableExtra("bean");
            this.title = bean.getTitle();
            String html = bean.getHtml();

            if (BuildConfig.DEBUG) Log.d("NewsDetailsActivity", html);
//            tv_content.setText(Html.fromHtml(bean.getHtml(), new NetworkImageGetter() , null));
            html += "<head><style>img{max-width: 100%; width:auto; height: auto;}</style></head>";
            wv_content.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            wv_content.loadData(html, "text/html; charset=UTF-8", null);
            //添加至"最近浏览"
            addToRecentViewed(this.title);
        }
    }

    private class NetworkImageGetter implements Html.ImageGetter{
        private InputStream is;
        private Drawable drawable;
        private String url;

        @Override
        public Drawable getDrawable(String source) {
            try {
                final URLConnection conn = new URL(source).openConnection();
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            conn.connect();
                            is = conn.getInputStream();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                drawable = new BitmapDrawable(getResources(), is);
                drawable.setBounds(1,1, 100, 100);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return drawable;
        }
    }

    private TreeMap<String, String> getParams(String title) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("title", title);
        params.put("needContent", "1");
        params.put("needHtml", "1");

        return params;
    }

    private void addToRecentViewed(final String title) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                rDao = new RecentlyViewedDao(NewsDetailsActivity.this);
                rDao.add(title, System.currentTimeMillis());
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
        if (android.R.id.home == itemId) {
            finish();
            return true;
        } else if (R.id.action_favorite == itemId) {
            addToFavorites(title);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addToFavorites(String title) {
        fDao = new FavoritesDao(this);
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("date", System.currentTimeMillis());
        fDao.add(values);
    }


    /**
     * 调用私有方法, 并使用反射调用隐藏方法, 强制在菜单中显示图标
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu instanceof MenuBuilder) {
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
