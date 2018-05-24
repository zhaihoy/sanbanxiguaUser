package com.hbbc.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import com.hbbc.R;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017/7/27.
 */
public class HWEditText extends EditText {

    public HWEditText(Context context) {

        this(context, null);
    }



    public HWEditText(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }



    public HWEditText(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
        modifyTextCursorDrawable(context,attrs,defStyle);
    }



    private void modifyTextCursorDrawable(Context context, AttributeSet attrs, int defStyle) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HWEditText);
        int cursorDrawable = a.getResourceId(R.styleable.HWEditText_cursorDrawable,0);
        if(cursorDrawable!=0){
            try {
                Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
                mCursorDrawableRes.setAccessible(true);
                mCursorDrawableRes.set(this,cursorDrawable);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        a.recycle();
    }

}
