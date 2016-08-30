package zjj.app.newsclient.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import zjj.app.newsclient.R;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsItemHolder>{

    private Context context;
    public NewsListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public NewsItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_news_list, null);
        NewsItemHolder holder = new NewsItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NewsItemHolder holder, int position) {
        holder.tv_info.setText(position + "");
    }

    @Override
    public int getItemCount() {
        return 30;
    }

    class NewsItemHolder extends RecyclerView.ViewHolder{

        public TextView tv_info;
        public NewsItemHolder(View itemView) {
            super(itemView);
            tv_info = (TextView) itemView.findViewById(R.id.tv_info);
        }
    }
}
