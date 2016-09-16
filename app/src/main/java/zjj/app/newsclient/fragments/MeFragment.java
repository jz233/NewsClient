/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.fragments;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import zjj.app.newsclient.R;
import zjj.app.newsclient.activities.RecentActivity;
import zjj.app.newsclient.base.BaseFragment;
import zjj.app.newsclient.ui.MySettingView;

public class MeFragment extends BaseFragment implements View.OnClickListener {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private MySettingView sv_recent;


    public MeFragment() {
    }

    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_me, null);
        toolbar = (Toolbar) view.findViewById(R.id.layout_appbar).findViewById(R.id.toolbar);
        context.setSupportActionBar(toolbar);
        actionBar = context.getSupportActionBar();
        actionBar.setTitle("我");

        sv_recent = (MySettingView) view.findViewById(R.id.sv_recent);

        return view;
    }

    @Override
    protected void initListener() {
        sv_recent.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(R.id.sv_recent == id){
            startActivity(new Intent(context, RecentActivity.class));
        }
    }
}
