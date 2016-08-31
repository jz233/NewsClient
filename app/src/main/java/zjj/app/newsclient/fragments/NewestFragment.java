package zjj.app.newsclient.fragments;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import zjj.app.newsclient.R;
import zjj.app.newsclient.adapters.NewsPagerAdapter;
import zjj.app.newsclient.base.BaseFragment;

public class NewestFragment extends BaseFragment{

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private TabLayout tab_layout;
    private ViewPager viewpager;
    private NewsPagerAdapter adapter;
    private String[] topics = {"国内", "国际", "教育"};
    private CoordinatorLayout root_view;

    public NewestFragment(){}

    public static NewestFragment newInstance(int type){
        NewestFragment fragment = new NewestFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_focuses, null);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        tab_layout = (TabLayout) view.findViewById(R.id.tab_layout);
        context.setSupportActionBar(toolbar);

        root_view = (CoordinatorLayout) view.findViewById(R.id.root_view);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        tab_layout = (TabLayout) view.findViewById(R.id.tab_layout);

        Bundle args = getArguments();
        int type = args.getInt("type", 0);

        adapter = new NewsPagerAdapter(context, context.getSupportFragmentManager(), topics, type);
        viewpager.setAdapter(adapter);
        tab_layout.setupWithViewPager(viewpager);

        return view;
    }

    @Override
    protected void initListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home, menu);
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
}
