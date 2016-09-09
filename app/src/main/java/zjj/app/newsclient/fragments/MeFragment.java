/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.fragments;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import zjj.app.newsclient.R;
import zjj.app.newsclient.base.BaseFragment;

public class MeFragment extends BaseFragment {

    private Toolbar toolbar;
    private ActionBar actionBar;

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

        return view;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }
}
