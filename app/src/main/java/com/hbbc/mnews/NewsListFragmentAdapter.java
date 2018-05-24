package com.hbbc.mnews;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hbbc.mnews.bean.NewsTypeListBean;

import java.util.List;

/**
 * 页面适配器
 */
public class NewsListFragmentAdapter extends FragmentPagerAdapter {

    private List<NewsTypeListBean.ListTypeBean> list;



    public NewsListFragmentAdapter(FragmentManager fm, List<NewsTypeListBean.ListTypeBean> listTypeBeanList) {

        super(fm);
        this.list = listTypeBeanList;
    }



    @Override
    public Fragment getItem(int position) {

        return NewsListFragment.getInstance(position, list.get(position).getNewsTypeID());
    }



    @Override
    public int getCount() {

        return list.size();
    }


}
