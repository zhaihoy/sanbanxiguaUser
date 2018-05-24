package com.hbbc.mshare.order;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreFooterLayout;
import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.hbbc.R;

/**
 * Created by Administrator on 2017/10/12.
 *
 */
public class LoadFooterView extends SwipeLoadMoreFooterLayout implements SwipeTrigger, SwipeLoadMoreTrigger {


    private ImageView iv_success;

    private ProgressBar pb;

    private TextView tv_text;



    public LoadFooterView(Context context) {

        this(context, null);
    }



    public LoadFooterView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }



    public LoadFooterView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

    }



    @Override
    protected void onFinishInflate() {

        super.onFinishInflate();
        iv_success = (ImageView) findViewById(R.id.iv_success);
        pb = (ProgressBar) findViewById(R.id.pb);
        tv_text = (TextView) findViewById(R.id.tv_refresh);

    }



    @Override
    public void onLoadMore() {
        iv_success.setVisibility(INVISIBLE);
        pb.setVisibility(VISIBLE);
        tv_text.setText("正在加载中");
    }



    @Override
    public void onPrepare() {

    }



    @Override
    public void onSwipe(int y, boolean isComplete) {

        if (!isComplete) {
            iv_success.setVisibility(INVISIBLE);
            pb.setVisibility(VISIBLE);
            if (y <= -getHeight()) {
                tv_text.setText("松开后加载");
            } else {
                tv_text.setText("上拉加载");
            }
        }
    }



    @Override
    public void onRelease() {

    }



    @Override
    public void complete() {
        pb.setVisibility(INVISIBLE);
        iv_success.setVisibility(VISIBLE);
        tv_text.setText("加载完成");
    }



    @Override
    public void onReset() {

    }
}
