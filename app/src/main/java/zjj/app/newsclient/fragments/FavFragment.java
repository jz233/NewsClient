/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;

import java.util.List;

import zjj.app.newsclient.BuildConfig;
import zjj.app.newsclient.R;
import zjj.app.newsclient.activities.RecentActivity;
import zjj.app.newsclient.base.BaseFragment;
import zjj.app.newsclient.ui.MySettingView;

public class FavFragment extends BaseFragment {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private LinearLayout root_view;

    @Override
    public void onResume() {
        super.onResume();
        if (BuildConfig.DEBUG) Log.d("FavFragment", "onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (BuildConfig.DEBUG) Log.d("FavFragment", "onStop");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (BuildConfig.DEBUG) Log.d("FavFragment", "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (BuildConfig.DEBUG) Log.d("FavFragment", "onDestroy");
    }

    public FavFragment() {
    }

    public static FavFragment newInstance() {
        FavFragment fragment = new FavFragment();
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_fav, null);
        root_view = (LinearLayout) view.findViewById(R.id.root_view);
        toolbar = (Toolbar) view.findViewById(R.id.layout_appbar).findViewById(R.id.toolbar);
        context.setSupportActionBar(toolbar);
        actionBar = context.getSupportActionBar();
        actionBar.setTitle("我的收藏");


        return view;
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initData() {

    }





}
