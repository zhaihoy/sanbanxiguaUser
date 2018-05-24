package com.hbbc.mpush;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hbbc.R;
import com.hbbc.mpush.bean.ReadNoticeListBean;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.GlobalParameter;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyTopbar;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;


/**
 * 我的消息页面
 */
public class MessageListActivity extends AppCompatActivity implements MyTopbar.OnTopBarClickListener {

    private int themeColor;                              //主题色

    private MyTopbar mpush_topbar;                      //标题栏

    private TabLayout mpush_mmain_tab;                  //通知标题

    private ViewPager mpush_mmain_vp;                   //通知消息列表

    private TextView mpush_cusitem_tv;                  //tab文本

    private ImageView mpush_cusitem_img;                //tab图标


    /**
     * 网络请求回调
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            getMessage(msg);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mpush_main_msg);
        overridePendingTransition(R.anim.global_in, R.anim.global_out);
        //初始化控件
        initView();
    }



    /**
     * 初始化控件
     */
    protected void initView() {

        mpush_topbar = (MyTopbar) findViewById(R.id.mpush_topbar);
        mpush_mmain_tab = (TabLayout) findViewById(R.id.mpush_mmain_tab);
        mpush_mmain_vp = (ViewPager) findViewById(R.id.mpush_mmain_vp);
        mpush_mmain_tab.setTabMode(TabLayout.MODE_FIXED);
        //初始化主题色
        initTheme();
        mpush_topbar.setOnTopBarClickListener(this);
        mpush_mmain_tab.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mpush_mmain_vp));
        //获取未读消息提示接口
        new HttpUtil().callJson(handler, 0, this, GlobalConfig.MPUSH_GETUNREADNOTICELIST, ReadNoticeListBean.class, "AppUserID", GlobalConfig.AppUserID, "ECID", GlobalConfig.ECID + "", "PushID", GlobalConfig.PushID, "UserType", GlobalParameter.getPersonflag(), "UserID", GlobalParameter.getManagerid() + GlobalParameter.getMemberid());
    }



    /**
     * 初始化主题色
     */
    private void initTheme() {

        //主题色赋值
        themeColor = GlobalParameter.getThemeColor();

        mpush_topbar.setBackgroundColor(themeColor);
        mpush_topbar.setTitle(GlobalConfig.Push_ModuleName);
        //设置背景色
        mpush_mmain_tab.setBackgroundColor(themeColor);
    }

    /**
     * 消息回调机制
     */
    protected void getMessage(Message msg) {

        switch (msg.what) {
            case 0:
                ReadNoticeListBean readNoticeListBean = (ReadNoticeListBean) msg.obj;

                if (readNoticeListBean.isResult()) {
                    //获取未读消息数据
                    ArrayList<ReadNoticeListBean.UnReadNoticeList> readNoticeLists = (ArrayList<ReadNoticeListBean.UnReadNoticeList>) readNoticeListBean.getUnReadNoticeList();
                    //设置适配器
                    MessageListFragmentAdapter myBaseAdapter = new MessageListFragmentAdapter(getSupportFragmentManager(), readNoticeLists);
                    mpush_mmain_vp.setAdapter(myBaseAdapter);
                    mpush_mmain_vp.setOffscreenPageLimit(1);
                    mpush_mmain_tab.setupWithViewPager(mpush_mmain_vp);
                    if (readNoticeLists.size() >= 5) {
                        mpush_mmain_tab.setTabMode(TabLayout.MODE_SCROLLABLE);
                    }
                    //设置tab
                    for (int i = 0; i < readNoticeLists.size(); i++) {
                        View view = LayoutInflater.from(this).inflate(R.layout.mpush_tab_cusview, null, false);
                        mpush_cusitem_tv = (TextView) view.findViewById(R.id.mpush_cusitem_tv);
                        mpush_cusitem_img = (ImageView) view.findViewById(R.id.mpush_cusitem_img);
                        //设置数据显示
                        Logger.d(readNoticeLists.get(i).getTypeName() + "--------" + readNoticeListBean.getUnReadNoticeList().size());
                        mpush_cusitem_tv.setText(readNoticeLists.get(i).getTypeName());
                        Glide.with(this).load(readNoticeLists.get(i).getPicFileID()).into(mpush_cusitem_img);

                        TabLayout.Tab tab = mpush_mmain_tab.getTabAt(i);
                        tab.setCustomView(view);
                    }
                } else {
                    //错误提示
                    Toast.makeText(this, readNoticeListBean.getNotice(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }



    /**
     * 退出
     */
    @Override
    public void finish() {

        super.finish();
        overridePendingTransition(R.anim.global_in, R.anim.global_out);
    }



    @Override
    public void setOnLeftClick() {

        finish();//退出当前页面
    }



    @Override
    public void setOnRightClick() {

    }
}
