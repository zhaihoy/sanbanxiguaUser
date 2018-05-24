package com.hbbc.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Administrator on 2017/7/12.
 *
 */
public class CustomGridView extends GridView {

    public CustomGridView(Context context) {

        this(context, null);
    }



    public CustomGridView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }



    public CustomGridView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int adjustedHeightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, adjustedHeightMeasureSpec);
    }


}
