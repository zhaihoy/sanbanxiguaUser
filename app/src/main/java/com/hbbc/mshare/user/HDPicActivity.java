package com.hbbc.mshare.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.hbbc.R;
import com.hbbc.util.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Administrator on 2017/8/11.
 *
 */
public class HDPicActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, -1);//TODO: 查一下为什么必须是-1  ?
        setContentView(R.layout.mshare_high_definition_pic);
        Intent intent = getIntent();
        if (intent == null)
            return;
        int selectedPosition = intent.getIntExtra("selectedPosition", 0);
        List<String> picList = (ArrayList<String>) intent.getSerializableExtra("picList");
        HDPagerAdapter adapter=new HDPagerAdapter(picList);
        ViewPager vp = (ViewPager) findViewById(R.id.pager);
        vp.setAdapter(adapter);
        vp.setCurrentItem(selectedPosition);

    }



    class HDPagerAdapter extends PagerAdapter {

        private List<String> urls;

        public HDPagerAdapter(List<String> urls) {
            this.urls=urls;
        }


        @Override
        public int getCount() {

            return urls.size();
        }



        @Override
        public boolean isViewFromObject(View view, Object object) {

            return view == object;
        }



        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            PhotoView photoView = new PhotoView(container.getContext());
            Glide.with(photoView.getContext()).load(urls.get(position)).into(photoView);//先不加上失败与占位图
            container.addView(photoView,ViewPager.LayoutParams.MATCH_PARENT,ViewPager.LayoutParams.MATCH_PARENT);
            return photoView;

        }



        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);

        }
    }


}
