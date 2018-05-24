package com.hbbc.mpush;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hbbc.mpush.bean.ReadNoticeListBean;

import java.util.ArrayList;

/**
 * 页面适配器
 */
public class MessageListFragmentAdapter extends FragmentPagerAdapter {


    private ArrayList<ReadNoticeListBean.UnReadNoticeList> list;



    public MessageListFragmentAdapter(FragmentManager fm, ArrayList<ReadNoticeListBean.UnReadNoticeList> list) {
        super(fm);
        this.list = list;
    }



    /**
     * 获取当前fragment页面
     */
    @Override
    public Fragment getItem(int position) {
        return MessageListFragment.getInstanceFragment(position, list.get(position).getMTID());
    }



    /**
     * fragment页面个数
     */
    @Override
    public int getCount() {
        return list.size();
    }


}
