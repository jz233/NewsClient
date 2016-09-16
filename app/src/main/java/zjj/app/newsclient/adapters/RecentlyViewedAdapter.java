/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.Formatter;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.List;

import zjj.app.newsclient.R;
import zjj.app.newsclient.activities.NewsDetailsActivity;
import zjj.app.newsclient.domain.RecentlyViewedItem;

public class RecentlyViewedAdapter extends CommonAdapter<RecentlyViewedItem> {
    public RecentlyViewedAdapter(Context context, int layoutId, List<RecentlyViewedItem> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(CommonViewHolder holder, RecentlyViewedItem data) {
        final String title = data.getTitle();
        holder.setHolderText(R.id.tv_title, title);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        holder.setHolderText(R.id.tv_date, format.format(data.getDate()));
        holder.setHolderOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsDetailsActivity.class);
                intent.putExtra("title", title);

                context.startActivity(intent);
            }
        });
    }
}
