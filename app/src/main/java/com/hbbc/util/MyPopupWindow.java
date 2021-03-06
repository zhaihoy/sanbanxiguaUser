package com.hbbc.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.hbbc.R;

/**
 * Created by Administrator on 2017/7/6.
 * 搞一个自定义属性,用来定义此popup_window中要填充的 child view;
 */
public class MyPopupWindow extends FrameLayout {

    public MyPopupWindow(Context context) {

        this(context,null);
    }
    public MyPopupWindow(Context context, AttributeSet attrs) {

        this(context, attrs,0);
    }
    public MyPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a= context.obtainStyledAttributes(attrs,R.styleable.MyPopupWindow);
        int childResId = a.getResourceId(R.styleable.MyPopupWindow_childResId, R.layout.mshare_popup_view);
        a.recycle();
        View inflate = LayoutInflater.from(context).inflate(childResId, this, false);
        addView(inflate);

    }
}
