/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.ui;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import zjj.app.newsclient.utils.UIUtils;

public class BottomNavBehavior extends CoordinatorLayout.Behavior<LinearLayout>{

    private int toolbarHeight;

    public BottomNavBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.toolbarHeight = UIUtils.getToolBarHeight(context);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return super.layoutDependsOn(parent, child, dependency) || (dependency instanceof AppBarLayout);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {
        boolean result = super.onDependentViewChanged(parent, child, dependency);
        if(dependency instanceof AppBarLayout){
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            int bottomMargin = params.bottomMargin;
            int distanceToScroll = child.getHeight() + bottomMargin;
            float ratio = dependency.getY() / (float)toolbarHeight;
            child.setTranslationY(-distanceToScroll * ratio);
        }
        return result;
    }
}
