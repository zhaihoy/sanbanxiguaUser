package com.hbbc.mshare.order;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hbbc.util.GlobalConfig;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/12.
 *
 */
public class OrderAdapter extends FragmentPagerAdapter {

    private static final String[] TITLES_FOR_USER = {"全部订单",/*"使用中订单",*/"未完成订单","已完成订单"};

    private static final String[] TITLES_FOR_SHARER = {"全部订单","待付款订单","已完成订单"};

    private ArrayList<OrderFrament> fragments = new ArrayList<>();


    public OrderAdapter(FragmentManager fm) {

        super(fm);
        initFrament();
    }

    private void initFrament() {

        Bundle args;
        if(Integer.valueOf(GlobalConfig.AppType) == 2){//用户端
            args = new Bundle();
            args.putInt("Fragment_Type",0);
            OrderFrament frag_all = new OrderFrament();
            frag_all.setArguments(args);

          /*  args = new Bundle();
            args.putInt("Fragment_Type",1);
            OrderFrament frag_unPayed = new OrderFrament();
            frag_unPayed.setArguments(args);*/

            args = new Bundle();
            args.putInt("Fragment_Type",2);
            OrderFrament frag_failed = new OrderFrament();
            frag_failed.setArguments(args);

            args = new Bundle();
            args.putInt("Fragment_Type",3);
            OrderFrament frag_finished = new OrderFrament();
            frag_finished.setArguments(args);

            fragments.add(frag_all);
         /*fragments.add(frag_unPayed);*/
            fragments.add(frag_failed);
            fragments.add(frag_finished);

        }else{
            args = new Bundle();
            args.putInt("Fragment_Type",0);
            OrderFrament frag_all = new OrderFrament();
            frag_all.setArguments(args);

            args = new Bundle();
            args.putInt("Fragment_Type",1);
            OrderFrament frag_unPayed = new OrderFrament();
            frag_unPayed.setArguments(args);



            fragments.add(frag_all);
            fragments.add(frag_unPayed);

        }

    }



    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }



    @Override
    public int getCount() {
        //客户端类型: 1.共享端(车主端) 2.用户端
        return GlobalConfig.AppType.equals("1") ? TITLES_FOR_SHARER.length : TITLES_FOR_USER.length;
    }



    @Override
    public CharSequence getPageTitle(int position) {

        return GlobalConfig.AppType.equals("1") ? TITLES_FOR_SHARER[position] : TITLES_FOR_USER[position];
    }
}
