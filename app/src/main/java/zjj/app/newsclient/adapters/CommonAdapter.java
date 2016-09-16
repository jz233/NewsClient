/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<CommonViewHolder>{
    protected Context context;
    protected int layoutId;
    protected List<T> datas;

    public CommonAdapter(Context context, int layoutId, List<T> datas) {
        this.context = context;
        this.layoutId = layoutId;
        this.datas = datas;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonViewHolder holder = CommonViewHolder.get(context, layoutId, parent);
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
//        holder.updatePosition(position)
        convert(holder, datas.get(position));
    }

    public abstract void convert(CommonViewHolder holder, T data);

    @Override
    public int getItemCount() {
        return datas.size();
    }
}
