package zjj.app.newsclient.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import zjj.app.newsclient.R;
import zjj.app.newsclient.activities.NewsDetailsActivity;
import zjj.app.newsclient.domain.NewsList;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ResultsHolder> {

    private Context context;
    private List<NewsList.ShowapiResBodyBean.PagebeanBean.ContentlistBean> list;

    public SearchResultsAdapter(Context context, NewsList newsList) {
        this.context = context;
        this.list = newsList.getShowapi_res_body().getPagebean().getContentlist();
    }


    @Override
    public ResultsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_search_results, null);
        ResultsHolder holder = new ResultsHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ResultsHolder holder, int position) {
        NewsList.ShowapiResBodyBean.PagebeanBean.ContentlistBean bean = list.get(position);
        holder.tv_title.setText(bean.getTitle());
        holder.tv_date.setText(bean.getPubDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ResultsHolder extends RecyclerView.ViewHolder{

        private TextView tv_title;
        private TextView tv_date;

        public ResultsHolder(View itemView) {
            super(itemView);
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
