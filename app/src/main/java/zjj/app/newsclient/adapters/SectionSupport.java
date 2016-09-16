/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.adapters;

public interface SectionSupport<T> {
    int sectionHeaderLayoutId();
    int sectionTitleTextViewId();
    String getTitle(T t);
}
