package zjj.app.newsclient.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zjj.app.newsclient.R;
import zjj.app.newsclient.adapters.NewsListAdapter;
import zjj.app.newsclient.base.BaseFragment;

public class DomesticFragment extends BaseFragment {

    private RecyclerView rv_domestic_list;
    private NewsListAdapter adapter;

    public DomesticFragment() {
    }

    public static DomesticFragment newInstance() {
        DomesticFragment fragment = new DomesticFragment();
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_domestic, null);
        rv_domestic_list = (RecyclerView) view.findViewById(R.id.rv_domestic_list);
        rv_domestic_list.setLayoutManager(new LinearLayoutManager(context));
        return view;

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        adapter = new NewsListAdapter(getActivity());
        rv_domestic_list.setAdapter(adapter);
    }
}
