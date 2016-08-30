package zjj.app.newsclient.fragments;

import android.view.LayoutInflater;
import android.view.View;

import zjj.app.newsclient.R;
import zjj.app.newsclient.base.BaseFragment;

public class InternationalFragment extends BaseFragment {

    public InternationalFragment() {
    }

    public static InternationalFragment newInstance() {
        InternationalFragment fragment = new InternationalFragment();
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_default, null);
        return view;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }
}
