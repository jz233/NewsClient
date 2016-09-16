/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.activities;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import zjj.app.newsclient.BuildConfig;
import zjj.app.newsclient.R;
import zjj.app.newsclient.adapters.SearchResultsAdapter;
import zjj.app.newsclient.base.AppController;
import zjj.app.newsclient.base.BaseActivity;
import zjj.app.newsclient.base.BaseApplication;
import zjj.app.newsclient.domain.NewsList;
import zjj.app.newsclient.ui.VerticalSpaceItemDecoration;
import zjj.app.newsclient.utils.Constant;


public class SearchActivity extends BaseActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private RecyclerView rv_search_results;
    private SwipeRefreshLayout swipe_refresh_layout;
    private RecyclerView.Adapter adapter;

    @Override
    public void initView() {
        setContentView(R.layout.activity_search);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        rv_search_results = (RecyclerView) findViewById(R.id.rv_search_results);
        rv_search_results.setLayoutManager(new LinearLayoutManager(this));
        rv_search_results.addItemDecoration(new VerticalSpaceItemDecoration(5));

        swipe_refresh_layout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipe_refresh_layout.setColorSchemeResources(R.color.colorPrimary);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (BuildConfig.DEBUG) Log.d("SearchActivity", query);

            swipe_refresh_layout.setRefreshing(true);
            TreeMap<String, String> params = getRequestParams(query);
            AppController.getInstance().enqueueGetRequest(params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String body = response.body().string();
                    final NewsList newsList = new Gson().fromJson(body, NewsList.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipe_refresh_layout.setRefreshing(false);
                            adapter = new SearchResultsAdapter(SearchActivity.this, newsList);
                            rv_search_results.setAdapter(adapter);
                        }
                    });
                }
            });

        }else if(Intent.ACTION_VIEW.equals(intent.getAction())){
            if (BuildConfig.DEBUG) Log.d("SearchActivity", "ACTION_VIEW");
        }
    }

    public TreeMap<String, String> getRequestParams(String query){
        TreeMap<String, String> params = new TreeMap<>();
        params.put("page", "1");
        params.put("title", query);
        params.put("needContent", "0");
        params.put("needHtml", "1");
        return params;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchManager manager = (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
        SearchView view = (SearchView) menu.findItem(R.id.action_search).getActionView();
        view.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        view.setIconifiedByDefault(true);
        view.setIconified(false);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocusFromTouch();


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(android.R.id.home == itemId){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
