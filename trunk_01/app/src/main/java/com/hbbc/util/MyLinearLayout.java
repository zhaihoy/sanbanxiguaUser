package com.hbbc.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.hbbc.R;

/**
 * Created by Administrator on 2017/7/14.
 *
 */
public class MyLinearLayout extends LinearLayout {

    private static final String TAG = "MyLinearLayout";

    public MyLinearLayout(Context context) {

        this(context, null);
    }



    public MyLinearLayout(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }



    public MyLinearLayout(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
//        setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                UIUtil.drawViewTouchShow(v,event);
//                return false;
//            }
//        });
        setBackgroundResource(R.drawable.my_linearlayout_selector);

    }



    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //根据事件传递与处理的流程:必须要重写拦截事件,屏蔽掉内部的两个RadioButton先响应事件这个默认执行流程.
        // setOnTouchListener同样是晚于onIntercepTouchEvent()执行,而先于onTouchEvent()方法执行.
        return true;
    }





}
