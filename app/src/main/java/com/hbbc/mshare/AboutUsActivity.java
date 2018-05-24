package com.hbbc.mshare;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hbbc.R;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.CommonUtil;
import com.hbbc.util.Constants;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyTopbar;

/**
 * Created by Administrator on 2017/7/19.
 *
 */
public class AboutUsActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener {


    private TextView tv_wechat;

    private TextView tv_phone;

    private TextView tv_email;

    private TextView tv_website;

    private ImageView iv_logo;



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

        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        tv_wechat = (TextView) findViewById(R.id.tv_wechat);
        tv_phone = (TextView) findViewById(R.id.tv_phoneNumber);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_website = (TextView) findViewById(R.id.tv_website);

        getData();
    }



    private void getData() {

        LoginResultBean user = UserDao.getDaoInstance(this).query();
        String phoneNumber = null;
        if(user!=null){
            phoneNumber = user.getPhoneNumber();
        }else{
            return;
        }

        String[] params = {Constants.AppID,GlobalConfig.AppID,Constants.ECID,GlobalConfig.ECID,Constants.PhoneNumber,phoneNumber,Constants.AppType,GlobalConfig.AppType};
        new HttpUtil().callJson(handler,this, GlobalConfig.SERVERROOTPATH+GlobalConfig.SHARER_RETRIEVE_ABOUT_US_INFO,AboutUsBean.class,params);

    }



    @Override
    protected void getMessage(Message msg) {

        AboutUsBean aboutUsBean = (AboutUsBean) msg.obj;
        if(aboutUsBean != null && aboutUsBean.getResult()){
            Glide.with(this)
                    .load(aboutUsBean.getAPPLogoPicFileID())
                    .into(iv_logo);
            tv_wechat.setText(aboutUsBean.getWXNumber());
            tv_email.setText(aboutUsBean.getMailAddress());
            tv_phone.setText(aboutUsBean.getContactNumber());
            tv_website.setText(aboutUsBean.getOfficeAddress());
        }


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
