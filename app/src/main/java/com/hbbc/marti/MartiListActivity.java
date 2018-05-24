package com.hbbc.marti;

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
import com.hbbc.marti.bean.MartiParamBean;
import com.hbbc.marti.bean.MartiTypeListBean;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.GlobalParameter;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyTopbar;

import java.util.List;

/**
 * 文章页面
 */
public class MartiListActivity extends AppCompatActivity
        implements MyTopbar.OnTopBarClickListener, TabLayout.OnTabSelectedListener {

    private int themeColor = Color.WHITE;           //主题色，默认白色

    private MyTopbar marti_include;

    private TabLayout marti_tablayout;

    private ViewPager marti_vp;

    @SuppressWarnings("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            getMessage(msg);
        }
    };



    /**
     * 消息回调机制
     */
    private void getMessage(Message msg) {

        switch (msg.what) {
            case 0:
                MartiParamBean martiBean = (MartiParamBean) msg.obj;
                if (martiBean.isResult()) {
                    marti_include.setTitle(martiBean.getModuleName());
                    GlobalConfig.MartiID = martiBean.getArticleID() + "";
                    //加载文章类型接口
                    LoadArtiTypeList();
                } else {
                    Toast.makeText(MartiListActivity.this, martiBean.getNotice(), Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                MartiTypeListBean martiTypeBean = (MartiTypeListBean) msg.obj;
                if (martiTypeBean.isResult()) {
                    List<MartiTypeListBean.TypeListBean> martiTypeListBean = martiTypeBean.getTypeList();

                    if (martiTypeListBean != null) {
                        MartiListFragmentAdapter martAdapter = new MartiListFragmentAdapter(getSupportFragmentManager(), martiTypeListBean);
                        marti_vp.setAdapter(martAdapter);
                        marti_tablayout.setupWithViewPager(marti_vp);
                        //选项卡数量大于5时设置为滑动模式
                        if (martiTypeListBean.size() > 5) {
                            marti_tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                        }

                        for (int i = 0; i < martiTypeListBean.size(); i++) {
                            TabLayout.Tab tab = marti_tablayout.getTabAt(i);
                            tab.setText(martiTypeListBean.get(i).getArticleTypeName());
                        }
                    }
                } else {
                    Toast.makeText(MartiListActivity.this, martiTypeBean.getNotice(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }



    private void LoadArtiTypeList() {
        /**
         * 获得文章分类接口 getTypeList
         */
        new HttpUtil().callJson(handler, 1, this, GlobalConfig.MARTI_GETTYPELIST, MartiTypeListBean.class, "AppUserID", GlobalConfig.AppUserID, "ECID", GlobalConfig.ECID + "", "ArticleID", GlobalConfig.MartiID);
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.marti_main);
        initView();
    }



    /**
     * 初始化控件
     */
    private void initView() {

        marti_include = (MyTopbar) findViewById(R.id.marti_include);
        marti_tablayout = (TabLayout) findViewById(R.id.marti_tablayout);
        marti_vp = (ViewPager) findViewById(R.id.marti_vp);

        new HttpUtil().callJson(handler, 0, this, GlobalConfig.MARTI_GETPARAMTER, MartiParamBean.class, "AppUserID", GlobalConfig.AppUserID, "ECID", GlobalConfig.ECID + "");

        initTheme();


        marti_include.setOnTopBarClickListener(this);
        marti_tablayout.setOnTabSelectedListener(this);
    }


    private void initTheme() {

        themeColor = GlobalParameter.getThemeColor();

        marti_include.setBackgroundColor(themeColor);

        marti_tablayout.setTabTextColors(Color.BLACK, themeColor);

        marti_tablayout.setSelectedTabIndicatorColor(themeColor);
    }

    @Override
    public void setOnLeftClick() {
        finish();
    }

    @Override
    public void setOnRightClick() {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        marti_vp.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
