package com.hbbc.mnews;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.hbbc.R;
import com.hbbc.mnews.bean.NewsTypeListBean;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.GlobalParameter;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyTopbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 新闻页面
 */
public class NewsListActivity extends AppCompatActivity implements MyTopbar.OnTopBarClickListener, TabLayout.OnTabSelectedListener {

    //标题栏布局
    private MyTopbar mnews_include;

    //新闻内容
    private ViewPager mnews_vp;

    //新闻标题
    private TabLayout mnews_tablayout;

    @SuppressWarnings("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            getMessage(msg);
        }
    };

    //主题色
    private int themeColor = Color.WHITE;



    /**
     * 消息回调机制
     */
    private void getMessage(Message msg) {

        switch (msg.what) {

            case 0:
                try {
                    JSONObject jsonObject = new JSONObject((String) msg.obj);
                    String notice = jsonObject.getString("Notice");
                    if (jsonObject.getBoolean("Result")) {
                        if (jsonObject.has("NewsID")) {
                            //设置新闻模块ID
                            GlobalConfig.NewsID = jsonObject.getString("NewsID");
                            //加载新闻分类接口
                            LoadNewsGetTypeList();
                        }

                        //设置标题名
                        if (jsonObject.has("ModuleName")) {
                            GlobalConfig.News_ModuleName = jsonObject.getString("ModuleName");
                            mnews_include.setTitle(GlobalConfig.News_ModuleName);
                        }
                    } else {
                        Toast.makeText(this, notice, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case 1:
                NewsTypeListBean newsTypeList = (NewsTypeListBean) msg.obj;
                if (newsTypeList.isResult()) {
                    List<NewsTypeListBean.ListTypeBean> TypeBeanList = newsTypeList.getListType();
                    NewsListFragmentAdapter newListFragmentAdapter = new NewsListFragmentAdapter(getSupportFragmentManager(), TypeBeanList);
                    //设置适配器
                    mnews_vp.setAdapter(newListFragmentAdapter);
                    mnews_tablayout.setupWithViewPager(mnews_vp);
                    //超过4个设置为可滑动的tab
                    if (TypeBeanList.size() > 5) {
                        mnews_tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                    }

                    for (int i = 0; i < TypeBeanList.size(); i++) {
                        TabLayout.Tab tab = mnews_tablayout.getTabAt(i);
                        //各个tab标题
                        tab.setText(TypeBeanList.get(i).getNewsTypeName());
                    }
                } else {
                    Toast.makeText(this, newsTypeList.getNotice(), Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }



    /**
     * 新闻分类接口 MNews.getTypeList
     */
    private void LoadNewsGetTypeList() {

        new HttpUtil().callJson(handler, 1, this, GlobalConfig.MNEWS_GETTYPELIST, NewsTypeListBean.class, "AppUserID", GlobalConfig.AppUserID, "ECID", GlobalConfig.ECID + "", "NewsID", GlobalConfig.NewsID);
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mnews_main);
        initView();
    }



    /**
     * 初始化控件
     */
    private void initView() {

        mnews_include = (MyTopbar) findViewById(R.id.mnews_include);
        mnews_vp = (ViewPager) findViewById(R.id.mnews_vp);
        mnews_tablayout = (TabLayout) findViewById(R.id.mnews_tablayout);
        mnews_tablayout.setOnTabSelectedListener(this);
        initTheme();

        //新闻模块详情接口
        new HttpUtil().callJson(handler, 0, this, GlobalConfig.MNEWS_GETMNEWSPARAMETER, "AppUserID", GlobalConfig.AppUserID, "ECID", GlobalConfig.ECID + "");
    }



    /**
     * 初始化主题色
     */
    private void initTheme() {

        themeColor = GlobalParameter.getThemeColor();
        mnews_include.setBackgroundColor(themeColor);
        mnews_include.setOnTopBarClickListener(this);

        //设置选中后的颜色
        mnews_tablayout.setSelectedTabIndicatorColor(themeColor);
        mnews_tablayout.setSelectedTabIndicatorHeight(3);
        mnews_tablayout.setTabTextColors(Color.BLACK, themeColor);
    }



    @Override
    public void setOnLeftClick() {

        finish();//退出当前页面
    }



    @Override
    public void setOnRightClick() {

    }



    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        mnews_vp.setCurrentItem(tab.getPosition());
    }



    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }



    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


}
