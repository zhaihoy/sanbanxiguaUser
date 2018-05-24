package com.hbbc.mshare.user;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.amap.api.maps.MapView;


/**
 * Created by Administrator on 2017/8/8.
 *
 */
public class MMapView extends MapView {

    public MMapView(Context context) {

        super(context);
    }



    public MMapView(Context context, AttributeSet attributeSet) {

        super(context, attributeSet);
    }



    public MMapView(Context context, AttributeSet attributeSet, int i) {

        super(context, attributeSet, i);
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //TODO 内部拦截法尝试一下修复这个Bug
        if (ev.getAction() == MotionEvent.ACTION_DOWN)//是否可靠呢?-->没有问题*_*
            requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
