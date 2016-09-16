/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import zjj.app.newsclient.R;

public class MySettingView extends RelativeLayout {
    private ImageView iv_setting_icon;
    private TextView tv_setting_title;


    public MySettingView(Context context) {
        super(context);
        initView(context);
    }

    public MySettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);

        String title = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "setting_title");
        int iconRes = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res-auto", "setting_icon", R.drawable.ic_menu_recent_img);
        setSettingTitle(title);
        setSettingIcon(iconRes);
    }

    public MySettingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(Context context) {
        //用this代表直接填充至当前类所代表的自定义控件下(当前类代表的自定义控件作为被填充入的Layout的父标签)
        View view = LayoutInflater.from(context).inflate(R.layout.ui_setting_item, this);
        iv_setting_icon = (ImageView) view.findViewById(R.id.iv_setting_icon);
        tv_setting_title = (TextView) view.findViewById(R.id.tv_setting_title);

    }

    private void setSettingTitle(String title){
        tv_setting_title.setText(title);
    }
    private void setSettingIcon(int iconRes){
        iv_setting_icon.setImageResource(iconRes);
    }

}
