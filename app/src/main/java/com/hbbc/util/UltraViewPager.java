package com.hbbc.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/8/14.
 */
public class UltraViewPager extends ViewPager{

    public UltraViewPager(Context context) {

        super(context, null);
    }



    public UltraViewPager(Context context, AttributeSet attrs) {

        super(context, attrs);
    }



    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        }catch (Exception e){
            return false;
        }
    }
}
