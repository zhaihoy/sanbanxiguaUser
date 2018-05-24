package com.hbbc.mshare.sharer.Bill;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hbbc.util.GlobalConfig;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/15.
 * 用于显示用户账单的适配器
 */

public class BillAdapter extends FragmentPagerAdapter {

    private static final String[] TITLES_FOR_BILL_USER = {"订单支付", "充值明细"};

    private ArrayList<BillFragment> fragments = new ArrayList<>();



    public BillAdapter(FragmentManager fm) {

        super(fm);
        initFragment();
    }



    @Override
    public Fragment getItem(int position) {

        return fragments.get(position);
    }



    private void initFragment() {

        Bundle args;
        if (Integer.valueOf(GlobalConfig.AppType) == 2) {//用户端
            args = new Bundle();
            args.putInt("Fragment_Type", 0);
            BillFragment frag_all = new BillFragment();
            frag_all.setArguments(args);

            args = new Bundle();
            args.putInt("Fragment_Type", 1);
            BillFragment frag_detail = new BillFragment();
            frag_detail.setArguments(args);


            fragments.add(frag_all);
            fragments.add(frag_detail);

        }
    }



    @Override
    public int getCount() {

        //客户端类型: 1.共享端(车主端) 2.用户端
        return TITLES_FOR_BILL_USER.length;
    }



    @Override
    public CharSequence getPageTitle(int position) {

        return TITLES_FOR_BILL_USER[position];
    }
}








