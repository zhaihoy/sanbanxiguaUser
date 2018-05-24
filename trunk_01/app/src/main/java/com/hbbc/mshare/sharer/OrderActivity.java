package com.hbbc.mshare.sharer;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.hbbc.R;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.mshare.user.OrderPageAdapter;
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
public class OrderActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private OrderPageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharer_order);
        initView();
    }


    @Override
    protected void initView() {

        MyTopbar topbar= (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        RecyclerView rv_order= (RecyclerView) findViewById(R.id.rv_order);

        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.swipe_color_1, R.color.swipe_color_2,
                R.color.swipe_color_3, R.color.swipe_color_4);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        adapter = new OrderPageAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_order.setLayoutManager(layoutManager);
        rv_order.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(true);
        requestOrderInfo();

    }



    /**
     * 请求当前用户的所有订单
     */
    public void requestOrderInfo(){
        UserDao userDao = UserDao.getDaoInstance(this);
        LoginResultBean currentUser = userDao.query();
        if(currentUser==null){
            ToastUtil.toast("当前没有登陆用户");
            return;
        }
        String phoneNumber = currentUser.getPhoneNumber();
        String[] params = new String[]{Constants.ECID,GlobalConfig.ECID,Constants.PhoneNumber,phoneNumber,Constants.AppType,GlobalConfig.AppType};
        new HttpUtil().callJson(handler,this, GlobalConfig.SHARER_SERVER_ROOT+GlobalConfig.SHARER_RETRIEVE_ORDER_INFO,OrderResultBean.class,params);
    }


    @Override
    protected void getMessage(Message msg) {
        mSwipeRefreshLayout.setRefreshing(false);
        OrderResultBean orderResultBean = (OrderResultBean) msg.obj;
        if(orderResultBean!=null&&orderResultBean.getOrderList()!=null){
            adapter.refreshData(orderResultBean.getOrderList());
            ToastUtil.toast("刷新完成");
        }else{
            ToastUtil.toast("获取失败,请稍后重试");
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
    public void onRefresh() {
        //
        Log.d("OrderActivity", "onRefresh: >>>"+"执行了刷新动作");
        requestOrderInfo();//重新再请求一次数据
    }


}
