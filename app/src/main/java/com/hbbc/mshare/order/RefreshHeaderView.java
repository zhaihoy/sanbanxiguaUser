package com.hbbc.mshare.order;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreFooterLayout;
import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.hbbc.R;

/**
 * Created by Administrator on 2017/10/12.
 *
 */
public class RefreshHeaderView extends SwipeLoadMoreFooterLayout implements SwipeRefreshTrigger,SwipeTrigger{


    private ImageView iv_arrow;

    private ImageView iv_success;

    private ProgressBar pb;

    private TextView tv_text;

    private boolean isRotated;



    public RefreshHeaderView(Context context) {

        this(context, null);
    }



    public RefreshHeaderView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }



    public RefreshHeaderView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

    }



    @Override
    protected void onFinishInflate() {

        super.onFinishInflate();
        iv_arrow = (ImageView) findViewById(R.id.iv_arrow);
        iv_success = (ImageView) findViewById(R.id.iv_success);
        pb = (ProgressBar) findViewById(R.id.pb);
        tv_text = (TextView) findViewById(R.id.tv_refresh);
    }



    @Override
    public void onRefresh() {
        tv_text.setText("正在刷新");
        pb.setVisibility(VISIBLE);
        iv_arrow.setVisibility(INVISIBLE);
        iv_success.setVisibility(INVISIBLE);
    }


    //何时调用此接口
    @Override
    public void onPrepare() {

    }



    @Override
    public void onSwipe(int y, boolean isComplete) {
        if (!isComplete) {
            iv_arrow.setVisibility(VISIBLE);
            iv_success.setVisibility(INVISIBLE);
            pb.setVisibility(INVISIBLE);
            if (y >= getHeight()) {
                tv_text.setText("松开后刷新");
                if(!isRotated){
                    iv_arrow.setRotation(-180f);
                    isRotated = true;
                }
            } else {
                tv_text.setText("下拉刷新");
                if(isRotated){
                    iv_arrow.setRotation(0);
                    isRotated = false;
                }
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
        iv_arrow.setVisibility(INVISIBLE);
        tv_text.setText("刷新完成");
    }


    //何时调用
    @Override
    public void onReset() {

    }
}
