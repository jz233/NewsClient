/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.fragments;


import android.view.LayoutInflater;
import android.view.View;

import zjj.app.newsclient.R;
import zjj.app.newsclient.base.BaseFragment;

public class DefaultFragment extends BaseFragment {


    public DefaultFragment() {
    }

    public static DefaultFragment newInstance() {
        DefaultFragment fragment = new DefaultFragment();
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_default, null);
        return view;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }
}
