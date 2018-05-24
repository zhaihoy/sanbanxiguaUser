package com.hbbc.mnews;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbbc.R;
import com.hbbc.mnews.bean.NewsInfoBean;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.GlobalParameter;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyTopbar;

/**
 * 新闻详情页面
 */
public class NewsInfoActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener {

    private MyTopbar mnews_info_include;//标题栏布局

    private TextView mnews_info_title, mnews_info_time, mnews_info_content;  //新闻标题,新闻发布时间,新闻内容

    private Button mnews_info_hotspot;              //热点

    private ImageView mnews_info_img;               //新闻图片

    private int themeColor = Color.WHITE;           //主题色，默认白色



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mnews_newsinfo_main);
        themeColor = GlobalParameter.getThemeColor();
        String NewsContentID = getIntent().getStringExtra("NewsContentID");
        initView();

        // 获得新闻详情接口 MNews.getNewsInfo
        new HttpUtil().callJson(handler, 0, this, GlobalConfig.MNEW_GETNEWSINFO, NewsInfoBean.class, "NewsID", GlobalConfig.NewsID, "NewsContentID", NewsContentID, "ECID", GlobalConfig.ECID + "", "AppUserID", GlobalConfig.AppUserID);
    }



    @Override
    protected void initView() {

        mnews_info_include = (MyTopbar) findViewById(R.id.mnews_info_include);
        mnews_info_title = (TextView) findViewById(R.id.mnews_info_title);
        mnews_info_time = (TextView) findViewById(R.id.mnews_info_time);
        mnews_info_content = (TextView) findViewById(R.id.mnews_info_content);
        mnews_info_hotspot = (Button) findViewById(R.id.mnews_info_hotspot);
        mnews_info_img = (ImageView) findViewById(R.id.mnews_info_img);

        //标题栏颜色
        mnews_info_include.setBackgroundColor(themeColor);
        mnews_info_include.setOnTopBarClickListener(this);
        mnews_info_include.setTitle("新闻详情");
    }



    @Override
    protected void getMessage(Message msg) {

        NewsInfoBean infoBean = (NewsInfoBean) msg.obj;
        if (infoBean.isResult()) {
            mnews_info_title.setText(infoBean.getHeadline());
            String content = "     " + infoBean.getContent();
            mnews_info_content.setText(content);

            if (!infoBean.getPublishTime().equals("")) {
                String date = infoBean.getPublishTime().substring(0, 16);
                mnews_info_time.setText(date);
            }

            Glide.with(this).load(infoBean.getContentPicFileID()).diskCacheStrategy(DiskCacheStrategy.NONE).into(mnews_info_img);
        } else {
            outputShort(infoBean.getNotice());
        }

    }



    @Override
    protected void onStop() {

        super.onStop();

        Glide.with(this).onStop();
    }



    @Override
    public void setOnLeftClick() {

        finish();
    }



    @Override
    public void setOnRightClick() {

    }
}
