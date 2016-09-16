/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.integration.okhttp3.OkHttpGlideModule;

import java.util.List;

import zjj.app.newsclient.BuildConfig;
import zjj.app.newsclient.R;
import zjj.app.newsclient.activities.NewsDetailsActivity;
import zjj.app.newsclient.base.BaseApplication;
import zjj.app.newsclient.domain.NewsList;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsItemHolder>{

    private Context context;
    private List<NewsList.ShowapiResBodyBean.PagebeanBean.ContentlistBean> list;

    public NewsListAdapter(Context context, NewsList newsList) {
        this.context = context;
        list = newsList.getShowapi_res_body().getPagebean().getContentlist();
    }

    @Override
    public NewsItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_news_list, null);
        NewsItemHolder holder = new NewsItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NewsItemHolder holder, int position) {
        NewsList.ShowapiResBodyBean.PagebeanBean.ContentlistBean bean = list.get(position);
        List<NewsList.ShowapiResBodyBean.PagebeanBean.ContentlistBean.ImageurlsBean> urls = bean.getImageurls();
        holder.tv_title.setText(bean.getTitle());
        holder.tv_date.setText(bean.getPubDate());

        if(urls == null || urls.size() == 0||TextUtils.isEmpty(urls.get(0).getUrl())){
            holder.iv_thumb.setImageResource(R.drawable.blank);
        }else{
            Glide.with(holder.iv_thumb.getContext())
                    .load(urls.get(0).getUrl())
                    .asBitmap()                         //gif强转静态
                    .error(R.drawable.blank)
                    .placeholder(R.drawable.loading)
                    .into(holder.iv_thumb);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NewsItemHolder extends RecyclerView.ViewHolder{

        public ImageView iv_thumb;
        public TextView tv_title;
        public TextView tv_date;
        public NewsItemHolder(View itemView) {
            super(itemView);
            iv_thumb = (ImageView) itemView.findViewById(R.id.iv_thumb);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsList.ShowapiResBodyBean.PagebeanBean.ContentlistBean bean = list.get(getAdapterPosition());
                    Intent intent = new Intent(context, NewsDetailsActivity.class);
                    intent.putExtra("bean", bean);
                    context.startActivity(intent);
                }
            });
        }
    }
}
