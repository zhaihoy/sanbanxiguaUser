package com.hbbc.mshare.sharer.Bill;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.hbbc.R;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.mshare.order.OrderPageAdapter;
import com.hbbc.util.Constants;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyApplication;
import com.hbbc.util.ToastUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */
public class BillFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener, OrderPageAdapter.onButtonClickListener {

    private static final String TAG = "BilllFragent";

    private String url;

    private RecyclerView rv;

    private HttpUtil httpUtil;

    private int fragment_type;//分类

    private FrameLayout fl_no_order;



    private BillPageAdapter pageadapter;

    private SwipeToLoadLayout loadLayout;

    private int FLAG_REFRESH = 1;

    private int FLAG_LOAD_MORE = 2;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what == FLAG_REFRESH) {//refresh
                BillBean BillBean = (BillBean) msg.obj;

                loadLayout.setRefreshing(false);

                if (BillBean != null && BillBean.getBillSet() != null && BillBean.getBillSet().size() > 0) {

                    List<com.hbbc.mshare.sharer.Bill.BillBean.BillSetBean> billSet = BillBean.getBillSet();
                    billPageAdapter.refreshData(billSet);//刷新成功了
                    
                    Log.d(TAG, "handleMessage:000000000000000000 "+billSet);
                    loadLayout.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.INVISIBLE);


                } else {

                    pb.setVisibility(View.INVISIBLE);

                    fl_no_order.setVisibility(View.VISIBLE);
                }
            }

            return true;

        }
    });

    private BillPageAdapter billPageAdapter;

    private android.widget.ProgressBar pb;

    private UserDao userDao;



    @Override
    public void onLoadMore() {
        handler.sendEmptyMessageDelayed(FLAG_LOAD_MORE, 2000);
    }



    @Override
    public void onRefresh() {
        queryData();
        fl_no_order.setVisibility(View.INVISIBLE);
        loadLayout.setRefreshing(true);
    }



    @Override
    public void onDelete(String orderId) {

    }



    @Override
    public void onConfirm(String orderId) {

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        fragment_type = args.getInt("Fragment_Type");
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        loadLayout = (SwipeToLoadLayout) view.findViewById(R.id.loadLayout);
        loadLayout.setOnRefreshListener(this);
        loadLayout.setLoadMoreEnabled(false);
        loadLayout.setOnLoadMoreListener(this);
        fl_no_order = (FrameLayout) view.findViewById(R.id.fl_no_order);

        rv = (RecyclerView) view.findViewById(R.id.swipe_target);
        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        billPageAdapter = new BillPageAdapter(MyApplication.getContext());
        rv.setAdapter(billPageAdapter);
        billPageAdapter.setOnButtonClickListener(this);

        pb = (ProgressBar) view.findViewById(R.id.pb);
//        requestOrderInfo();//自动刷新一次
        pb.setVisibility(View.VISIBLE);
        fl_no_order.setVisibility(View.INVISIBLE);
        initUrl();
        queryData();
    }



    private void queryData() {

        if (httpUtil == null)
            httpUtil = new HttpUtil();
        if (userDao == null) {
            userDao = UserDao.getDaoInstance(getContext());
        }

        LoginResultBean user = userDao.query();
        if (user == null || user.getPhoneNumber() == null) {
            ToastUtil.toast("请先登陆");
            return;
        }

        String phoneNumber = user.getPhoneNumber();
        String[] params = {Constants.AppID,GlobalConfig.AppID,Constants.ECID, GlobalConfig.ECID,
                Constants.PhoneNumber, phoneNumber, Constants.AppType, GlobalConfig.AppType};

        httpUtil.callJson(handler, FLAG_REFRESH, getContext(), url, BillBean.class, params);


    }



    private void initUrl() {

        url = GlobalConfig.SERVERROOTPATH;

        switch (fragment_type) {
            case 0:
                url += GlobalConfig.PAYBILL;
                break;
            case 1:
                url += GlobalConfig.ReachBILL;
                break;

        }
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.sharer_order_fragment_layout, container, false);

        return  inflate;
    }
}

