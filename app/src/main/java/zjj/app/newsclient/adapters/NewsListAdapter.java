package zjj.app.newsclient.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import zjj.app.newsclient.BuildConfig;
import zjj.app.newsclient.R;
import zjj.app.newsclient.activities.NewsDetailsActivity;
import zjj.app.newsclient.base.BaseApplication;
import zjj.app.newsclient.domain.NewsList;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsItemHolder>{

    private final ImageLoader imageLoader;
    private Context context;
    private List<NewsList.ShowapiResBodyBean.PagebeanBean.ContentlistBean> list;

    public NewsListAdapter(Context context, NewsList newsList) {
        this.context = context;
        list = newsList.getShowapi_res_body().getPagebean().getContentlist();
        imageLoader = BaseApplication.getInstance().getImageLoader();
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

        if(urls == null || urls.size() == 0){
            holder.niv_thumb.setImageUrl("https://www.showapi.com/images/provide_left.png", imageLoader);
        }else if(TextUtils.isEmpty(urls.get(0).getUrl())){
            holder.niv_thumb.setImageUrl("https://www.showapi.com/images/provide_left.png", imageLoader);
        }else{
            holder.niv_thumb.setImageUrl(urls.get(0).getUrl(), imageLoader);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NewsItemHolder extends RecyclerView.ViewHolder{

        public NetworkImageView niv_thumb;
        public TextView tv_title;
        public TextView tv_date;
        public NewsItemHolder(View itemView) {
            super(itemView);
            niv_thumb = (NetworkImageView) itemView.findViewById(R.id.niv_thumb);
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
