/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CommonViewHolder extends RecyclerView.ViewHolder {
    //比HashMap更高效
    private SparseArray<View> views;
    private View convertView;
    private Context context;

    public CommonViewHolder(Context context, View itemView, ViewGroup parent) {
        super(itemView);
        this.context = context;
        this.convertView = itemView;
        this.views = new SparseArray<>();
    }

    /**
     * 静态方法得到holder
     */
    public static CommonViewHolder get(Context context, int layoutId, ViewGroup parent){
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        CommonViewHolder holder = new CommonViewHolder(context, itemView, parent);
        return holder;
    }

    /**
     * 根据控件id得到对象
     */
    public <T extends View> T getView(int viewId){
        View view = views.get(viewId);
        if(view == null){
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public CommonViewHolder setHolderText(int viewId, String value){
        TextView tv = getView(viewId);
        tv.setText(value);

        return this;
    }

    public CommonViewHolder setHolderImageResource(int viewId, int resId){
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);

        return this;
    }

    public CommonViewHolder setHolderOnClickListener(View.OnClickListener listener){
        convertView.setOnClickListener(listener);

        return this;
    }
}
