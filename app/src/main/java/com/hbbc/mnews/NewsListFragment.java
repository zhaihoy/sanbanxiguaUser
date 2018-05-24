package com.hbbc.mnews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hbbc.R;
import com.hbbc.mnews.bean.NewsListBeans;
import com.hbbc.util.BaseFragment;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.LoadMoreRecyclerView;
import com.orhanobut.logger.Logger;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻列表
 */
public class NewsListFragment extends BaseFragment implements NewsListRecyclerAdapter.ItemClick, LoadMoreRecyclerView.LoadMore {

    public static final String PAGE = "page";

    public static final String NEWSTYPEID = "NewsTypeID";

    private int page;                                //当前页面

    private String NewsTypeID;                      //新闻类型ID

    private Banner mnews_frg_banner;                //轮播图

    private LoadMoreRecyclerView mnews_frg_recview;         //新闻条目列表

    private NewsListRecyclerAdapter adapter;     //适配器

    private List<NewsListBeans.NewsListBean> newsListBeen = null;  //新闻数据

    private int pageNum;                        //请求次数



    /**
     * 获取NewsTypeID对应的列表页面
     */
    public static Fragment getInstance(int p, String NewsTypeId) {

        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PAGE, p);
        bundle.putString(NEWSTYPEID, NewsTypeId);
        fragment.setArguments(bundle);
        return fragment;
    }



    /**
     * 消息回调
     */
    @Override
    protected void getMessage(Message msg) {

        NewsListBeans newsListBean = (NewsListBeans) msg.obj;

        if (newsListBean.isResult()) {
            //获取轮播图数据
            final List<NewsListBeans.BannerNewsListBean> listBannerBeen = newsListBean.getBannerNewsList();

            //当有轮播图时执行
            if (listBannerBeen != null) {
                ArrayList<String> bannerTitle = new ArrayList<>();
                ArrayList<String> imgUrl = new ArrayList<>();
                for (int i = 0; i < listBannerBeen.size(); i++) {
                    bannerTitle.add(listBannerBeen.get(i).getHeadline());
                    imgUrl.add(listBannerBeen.get(i).getPicFileID());
                }
                //开启轮播图
                //设置banner样式
                mnews_frg_banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
                //设置图片加载器
                mnews_frg_banner.setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        //设置Glide加载，也可以设置其他第三方加载图片加载库
                        Glide.with(context).load(path).error(R.drawable.global_img_error).into(imageView);
                    }
                });
                //设置3000毫秒
                mnews_frg_banner.setDelayTime(3000);
                //设置动画效果
                mnews_frg_banner.setBannerAnimation(Transformer.Default);
                mnews_frg_banner.setBannerTitles(bannerTitle);
                mnews_frg_banner.setImages(imgUrl);
                mnews_frg_banner.startAutoPlay();
                mnews_frg_banner.start();
                //轮播图监听
                mnews_frg_banner.setOnBannerClickListener(new OnBannerClickListener() {
                    @Override
                    public void OnBannerClick(int position) {

                        StartToActivity(listBannerBeen.get(position - 1).getNewsContentID() + "");
                    }
                });
            } else {
                mnews_frg_banner.setVisibility(View.GONE);
            }

            // 因为隔页跳转时，会执行多次pageNum为零的请求
            if (pageNum == 0) {
                newsListBeen.clear();
            }

            List<NewsListBeans.NewsListBean> listBeen = newsListBean.getNewsList();

            //当条目不为空时执行
            if (listBeen != null) {
                newsListBeen.addAll(listBeen);
                //设置适配器
                adapter = new NewsListRecyclerAdapter(getContext(), newsListBeen);
                mnews_frg_recview.setLayoutManager(new LinearLayoutManager(getContext()));
                //设置Item增加删除动画
                mnews_frg_recview.setItemAnimator(new DefaultItemAnimator());
                mnews_frg_recview.setAdapter(adapter);
                //设置适配器条目的监听事件
                adapter.setOnItemClick(this);
            }
        } else {
            Toast.makeText(NewsListFragment.this.getContext(), "" + newsListBean.getNotice(), Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //获取当前Fragment的参数数据
        page = getFragmentBundle().getInt(PAGE);
        NewsTypeID = getFragmentBundle().getString(NEWSTYPEID);
    }



    @Override
    protected void initControlView(View view) {

        mnews_frg_banner = (Banner) view.findViewById(R.id.mnews_frg_banner);
        mnews_frg_recview = (LoadMoreRecyclerView) view.findViewById(R.id.mnews_frg_recview);
        mnews_frg_recview.setLayoutManager(new LinearLayoutManager(getContext()));
        mnews_frg_recview.setOnLoadMoreListener(this);
    }



    @Override
    protected int setContentView() {

        return R.layout.mnews_main_frgitem;
    }



    @Override
    protected void stopData() {

        Logger.d("stopData");
        if (mnews_frg_banner != null) {
            mnews_frg_banner.stopAutoPlay();
        }

    }



    @Override
    protected void LoadData() {

        Logger.d("LoadData");
        newsListBeen = new ArrayList<>();
        //获得新闻列表接口 MNews.getNewsList
        pageNum = 0;
        new HttpUtil().callJson(handler, getContext(), GlobalConfig.MNEWS_GETNEWSLIST, NewsListBeans.class, "AppUserID", GlobalConfig.AppUserID, "ECID", GlobalConfig.ECID + "", "NewsTypeID", NewsTypeID, "NewsID", GlobalConfig.NewsID, "pageNum", pageNum + "");
    }



    @Override
    public void OnItemClick(int position, String newsContentID) {

        StartToActivity(newsContentID);
    }



    /**
     * 跳转新闻详情页面
     *
     * @param newsContentID 新闻ID
     */
    public void StartToActivity(String newsContentID) {

        Intent intent = new Intent();
        intent.putExtra("NewsContentID", newsContentID);
        intent.setClass(getContext(), NewsInfoActivity.class);
        startActivity(intent);
    }



    @Override
    public void onLoadMore() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                new HttpUtil().callJson(handler, getContext(), GlobalConfig.MNEWS_GETNEWSLIST, NewsListBeans.class, "AppUserID", GlobalConfig.AppUserID, "ECID", GlobalConfig.ECID + "", "NewsTypeID", NewsTypeID, "NewsID", GlobalConfig.NewsID, "pageNum", ++pageNum + "");
                mnews_frg_recview.complete();
                adapter.notifyDataSetChanged();
            }
        }, 2000);
    }


}
