package com.hbbc.marti;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hbbc.R;
import com.hbbc.marti.bean.MartiListBeans;
import com.hbbc.util.BaseFragment;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.LoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章条目页面
 */
public class MartiListFragment extends BaseFragment implements MartiListRecyclerAdapter.ArtiItemClick, LoadMoreRecyclerView.LoadMore {

    private static final String PAGE = "page";

    private static final String ARTICLEID = "ArticleContentID";

    private String ArticleTypeID;                                   //文章ID

    private int page;                                               //当前页面

    private LoadMoreRecyclerView marti_frg_recview;               //列表

    private int pageNum;                                        //请求次数

    private MartiListRecyclerAdapter adapter;                     //适配器


    private List<MartiListBeans.ArticleListBean> martiListBeansList = null;



    public static Fragment getInstance(int page, String ArticleContentID) {

        MartiListFragment martiListFragment = new MartiListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PAGE, page);
        bundle.putString(ARTICLEID, ArticleContentID);
        martiListFragment.setArguments(bundle);
        return martiListFragment;
    }



    @Override
    protected void getMessage(Message msg) {

        MartiListBeans martiListBeans = (MartiListBeans) msg.obj;
        if (martiListBeans.isResult()) {
            List<MartiListBeans.ArticleListBean> artilist = martiListBeans.getArticleList();

            //因为隔页跳转时，会执行多次pageNum为零的请求
            if (pageNum == 0) {
                martiListBeansList.clear();
            }
            if (artilist != null) {
                Log.e("size", artilist.size() + "");
                martiListBeansList.addAll(artilist);
                adapter = new MartiListRecyclerAdapter(getContext(), martiListBeansList);
                marti_frg_recview.setAdapter(adapter);
                adapter.setOnArtiItemClick(this);
            }


        } else {
            Toast.makeText(MartiListFragment.this.getContext(), martiListBeans.getNotice(), Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        page = getFragmentBundle().getInt(PAGE);
        ArticleTypeID = getFragmentBundle().getString(ARTICLEID);
    }



    @Override
    protected void initControlView(View view) {

        marti_frg_recview = (LoadMoreRecyclerView) view.findViewById(R.id.marti_frg_recview);
        marti_frg_recview.setLayoutManager(new LinearLayoutManager(getContext()));
        marti_frg_recview.setItemAnimator(new DefaultItemAnimator());
        marti_frg_recview.setOnLoadMoreListener(this);
    }



    @Override
    protected int setContentView() {

        return R.layout.marti_main_frgitem;
    }



    @Override
    protected void stopData() {

    }



    @Override
    protected void LoadData() {

        pageNum = 0;
        martiListBeansList = new ArrayList<>();
        new HttpUtil().callJson(handler, getContext(), GlobalConfig.MARTI_GETARTILIST, MartiListBeans.class, "AppUserID", GlobalConfig.AppUserID, "ECID", GlobalConfig.ECID + "", "ArticleTypeID", ArticleTypeID, "pageNum", pageNum + "");
    }



    @Override
    public void setOnArtiItemClick(int position, String ArticleContentID) {

        Intent intent = new Intent();

        intent.setClass(getContext(), MartiInfoActivity.class);

        intent.putExtra("ArticleContentID", ArticleContentID);

        startActivity(intent);
    }



    @Override
    public void onLoadMore() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                new HttpUtil().callJson(handler, getContext(), GlobalConfig.MARTI_GETARTILIST, MartiListBeans.class, "AppUserID", GlobalConfig.AppUserID, "ECID", GlobalConfig.ECID + "", "ArticleTypeID", ArticleTypeID, "pageNum", ++pageNum + "");
                marti_frg_recview.complete();
                adapter.notifyDataSetChanged();
            }
        }, 2000);
    }


}
