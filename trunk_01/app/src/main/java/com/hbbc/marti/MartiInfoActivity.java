package com.hbbc.marti;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hbbc.R;
import com.hbbc.marti.bean.MartiInfoBean;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.GlobalParameter;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyTopbar;

/**
 * 文章详情页面
 */
public class MartiInfoActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener {

    private MyTopbar marti_info_include;        //标题栏布局

    private TextView marti_info_title;          //文章标题

    private TextView marti_info_time;           //文章发布时间

    private ImageView marti_info_img;           //文章图片

    private TextView marti_info_content;        //文章内容

    private int themeColor = Color.WHITE;        //主题色

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.marti_artiinfo_main);
        String ArticleContentID = getIntent().getStringExtra("ArticleContentID");
        initView();

        new HttpUtil().callJson(handler, this, GlobalConfig.MARTI_GETARTIINFO,
                MartiInfoBean.class, "AppUserID", GlobalConfig.AppUserID, "ECID",
                GlobalConfig.ECID + "", "ArticleContentID", ArticleContentID);

    }

    @Override
    protected void initView() {
        marti_info_include = (MyTopbar) findViewById(R.id.marti_info_include);
        marti_info_title = (TextView) findViewById(R.id.marti_info_title);
        marti_info_time = (TextView) findViewById(R.id.marti_info_time);
        marti_info_img = (ImageView) findViewById(R.id.marti_info_img);
        marti_info_content = (TextView) findViewById(R.id.marti_info_content);

        initTheme();

        marti_info_include.setOnTopBarClickListener(this);
    }

    private void initTheme() {

        themeColor = GlobalParameter.getThemeColor();
        marti_info_include.setBackgroundColor(themeColor);

        marti_info_include.setTitle("文章详情");
    }

    @Override
    protected void getMessage(Message msg) {

        MartiInfoBean martiInfoBean = (MartiInfoBean) msg.obj;

        if (martiInfoBean.isResult()) {
            marti_info_title.setText(martiInfoBean.getHeadline());
            if (!martiInfoBean.getPublishTime().equals("")) {
                String date = martiInfoBean.getPublishTime().substring(0, 16);
                marti_info_time.setText(date);
            }
            String content = "        " + martiInfoBean.getContent();
            marti_info_content.setText(content);

            Glide.with(this).load(martiInfoBean.getContentPicFileID()).into(marti_info_img);
        } else {
            Toast.makeText(MartiInfoActivity.this, martiInfoBean.getNotice(), Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onStop() {

        super.onStop();
        Glide.with(this).pauseRequests();
    }



    @Override
    public void setOnLeftClick() {
        finish();
    }



    @Override
    public void setOnRightClick() {

    }
}
