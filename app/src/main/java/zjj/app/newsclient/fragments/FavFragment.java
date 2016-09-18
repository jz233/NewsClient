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

    private static final int BAIDU_SDK_PERMISSION = 100;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private LinearLayout root_view;
    private Button btn_getLocation;
    private LocationClient client;
    private BDLocationListener listener;
    private LocationClientOption option;

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
        if(listener != null){
            client.unRegisterLocationListener(listener);
            listener = null;
        }
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

        btn_getLocation = (Button) view.findViewById(R.id.btn_getLocation);

        return view;
    }

    @Override
    protected void initListener() {

        client = new LocationClient(getActivity().getApplicationContext());
        if(listener == null){
            listener = new MyLocationListener();
            client.registerLocationListener(listener);
        }

        btn_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!client.isStarted()){
                    client.start();
                }
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(context, "requestLocationPermissions", Toast.LENGTH_SHORT).show();
                        requestLocationPermissions();
                        return;
                    }else{
                        getLocation();
                    }
                }else{
                    getLocation();
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\ncity : ");
            sb.append(location.getCity());
            Log.d("FavFragment", sb.toString());

        }
    }


    public void getLocation() {
        option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);

        client.setLocOption(option);
        client.start();
    }


    private void requestLocationPermissions() {
        if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)||
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
            Snackbar.make(root_view, "You need grant the permissions", Snackbar.LENGTH_INDEFINITE).setAction("GO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,  Manifest.permission.ACCESS_COARSE_LOCATION}, BAIDU_SDK_PERMISSION);
                }
            }).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case BAIDU_SDK_PERMISSION:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(context, "got it!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "权限不足，请后台打开位置权限", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

}
