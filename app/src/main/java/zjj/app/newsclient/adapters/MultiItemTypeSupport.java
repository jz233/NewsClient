/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.adapters;

public interface MultiItemTypeSupport<T> {
    int getLayoutId(int itemType);
    int getItemViewType(int position, T t);
}
