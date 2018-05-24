package com.hbbc.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/8/23.
 * 实现一个矩形的窗器
 */
public class RectangleFrameLayout extends FrameLayout {

    public RectangleFrameLayout(Context context) {

        this(context, null);
    }



    public RectangleFrameLayout(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }



    public RectangleFrameLayout(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int adjustedHeightSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.getMode(widthMeasureSpec));

        super.onMeasure(widthMeasureSpec, adjustedHeightSpec);

    }
}
