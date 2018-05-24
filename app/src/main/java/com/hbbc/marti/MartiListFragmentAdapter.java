package com.hbbc.marti;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hbbc.marti.bean.MartiTypeListBean;

import java.util.List;

/**
 * 文章页面适配器
 */
public class MartiListFragmentAdapter extends FragmentPagerAdapter {

    private List<MartiTypeListBean.TypeListBean> list;



    public MartiListFragmentAdapter(FragmentManager fm, List<MartiTypeListBean.TypeListBean> list) {

        super(fm);
        this.list = list;
    }



    @Override
    public Fragment getItem(int position) {

        return MartiListFragment.getInstance(position, list.get(position).getArticleTypeID());
    }



    @Override
    public int getCount() {

        return list.size();
    }


}
