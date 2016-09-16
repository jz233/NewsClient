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
    private MySettingView sv_recent;
    private Geocoder geocoder;
    private LocationManager lm;
    private Button btn_getLocation;

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
        btn_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestLocationPermissions();
                        return;
                    }else{
                        getLocation();
                    }
                }
//                getLocation();
            }
        });
    }

    @Override
    protected void initData() {

    }


    public void getLocation() {
        geocoder = new Geocoder(context);
        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

       /* LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
//                        List<Address> addr = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (BuildConfig.DEBUG)
                        Log.d("FavFragment", String.valueOf(location.getLatitude()));
                    Toast.makeText(context, String.valueOf(location.getLatitude()), Toast.LENGTH_SHORT).show();
                } else {
                    if (BuildConfig.DEBUG) Log.d("FavFragment", "location is null");
                    Toast.makeText(context, "location is null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        lm.requestLocationUpdates(lm.getBestProvider(criteria, true), 1000, 0, listener);*/
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location != null){
            if (BuildConfig.DEBUG)
                Log.d("FavFragment", "location.getLatitude():" + location.getLatitude());
            Toast.makeText(context, String.valueOf(location.getLatitude()), Toast.LENGTH_SHORT).show();
        }else{
            if (BuildConfig.DEBUG) Log.d("FavFragment", "location == null");
            Toast.makeText(context, "location is null", Toast.LENGTH_SHORT).show();
        }
//        lm.requestSingleUpdate(criteria, listener, null);

    }


    private void requestLocationPermissions() {
        if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)||
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
            Snackbar.make(root_view, "my permission ...", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
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
//                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//                    if(location != null){
//                        if (BuildConfig.DEBUG)
//                            Log.d("FavFragment", "location.getLatitude():" + location.getLatitude());
//                        Toast.makeText(context, String.valueOf(location.getLatitude()), Toast.LENGTH_SHORT).show();
//                    }else{
//                        if (BuildConfig.DEBUG) Log.d("FavFragment", "location == null");
//                        Toast.makeText(context, "location is null", Toast.LENGTH_SHORT).show();
//                    }
                }else{
                    Toast.makeText(context, "权限不足，请打开位置权限", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }
}
