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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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
    private static final int BAIDU_SDK_PERMISSION = 100;
    private final static int TYPE_FOCUSES = 0;
    private final static int TYPE_NEWEST = 1;
    private final static int TYPE_FAVORITE = 2;
    private final static int TYPE_ME = 3;
    private FrameLayout fragment_container;
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

    private LocationClient client;
    private BDLocationListener listener;
    private LocationClientOption option;
    private Handler handler;


    @Override
    public void initView() {
        setContentView(R.layout.activity_home);
        fragment_container = (FrameLayout) findViewById(R.id.fragment_container);
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

        client =  new LocationClient(getApplicationContext());
        if(listener == null){
            listener = new MyLocationListener();
            client.registerLocationListener(listener);
        }
    }

    @Override
    public void initData() {
        handler = new Handler(Looper.getMainLooper());
        if(!client.isStarted())
            client.start();
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

    public void getLocation() {
        option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);

        client.setLocOption(option);

    }

    private void requestLocationPermissions() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)||
                ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
            Snackbar.make(fragment_container, "You need grant the permissions", Snackbar.LENGTH_INDEFINITE).setAction("GO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,  Manifest.permission.ACCESS_COARSE_LOCATION}, BAIDU_SDK_PERMISSION);
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

    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            final Snackbar snackbar = Snackbar.make(fragment_container, location.getCity(), Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            }).show();

        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(listener != null){
            client.unRegisterLocationListener(listener);
            listener = null;
        }
    }
}
