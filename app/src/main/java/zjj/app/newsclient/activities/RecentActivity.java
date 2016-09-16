/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.activities;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import zjj.app.newsclient.R;
import zjj.app.newsclient.adapters.RecentlyViewedAdapter;
import zjj.app.newsclient.base.BaseActivity;
import zjj.app.newsclient.dao.RecentlyViewedDao;
import zjj.app.newsclient.domain.RecentlyViewedItem;
import zjj.app.newsclient.ui.VerticalSpaceItemDecoration;
import zjj.app.newsclient.utils.SharedPreferencesUtils;

public class RecentActivity extends BaseActivity {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private RecyclerView rv_recent;
    private RecentlyViewedAdapter adapter;

    @Override
    public void initView() {
        setContentView(R.layout.activity_recent);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("最近浏览");

        rv_recent = (RecyclerView) findViewById(R.id.rv_recent);
        rv_recent.setLayoutManager(new LinearLayoutManager(context));
        rv_recent.addItemDecoration(new VerticalSpaceItemDecoration(3));
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        new RecentlyViewedTask().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recent_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(android.R.id.home == itemId){
            finish();
        }else if(R.id.action_clearAll == itemId){
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("清空历史记录")
                    .setMessage("您确定要清除吗?")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
        return true;
    }

    private class RecentlyViewedTask extends AsyncTask<Void, Void, List<RecentlyViewedItem>>{

        @Override
        protected List<RecentlyViewedItem> doInBackground(Void... params) {
            ArrayList<RecentlyViewedItem> items = new RecentlyViewedDao(context).getAll();
            return items;
        }

        @Override
        protected void onPostExecute(List<RecentlyViewedItem> items) {
            super.onPostExecute(items);
            adapter = new RecentlyViewedAdapter(context, R.layout.item_recent, items);
            rv_recent.setAdapter(adapter);
        }
    }
}
