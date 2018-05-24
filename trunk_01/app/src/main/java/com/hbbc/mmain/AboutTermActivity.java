package com.hbbc.mmain;

import android.graphics.Color;
import android.os.Bundle;

import com.hbbc.R;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.GlobalParameter;
import com.hbbc.util.MyTopbar;

/**
 * 服务条款页面
 */
public class AboutTermActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener {

    private MyTopbar aboutterm_layout;          //标题栏

    private int themeColor = Color.WHITE;        //主题色



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mmain_aboutterm_main);
        initView();
    }



    @Override
    protected void initView() {

        aboutterm_layout = (MyTopbar) findViewById(R.id.aboutterm_layout);
        //初始化主题色
        initthemeColor();

        //设置监听事件
        aboutterm_layout.setOnTopBarClickListener(this);
    }



    /**
     * 初始化主题色
     */
    private void initthemeColor() {

        themeColor = GlobalParameter.getThemeColor();
        //设置topbar主题色
        aboutterm_layout.setBackgroundColor(themeColor);
        //标题
        aboutterm_layout.setTitle("服务条款");
    }



    @Override
    public void setOnLeftClick() {
        finish();//退出当前页面
    }



    @Override
    public void setOnRightClick() {

    }
}
