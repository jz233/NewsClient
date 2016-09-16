/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Callback;
import zjj.app.newsclient.BuildConfig;
import zjj.app.newsclient.R;
import zjj.app.newsclient.base.AppController;
import zjj.app.newsclient.base.BaseActivity;
import zjj.app.newsclient.base.BaseApplication;
import zjj.app.newsclient.base.BaseFragment;
import zjj.app.newsclient.domain.Channels;
import zjj.app.newsclient.domain.NewsList;
import zjj.app.newsclient.fragments.DefaultFragment;
import zjj.app.newsclient.fragments.FavFragment;
import zjj.app.newsclient.fragments.MeFragment;
import zjj.app.newsclient.fragments.NewsListFragment;
import zjj.app.newsclient.utils.Constant;
import zjj.app.newsclient.utils.SharedPreferencesUtils;
import zjj.app.newsclient.utils.URLUtils;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private FragmentManager fm;
    private final static int TYPE_FOCUSES = 0;
    private final static int TYPE_NEWEST = 1;
    private final static int TYPE_FAVORITE = 2;
    private final static int TYPE_ME = 3;
    private LinearLayout ll_focuses;
    private LinearLayout ll_newest;
    private LinearLayout ll_favorite;
    private LinearLayout ll_me;
    private ImageButton ib_focuses;
    private int currentCheckedType = TYPE_FOCUSES;
    private ImageButton ib_newest;
    private ImageButton ib_favorite;
    private ImageButton ib_me;
    private TextView tv_me;
    private TextView tv_focuses;
    private TextView tv_newest;
    private TextView tv_favorite;
    private Fragment currentFragment = null;
    private LocationManager lm;
    private Geocoder geocoder;


    @Override
    public void initView() {
        setContentView(R.layout.activity_home);


        fm = getSupportFragmentManager();
        setupBottomNavBar();

        setCheckedFragment(TYPE_FOCUSES);

    }

    private void setupBottomNavBar() {
        ll_focuses = (LinearLayout) findViewById(R.id.ll_focuses);
        ll_newest = (LinearLayout) findViewById(R.id.ll_newest);
        ll_favorite = (LinearLayout) findViewById(R.id.ll_favorite);
        ll_me = (LinearLayout) findViewById(R.id.ll_me);

        ib_focuses = (ImageButton) findViewById(R.id.ib_focuses);
        ib_newest = (ImageButton) findViewById(R.id.ib_newest);
        ib_favorite = (ImageButton) findViewById(R.id.ib_favorite);
        ib_me = (ImageButton) findViewById(R.id.ib_me);

        tv_focuses = (TextView) findViewById(R.id.tv_focuses);
        tv_newest = (TextView) findViewById(R.id.tv_newest);
        tv_favorite = (TextView) findViewById(R.id.tv_favorite);
        tv_me = (TextView) findViewById(R.id.tv_me);

    }

    @Override
    public void initListener() {
        ll_focuses.setOnClickListener(this);
        ll_newest.setOnClickListener(this);
        ll_favorite.setOnClickListener(this);
        ll_me.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    /**
     *  添加或替换成要显示到前端的Fragment对象
     */
    private void setCheckedFragment(int type) {
        currentCheckedType = type;

        handleFragments(type);
        setCheckedIcon(type);

    }

    /**
     * 把选中的导航图标和文字高亮
     */
    private void setCheckedIcon(int type) {
        unCheckedAll();
        switch (type) {
            case TYPE_FOCUSES:
                ib_focuses.setSelected(true);
                tv_focuses.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                break;
            case TYPE_NEWEST:
                ib_newest.setSelected(true);
                tv_newest.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                break;
            case TYPE_FAVORITE:
                ib_favorite.setSelected(true);
                tv_favorite.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                break;
            case TYPE_ME:
                ib_me.setSelected(true);
                tv_me.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                break;
        }

    }

    /**
     * 把所有导航按钮状态设置为初始
     */
    private void unCheckedAll() {
        ib_focuses.setSelected(false);
        ib_newest.setSelected(false);
        ib_favorite.setSelected(false);
        ib_me.setSelected(false);
        tv_focuses.setTextColor(getResources().getColor(R.color.light_gray));
        tv_newest.setTextColor(getResources().getColor(R.color.light_gray));
        tv_favorite.setTextColor(getResources().getColor(R.color.light_gray));
        tv_me.setTextColor(getResources().getColor(R.color.light_gray));
    }

    public void getLocation() {
        geocoder = new Geocoder(context);
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(location != null){
//                        List<Address> addr = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (BuildConfig.DEBUG) Log.d("HomeActivity", String.valueOf(location.getLatitude()));

                }else{
                    if (BuildConfig.DEBUG) Log.d("HomeActivity", "location is null");
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
        lm.requestLocationUpdates(lm.getBestProvider(criteria, true), 1000, 0, listener);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null){
            if (BuildConfig.DEBUG)
                Log.d("HomeActivity", "location.getLatitude():" + location.getLatitude());
        }else{
            if (BuildConfig.DEBUG) Log.d("HomeActivity", "location == null");
        }
//        lm.requestSingleUpdate(criteria, listener, null);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.ll_focuses == id) {
            setCheckedFragment(TYPE_FOCUSES);
        } else if (R.id.ll_newest == id) {
            setCheckedFragment(TYPE_NEWEST);
        } else if (R.id.ll_favorite == id) {
            setCheckedFragment(TYPE_FAVORITE);
        } else if (R.id.ll_me == id) {
            setCheckedFragment(TYPE_ME);
        }
    }

    private void handleFragments(int type) {
        FragmentTransaction transaction = fm.beginTransaction();
        NewsListFragment focusesFragment = (NewsListFragment) fm.findFragmentByTag("FocusesFragment");
        NewsListFragment newestFragment = (NewsListFragment) fm.findFragmentByTag("NewestFragment");
        FavFragment favoritesFragment = (FavFragment) fm.findFragmentByTag("FavoritesFragment");
        MeFragment meFragment = (MeFragment) fm.findFragmentByTag("MeFragment");

        switch (type){
            case TYPE_FOCUSES:
                if(newestFragment != null)
                    transaction = transaction.hide(newestFragment);
                if(favoritesFragment != null)
                    transaction = transaction.hide(favoritesFragment);
                if(meFragment != null)
                    transaction = transaction.hide(meFragment);
                if(focusesFragment != null){
                    transaction.show(focusesFragment).commit();
                }else{
                    transaction.add(R.id.fragment_container, NewsListFragment.newInstance(TYPE_FOCUSES), "FocusesFragment").commit();
                }
                break;
            case TYPE_NEWEST:
                if(focusesFragment != null)
                    transaction = transaction.hide(focusesFragment);
                if(favoritesFragment != null)
                    transaction = transaction.hide(favoritesFragment);
                if(meFragment != null)
                    transaction = transaction.hide(meFragment);
                if(newestFragment != null){
                    transaction.show(newestFragment).commit();
                }else{
                    transaction.add(R.id.fragment_container, NewsListFragment.newInstance(TYPE_NEWEST), "NewestFragment").commit();
                }
                break;
            case TYPE_FAVORITE:
                if(focusesFragment != null)
                    transaction = transaction.hide(focusesFragment);
                if(newestFragment != null)
                    transaction = transaction.hide(newestFragment);
                if(meFragment != null)
                    transaction = transaction.hide(meFragment);
                if(favoritesFragment != null){
                    transaction.show(favoritesFragment).commit();
                }else{
                    transaction.add(R.id.fragment_container, FavFragment.newInstance(), "FavoritesFragment").commit();
                }
                break;
            case TYPE_ME:
                if(focusesFragment != null)
                    transaction = transaction.hide(focusesFragment);
                if(newestFragment != null)
                    transaction = transaction.hide(newestFragment);
                if(favoritesFragment != null)
                    transaction = transaction.hide(favoritesFragment);
                if(meFragment != null){
                    transaction.show(meFragment).commit();
                }else{
                    transaction.add(R.id.fragment_container, MeFragment.newInstance(), "MeFragment").commit();
                }
                break;
        }

        //一定要执行，否则之后无法使用findFragmentByTag查找已添加的fragment对象
        fm.executePendingTransactions();
    }


}
