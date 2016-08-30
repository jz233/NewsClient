package zjj.app.newsclient.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;

import zjj.app.newsclient.BuildConfig;
import zjj.app.newsclient.R;

public class UIUtils {

    public static int getToolBarHeight(Context context){
        final TypedArray array = context.getTheme().obtainStyledAttributes(new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) array.getDimension(0, 0);
        array.recycle();

        return toolbarHeight;
    }

}
