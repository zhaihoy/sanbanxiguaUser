package com.hbbc.mshare;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbbc.R;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.CommonUtil;
import com.hbbc.util.MyTopbar;

/**
 * Created by Administrator on 2017/7/19.
 *
 */
public class AboutUsActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_about_us);
        initView();

    }

    @Override
    protected void initView() {
        MyTopbar topbar= (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);

        TextView tv_version_name= (TextView) findViewById(R.id.tv_version_name);
        LinearLayout ll_weixin= (LinearLayout) findViewById(R.id.ll_weixin);
        LinearLayout ll_phone= (LinearLayout) findViewById(R.id.ll_phone_number);
        LinearLayout ll_email= (LinearLayout) findViewById(R.id.ll_email);
        LinearLayout ll_website= (LinearLayout) findViewById(R.id.ll_official_website);

        ll_weixin.setOnClickListener(this);
        ll_phone.setOnClickListener(this);
        ll_email.setOnClickListener(this);
        ll_website.setOnClickListener(this);

        String verName = "v "+CommonUtil.getVerName(this);
        tv_version_name.setText(verName);

    }



    @Override
    public void setOnLeftClick() {
        finish();
    }



    @Override
    public void setOnRightClick() {

    }

    @Override
    public void onClick(View v) {

        super.onClick(v);
        switch (v.getId()){
            case R.id.ll_weixin:

                break;
            case R.id.ll_phone_number:

                break;
            case R.id.ll_email:

                break;
            case R.id.ll_official_website:

                break;
        }
    }
}
