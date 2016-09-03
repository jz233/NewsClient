package zjj.app.newsclient.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.TreeMap;

import zjj.app.newsclient.R;
import zjj.app.newsclient.activities.HomeActivity;
import zjj.app.newsclient.adapters.NewsListAdapter;
import zjj.app.newsclient.base.BaseActivity;
import zjj.app.newsclient.base.BaseApplication;
import zjj.app.newsclient.base.BaseFragment;
import zjj.app.newsclient.domain.NewsList;
import zjj.app.newsclient.ui.VerticalSpaceItemDecoration;
import zjj.app.newsclient.utils.Constant;

public class NewsPagerFragment extends BaseFragment {

    private RecyclerView rv_news_list;
    private NewsListAdapter adapter;
    private SwipeRefreshLayout swipe_refresh_layout;

    public NewsPagerFragment(){}

    public static NewsPagerFragment newInstance(String channelId, String page){
        return newInstance(channelId, page, "0", "0");
    }

    public static NewsPagerFragment newInstance(String channelId, String page, String needContent, String needHtml){
        NewsPagerFragment fragment = new NewsPagerFragment();
        Bundle args = new Bundle();
        args.putString("channelId", channelId);
        args.putString("page", page);
        args.putString("needContent", needContent);
        args.putString("needHtml", needHtml);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_newspager, null);
        rv_news_list = (RecyclerView) view.findViewById(R.id.rv_news_list);
        rv_news_list.setLayoutManager(new LinearLayoutManager(context));
        rv_news_list.addItemDecoration(new VerticalSpaceItemDecoration(10));
        swipe_refresh_layout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipe_refresh_layout.setColorSchemeResources(R.color.colorPrimary);

        return view;
    }

    @Override
    protected void initListener() {
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }

    @Override
    protected void initData() {
        swipe_refresh_layout.setRefreshing(true);
        TreeMap<String, String> params = getRequestParams();
        BaseApplication.getInstance().getJsonStringRequest(context, Constant.BASE_URL, params, "GetNewsList", new BaseApplication.NewsClientListener() {
            @Override
            public void OnNewsListResponse(NewsList newsList) {
                Log.d("BaseApplication","OnNewsListResponse");
                if(swipe_refresh_layout.isRefreshing()){
                    swipe_refresh_layout.setRefreshing(false);
                }
                adapter = new NewsListAdapter(getActivity(), newsList);
                rv_news_list.setAdapter(adapter);
            }
        });

    }

    public TreeMap<String, String> getRequestParams(){
        Bundle args = getArguments();
        String channelId = args.getString("channelId", "5572a109b3cdc86cf39001db");
        String page = args.getString("page", "1");
        String needContent = args.getString("needContent", "0");
        String needHtml = args.getString("needHtml", "0");

        TreeMap<String, String> params = new TreeMap<>();
        params.put("channelId", channelId);
        params.put("page", page);
        params.put("needContent", needContent);
        params.put("needHtml",needHtml);

        return params;
    }

}
