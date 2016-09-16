/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.adapters;

import android.content.Context;

import java.util.List;

public class SectionAdapter<T> extends MultiItemCommonAdapter<T>{

    public SectionAdapter(Context context, List<T> datas, MultiItemTypeSupport<T> support) {
        super(context, datas, support);
    }

    @Override
    public void convert(CommonViewHolder holder, T data) {

    }
}
