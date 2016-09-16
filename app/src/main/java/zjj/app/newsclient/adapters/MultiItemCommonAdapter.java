/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.adapters;

import android.content.Context;
import android.view.ViewGroup;

import java.util.List;

public abstract class MultiItemCommonAdapter<T> extends CommonAdapter<T>{

    private MultiItemTypeSupport<T> support;

    public MultiItemCommonAdapter(Context context, List<T> datas, MultiItemTypeSupport<T> support) {
        super(context, -1, datas);
        this.support = support;

    }

    @Override
    public int getItemViewType(int position) {
        return support.getItemViewType(position, datas.get(position));
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = support.getLayoutId(viewType);
        CommonViewHolder holder = CommonViewHolder.get(context, layoutId, parent);
        return holder;
    }

}
