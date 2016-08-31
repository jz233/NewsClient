package zjj.app.newsclient.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.TreeMap;

import zjj.app.newsclient.R;
import zjj.app.newsclient.activities.HomeActivity;
import zjj.app.newsclient.adapters.NewsListAdapter;
import zjj.app.newsclient.base.BaseFragment;
import zjj.app.newsclient.domain.NewsList;
import zjj.app.newsclient.utils.Constant;

public class NewsPagerFragment extends BaseFragment {

    private RecyclerView rv_news_list;
    private NewsListAdapter adapter;
    private String channelId;
    private String age;

    public NewsPagerFragment(){}

    public static NewsPagerFragment newInstance(String channelId, String page){
        NewsPagerFragment fragment = new NewsPagerFragment();
        Bundle args = new Bundle();
        args.putString("channelId", channelId);
        args.putString("page", page);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_newspager, null);
        rv_news_list = (RecyclerView) view.findViewById(R.id.rv_news_list);
        rv_news_list.setLayoutManager(new LinearLayoutManager(context));

        return view;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        TreeMap<String, String> params = getRequestParams();
        ((HomeActivity)context).getNewsRequest(Constant.BASE_URL, params, new HomeActivity.NewsClientListener() {
            @Override
            public void OnNewsListResponse(NewsList newsList) {
                Log.d("NewsClient","OnNewsListResponse");
                adapter = new NewsListAdapter(getActivity(), newsList);
                rv_news_list.setAdapter(adapter);
            }
        });

    }

    public TreeMap<String, String> getRequestParams(){
        Bundle args = getArguments();
        String channelId = args.getString("channelId", "5572a109b3cdc86cf39001db");
        String page = args.getString("page", "1");

        TreeMap<String, String> params = new TreeMap<>();
        params.put("channelId", channelId);
        params.put("page", page);

        return params;
    }

}