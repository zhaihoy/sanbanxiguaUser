package com.asijack.tabhostdemo;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

/*
 * 
 * 一、谷歌官方认为，ViewPager应该和Fragment一起使用时，此时ViewPager的适配器是FragmentPagerAdapter
 *    当你实现一个FragmentPagerAdapter,你必须至少覆盖以下方法:
	getCount()
	getItem()
        二、 如果ViewPager没有和Fragment一起，ViewPager的适配器是PagerAdapter，
               它是基类提供适配器来填充页面ViewPager内部，当你实现一个PagerAdapter,你必须至少覆盖以下方法:
    instantiateItem(ViewGroup, int)
    destroyItem(ViewGroup, int, Object)
    getCount()
    isViewFromObject(View, Object)


 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
	ArrayList<Fragment> list;
	public MyFragmentPagerAdapter(FragmentManager fm,ArrayList<Fragment> list) {
		super(fm);
		this.list=list;
	}
	
	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list.size();
	}
	
}
