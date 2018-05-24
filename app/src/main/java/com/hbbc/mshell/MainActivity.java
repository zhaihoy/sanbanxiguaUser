package com.hbbc.mshell;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hbbc.R;
import com.hbbc.marti.MartiListActivity;
import com.hbbc.mmain.InformActivity;
import com.hbbc.mmain.UnlistedActivity;
import com.hbbc.mnews.NewsListActivity;
import com.hbbc.mshell.bean.CarouselBean;
import com.hbbc.mshell.bean.ShellItemListBean;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.GlobalParameter;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyCircleImageView;
import com.hbbc.util.UIUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Main页面
 */
public class MainActivity extends BaseActivity implements MainRecyclerViewAdapter.MyRecyclerItemListener {

    private ImageView recy_bg;                  //模块背景图片

    private Banner banner;                      //轮播图

    private TextView tv_title;                  //模块标题

    private RecyclerView mRecyclerView;         //菜单列表控件

    private MyCircleImageView mshell_userPic;   //头像

    private RelativeLayout mRelativeLayout;     //TopBar布局，用于改变颜色

    private int ThemeColor = Color.WHITE;         //主题色

    private RelativeLayout mshell_linear;           //轮播图布局



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshell_main_main);
        GlobalConfig.APP_Context = this;

        // 页面控件初始化
        initView();
    }



    /**
     * 页面控件初始化
     */
    @Override
    protected void initView() {

        //主题色赋值
        ThemeColor = GlobalParameter.getThemeColor();

        //模块背景图
        recy_bg = (ImageView) findViewById(R.id.recy_bg);
        Glide.with(this).load(GlobalConfig.BackgroundPicFileID).into(recy_bg);

        //模块标题
        tv_title = (TextView) findViewById(R.id.shell_titlebar_tv_Title);
        tv_title.setText(GlobalConfig.Global_ModuleName);
        //头像
        mshell_userPic = (MyCircleImageView) findViewById(R.id.mshell_userPic);
        mshell_userPic.setOnClickListener(this);
        mshell_linear = (RelativeLayout) findViewById(R.id.mshell_linear);
        //设置头像
        if (GlobalParameter.contains("HeadPicFieldID")) {
            Glide.with(this).load(GlobalParameter.getHeadpicfieldid()).error(R.drawable.mmain_unlisted_upic).into(mshell_userPic);
        }

        mRelativeLayout = (RelativeLayout) findViewById(R.id.mshell_topbar);

        mRelativeLayout.setBackgroundColor(ThemeColor);
        //轮播图
        banner = (Banner) findViewById(R.id.mshell_banner1);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                //设置Glide加载，也可以设置其他第三方加载图片加载库
                Glide.with(context).load(path).error(R.drawable.global_img_error).into(imageView);
            }
        });
        //设置3000毫秒
        banner.setDelayTime(3000);
        //设置动画效果
        banner.setBannerAnimation(Transformer.Default);
        //请求轮播图数据
        new HttpUtil().callJson(handler, 1, this, GlobalConfig.MSHELL_GETCAROUSELLIST, CarouselBean.class, "AppUserID", GlobalConfig.AppUserID, "ECID", GlobalConfig.ECID + "", "ShellID", GlobalConfig.Global_ShellID);

        //菜单列表控件
        mRecyclerView = (RecyclerView) findViewById(R.id.mshell_rcv);
        new HttpUtil().callJson(handler, 2, this, GlobalConfig.MSHELL_GETSHELLITEMLIST, ShellItemListBean.class, "AppUserID", GlobalConfig.AppUserID, "ECID", GlobalConfig.ECID + "", "ShellID", GlobalConfig.Global_ShellID);
    }



    /**
     * 消息回调机制
     */
    @Override
    protected void getMessage(Message msg) {


        switch (msg.what) {

            case 1:
                CarouselBean carouselBean = (CarouselBean) msg.obj;
                //获取轮播图数据集合
                List<CarouselBean.CarouselList> carouselList = carouselBean.getCarouselList();
                if (carouselList != null) {
                    List<String> mCarouselTitel = new ArrayList<>();
                    List<String> mCarouselImgFileID = new ArrayList<>();
                    //遍历加入Banner控件中显示轮播图
                    for (int i = 0; i < carouselList.size(); i++) {
                        //轮播图标题
                        mCarouselTitel.add(carouselList.get(i).getTitle());
                        //图片地址
                        mCarouselImgFileID.add(carouselList.get(i).getImgFileID());
                    }
                    //设置轮播图标题
                    banner.setBannerTitles(mCarouselTitel);
                    //设置轮播图的地址
                    banner.setImages(mCarouselImgFileID);
                    //进行banner渲染
                    banner.start();
                } else {
                    mshell_linear.setVisibility(View.GONE);
                }
                break;

            case 2:
                ShellItemListBean shellItemListBean = (ShellItemListBean) msg.obj;
                if (shellItemListBean.isResult()) {
                    //设置RecyclerView的显示布局类型
                    mRecyclerView.setLayoutManager(new GridLayoutManager(this, Integer.valueOf(GlobalConfig.Global_MenuType) + 1));
                    //设置adapter
                    MainRecyclerViewAdapter myRecyclerAdapter = new MainRecyclerViewAdapter(this, shellItemListBean.getShellItemList());
                    //监听事件
                    myRecyclerAdapter.setOnMyRecyclerItemListener(this);
                    //添加适配器
                    mRecyclerView.setAdapter(myRecyclerAdapter);
                    //设置Item增加删除动画
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                }

                break;
        }
    }



    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.mshell_userPic:

                if (GlobalParameter.getString("Name").equals("")) {
                    //跳转未登录页面
                    startActivity(new Intent(this, UnlistedActivity.class));
                } else {
                    //跳转个人信息页面
                    startActivity(new Intent(this, InformActivity.class));
                }
                mshell_userPic.setEnabled(false);//不能让用户再次点击
                break;
        }
    }



    /**
     * RecyclerView的Item监听
     */
    @Override
    public void setOnMyRecyclerItemListener(View view, int position, String url) {

        if (url.contains("news")) {
            startActivity(new Intent(this, NewsListActivity.class));
        } else if (url.contains("article")) {
            startActivity(new Intent(this, MartiListActivity.class));
        }

    }



    /**
     * 若主页按返回键，询问用户是否退出程序，若是确认，则退出。否则，继续留在本页面。
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 退出APP
            UIUtil.exitApp(this, true);
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    protected void onResume() {


        super.onResume();
        //开启轮播
        banner.startAutoPlay();
        mshell_userPic.setEnabled(true);//可以点击

        //刷新头像
        if (GlobalParameter.contains("HeadPicFieldID")) {
            Glide.with(this).load(GlobalParameter.getHeadpicfieldid()).error(R.drawable.mmain_unlisted_upic).into(mshell_userPic);
        }

    }



    @Override
    protected void onPause() {


        super.onPause();
        //停止轮播图轮播
        banner.stopAutoPlay();
    }
}
