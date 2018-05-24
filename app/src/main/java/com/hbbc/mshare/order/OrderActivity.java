package com.hbbc.mshare.order;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.hbbc.R;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.mshare.sharer.OrderResultBean;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.Constants;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyTopbar;
import com.hbbc.util.ToastUtil;

/**
 * Created by Administrator on 2017/8/25.
 *
 */
public class OrderActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharer_order);
        initView();


    }


    @Override
    protected void initView() {

        MyTopbar topbar = (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);

        TabLayout tabs = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager vp = (ViewPager) findViewById(R.id.vp);
        OrderAdapter adapter = new OrderAdapter(getSupportFragmentManager());

        vp.setAdapter(adapter);
        tabs.setupWithViewPager(vp);//必须在VP添加适配器后,再关联

    }



    @Override
    protected void getMessage(Message msg) {

    }



    @Override
    public void setOnLeftClick() {
        finish();
    }



    @Override
    public void setOnRightClick() {

    }



    /**
     * 请求当前用户的所有订单
     */
    public void requestOrderInfo() {

        UserDao userDao = UserDao.getDaoInstance(this);
        LoginResultBean currentUser = userDao.query();
        if (currentUser == null) {
            ToastUtil.toast("当前没有登陆用户");
            return;
        }
        String phoneNumber = currentUser.getPhoneNumber();
        String[] params = new String[]{Constants.AppID,GlobalConfig.AppID,Constants.ECID, GlobalConfig.ECID, Constants.PhoneNumber, phoneNumber, Constants.AppType, GlobalConfig.AppType};
        new HttpUtil().callJson(handler, this, GlobalConfig.SERVERROOTPATH + GlobalConfig.SHARER_RETRIEVE_ORDER_INFO, OrderResultBean.class, params);
    }


}
