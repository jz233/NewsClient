package zjj.app.newsclient.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.TreeMap;

import zjj.app.newsclient.R;
import zjj.app.newsclient.adapters.NewsPagerAdapter;
import zjj.app.newsclient.base.BaseActivity;
import zjj.app.newsclient.base.BaseApplication;
import zjj.app.newsclient.domain.NewsList;
import zjj.app.newsclient.networking.NewsClient;
import zjj.app.newsclient.utils.Constant;
import zjj.app.newsclient.utils.UIUtils;

public class HomeActivity extends BaseActivity implements NewsClient.NewsClientListener{

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private TabLayout tab_layout;
    private ViewPager viewpager;
    private NewsPagerAdapter adapter;
    private String[] topics = {"国内", "国际", "体育"};
    private CoordinatorLayout root_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        root_view = (CoordinatorLayout) findViewById(R.id.root_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);

        adapter = new NewsPagerAdapter(getSupportFragmentManager(), topics);
        viewpager.setAdapter(adapter);
        tab_layout.setupWithViewPager(viewpager);



    }

    @Override
    public void initListener() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void initData() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Snackbar.make(root_view, "Settings", Snackbar.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnNewsListResponse(NewsList newsList) {

    }
}
