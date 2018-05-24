package com.hbbc.mmain;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hbbc.R;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.CommonUtil;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.GlobalParameter;
import com.hbbc.util.MyCircleImageView;
import com.hbbc.util.MyTopbar;

/**
 * 关于我们页面
 */
public class AboutUsActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener {

    private MyTopbar aboutus_layout;                                          //标题栏

    private MyCircleImageView aboutus_logo;                                 //logo

    private TextView aboutus_version;                                        //版本号

    private TextView aboutus_service, about_technicalsupport;                 //服务条款,技术支持

    private int themeColor = Color.WHITE;                                    //主题色



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mmain_aboutus_main);
        initView();
    }



    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        aboutus_layout = (MyTopbar) findViewById(R.id.aboutus_layout);
        aboutus_logo = (MyCircleImageView) findViewById(R.id.aboutus_logo);
        aboutus_version = (TextView) findViewById(R.id.aboutus_version);
        aboutus_service = (TextView) findViewById(R.id.aboutus_service);
        about_technicalsupport = (TextView) findViewById(R.id.about_technicalsupport);

        //初始化主题色
        initthemeColor();

        //监听事件
        aboutus_layout.setOnTopBarClickListener(this);
    }



    /**
     * 主题色
     */
    private void initthemeColor() {

        //主题色赋值
        themeColor = GlobalParameter.getThemeColor();
        //设置topbar主题色
        aboutus_layout.setBackgroundColor(themeColor);

        //标题
        aboutus_layout.setTitle("关于我们");

        //LOGO
        Glide.with(this).load(GlobalConfig.ThemePicFileID).into(aboutus_logo);

        //服务条款下划线
        SpannableString s = new SpannableString("服务条款");
        s.setSpan(new UnderlineSpan(), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //跳转到服务条款页面
                startActivity(new Intent(AboutUsActivity.this, AboutTermActivity.class));
            }
        }, 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(themeColor), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        aboutus_service.setText(s);
        aboutus_service.setMovementMethod(LinkMovementMethod.getInstance());

        //版本号
        aboutus_version.setText(String.valueOf("版本号:" + CommonUtil.getVerName(this) + "." + CommonUtil.getVerCode(this)));

        //技术支持
        about_technicalsupport.setText(GlobalConfig.TechnicalSupport);
    }



    @Override
    public void setOnLeftClick() {
        finish();//退出当前页面
    }



    @Override
    public void setOnRightClick() {

    }
}
