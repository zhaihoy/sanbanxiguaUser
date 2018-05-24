package com.hbbc.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 自定义上拉加载RecyclerView
 */
public class LoadMoreRecyclerView extends LinearLayout {


    private RecyclerView recyclerView;


    private LinearLayout linearview;


    private RecyclerView.LayoutManager l;


    private LoadMore loadMore;

    private int startY;

    private boolean isLoading;

    private int State;



    public LoadMoreRecyclerView(Context context) {

        this(context, null);
    }



    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {

        this(context, attrs, 0);
    }



    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
        init(context);
    }



    private void init(Context context) {

        recyclerView = new RecyclerView(context);
        LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        linearview = new LinearLayout(context);
        linearview.setGravity(Gravity.CENTER);
        linearview.setOrientation(LinearLayout.HORIZONTAL);
        linearview.setAlpha(0.5f);
        //进度条
        ProgressBar pb = new ProgressBar(context);
        LayoutParams pbParams = new LayoutParams(50, 50);


        //正在加载TV
        TextView tv = new TextView(linearview.getContext());
        LayoutParams tvParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tv.setText("正在加载...");


        LayoutParams linearParams = new LayoutParams(LayoutParams.MATCH_PARENT, 80);
        linearParams.setMargins(0, -80, 0, 0);

        linearview.addView(pb, pbParams);
        linearview.addView(tv, tvParams);
        addView(recyclerView, p);
        addView(linearview, linearParams);
        linearview.setVisibility(View.INVISIBLE);
        setOrientation(VERTICAL);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }



    public void setLayoutManager(RecyclerView.LayoutManager l) {

        this.l = l;
        recyclerView.setLayoutManager(l);

    }



    public void setItemAnimator(DefaultItemAnimator defaultItemAnimator) {

        recyclerView.setItemAnimator(defaultItemAnimator);
    }



    public void setAdapter(final RecyclerView.Adapter<?> adapter) {

        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                State = newState;
                isLoading = true;
                super.onScrollStateChanged(recyclerView, newState);
            }



            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);
            }
        });


        recyclerView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = (int) event.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (State == RecyclerView.SCROLL_STATE_DRAGGING && (startY - event.getY()) > (linearview.getMeasuredHeight() * 3) && (adapter.getItemCount() - 1) == getLastVisiblePosition() && isLoading) {
                            loadMore.onLoadMore();
                            linearview.setVisibility(View.VISIBLE);
                            isLoading = false;
                        }
                        return false;
                }
                return false;
            }
        });
    }



    /**
     * 加载完成调用
     */
    public void complete() {

        linearview.setVisibility(View.INVISIBLE);
    }



    /**
     * 获取最后显示的Item
     */
    public int getLastVisiblePosition() {

        View lastVisibleChild = recyclerView.getChildAt(recyclerView.getChildCount() - 1);

        return lastVisibleChild != null ? recyclerView.getChildAdapterPosition(lastVisibleChild) : -1;
    }



    public void setOnLoadMoreListener(LoadMore loadMore) {

        this.loadMore = loadMore;
    }



    public interface LoadMore {

        void onLoadMore();
    }
}
