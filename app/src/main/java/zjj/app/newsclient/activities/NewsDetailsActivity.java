/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.activities;

import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import zjj.app.newsclient.BuildConfig;
import zjj.app.newsclient.R;
import zjj.app.newsclient.base.BaseActivity;
import zjj.app.newsclient.domain.NewsList;

public class NewsDetailsActivity extends BaseActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView tv_content;

    @Override
    public void initView() {
        setContentView(R.layout.activity_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tv_content = (TextView) findViewById(R.id.tv_content);

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        NewsList.ShowapiResBodyBean.PagebeanBean.ContentlistBean bean = getIntent().getParcelableExtra("bean");
        tv_content.setText(Html.fromHtml(bean.getHtml()));
        if (BuildConfig.DEBUG)
            Log.d("NewsDetailsActivity", "Html.fromHtml(bean.getHtml()):" + Html.fromHtml(bean.getHtml()));
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
