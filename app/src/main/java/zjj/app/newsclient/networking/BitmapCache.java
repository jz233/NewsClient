/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.networking;


import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;


public class BitmapCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

    public BitmapCache(int size){
        super(size);
    }

    public BitmapCache(){
        super(getLruCacheSize());
    }

    /**
     * 缓存大小取分配给当前进程的理论最大内存容量的八分之一
     * 必须为static,否则无法作为参数放入当前类的构造方法
     * @return
     */
    public static int getLruCacheSize(){
        int maxMemory = (int) Runtime.getRuntime().maxMemory() / 1024;
        int cacheSize = maxMemory / 8;
        return cacheSize;
    }


    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}
