package com.hbbc.mshare.order;

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
import com.hbbc.mshare.login.BaseResultBean;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.mshare.sharer.OrderResultBean;
import com.hbbc.util.Constants;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyApplication;
import com.hbbc.util.ToastUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/10/12.
 *
 */
public class OrderFrament extends Fragment implements OnRefreshListener, OnLoadMoreListener, OrderPageAdapter.onButtonClickListener {

    private static final String TAG = "OrderFrament";

    private OrderPageAdapter adapter;

    private SwipeToLoadLayout loadLayout;

    private ProgressBar pb;

    private int fragment_type;//分类

    private HttpUtil httpUtil;

    private UserDao userDao;

    private int FLAG_AUTOREFRESH = 0;

    private int FLAG_REFRESH = 1;

    private int FLAG_LOAD_MORE = 2;

    private  final int FLAG_SETORDERCOMPLETE = 3;

    private static final int FLAG_DELETE_ORDER = 4;

    private String url;

    ////支付状态：6&7、未完成订单 8、待支付修改为订单完成 10、已支付

    private String orderIdToConfirm;
    private String orderIdToDelete;

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {


            if (msg.what == FLAG_REFRESH) {//refresh
                OrderResultBean resultBean = (OrderResultBean) msg.obj;

                loadLayout.setRefreshing(false);

                if (resultBean != null && resultBean.getOrderList() != null && resultBean.getOrderList().size() > 0) {

                    List<OrderResultBean.OrderInfoBean> orders = resultBean.getOrderList();
                    adapter.refreshData(orders);//刷新成功了
                    Log.d(TAG, "handleMessage: 返回结果了" + orders.size());
                    loadLayout.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.INVISIBLE);


                } else {

                    pb.setVisibility(View.INVISIBLE);
                    fl_no_order.setVisibility(View.VISIBLE);

                }
            }else if (msg.what == FLAG_LOAD_MORE) {//load more


            }else if(msg.what == FLAG_SETORDERCOMPLETE){

                BaseResultBean resultBean = (BaseResultBean) msg.obj;

                if(resultBean != null && resultBean.getResult()){
                    //确认成功了，删除此条目
                    adapter.removeItem(orderIdToConfirm);

                }else{
                    ToastUtil.toast("操作超时，请重试");
                    orderIdToConfirm = null;
                }
            }   else if(msg.what == FLAG_DELETE_ORDER){

                BaseResultBean resultBean = (BaseResultBean) msg.obj;
                if(resultBean!=null && resultBean.getResult()){
                    adapter.removeItem(orderIdToDelete);
                }else{
                    ToastUtil.toast("操作超时，请重试");
                }
            }


            return true;
        }
    });

    private RecyclerView rv;

    private FrameLayout fl_no_order;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        fragment_type = args.getInt("Fragment_Type");

    }



    private void initUrl() {

        if (Integer.valueOf(GlobalConfig.AppType) == 2) {//用户端

            url = GlobalConfig.SERVERROOTPATH;

            switch (fragment_type) {
                case 0:
                    url += GlobalConfig.MSHARE_QUERY_ALL_ORDERS;
                    break;
            /*    case 1:
                    url += GlobalConfig.MSHARE_QUERY_ORDERS_NOT_PAYED;
                    break;*/
                case 2:
                    url += GlobalConfig.MSHARE_QUERY_FAILED_ORDERS;
                    break;
                case 3:
                    url += GlobalConfig.MSHARE_QUERY_PAYED_ORDERS;
                    break;
            }
        } else {//共享端

            url = GlobalConfig.SHARER_SERVER_ROOT;
            switch (fragment_type) {
                case 0:
                    url += GlobalConfig.SHARER_RETRIEVE_ORDER_ALL_TYPE;
                    break;
                case 1:
                    url += GlobalConfig.SHARER_RETRIEVE_ORDER_NOT_PAYED;
                    break;
                case 2:
                    url += GlobalConfig.SHARER_RETRIEVE_ORDER_PAYED;
                    break;
            }

        }

    }





    private void queryData() {

        if (httpUtil == null)
            httpUtil = new HttpUtil();
        if (userDao == null)
            userDao = UserDao.getDaoInstance(getContext());

        LoginResultBean user = userDao.query();
        if (user == null || user.getPhoneNumber() == null) {
            ToastUtil.toast("请先登陆");
            return;
        }

        String phoneNumber = user.getPhoneNumber();
        String[] params = {Constants.AppID,GlobalConfig.AppID,Constants.ECID, GlobalConfig.ECID,
                Constants.PhoneNumber, phoneNumber, Constants.AppType, GlobalConfig.AppType};

        httpUtil.callJson(handler, FLAG_REFRESH, getContext(), url, OrderResultBean.class, params);

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.sharer_order_fragment_layout, container, false);

        return inflate;
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

        adapter = new OrderPageAdapter(MyApplication.getContext());
        rv.setAdapter(adapter);
        adapter.setOnButtonClickListener(this);

        pb = (ProgressBar) view.findViewById(R.id.pb);
//        requestOrderInfo();//自动刷新一次
        pb.setVisibility(View.VISIBLE);
        fl_no_order.setVisibility(View.INVISIBLE);
        initUrl();
        queryData();

    }



    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: url----------------->" +
                "---->"+url);
    }


    @Override
    public void onRefresh() {

        queryData();
        fl_no_order.setVisibility(View.INVISIBLE);
        loadLayout.setRefreshing(true);
    }



    @Override
    public void onLoadMore() {

        handler.sendEmptyMessageDelayed(FLAG_LOAD_MORE, 2000);//1:load more

    }



    @Override
    public void onDelete(String orderId) {
        orderIdToDelete = orderId;
        if(httpUtil == null)
            httpUtil = new HttpUtil();
        if(userDao == null)
            userDao = UserDao.getDaoInstance(getContext());
        LoginResultBean user = userDao.query();
        String phoneNumber = user.getPhoneNumber();
        String[] params = {Constants.AppID,GlobalConfig.AppID,Constants.ECID,GlobalConfig.ECID,Constants.PhoneNumber,phoneNumber,Constants.AppType,GlobalConfig.AppType,Constants.OrderPayOrderID,orderId};
        String url = ((Integer.valueOf(GlobalConfig.AppType)==2)? GlobalConfig.MSHARE_SERVER_ROOT:GlobalConfig.SHARER_SERVER_ROOT) +GlobalConfig.SHARER_DELETE_ORDER;
        httpUtil.callJson(handler,FLAG_DELETE_ORDER,getContext(),url, BaseResultBean.class,params);

    }



    @Override
    public void onConfirm(String orderId) {//确认订单交易完成，成功后，将当前订单从集合中移除即可
        orderIdToConfirm = orderId;

        if(httpUtil==null)
            httpUtil = new HttpUtil();

        if(userDao==null)
            userDao = UserDao.getDaoInstance(getContext());
        LoginResultBean user = userDao.query();
        String phoneNumber = user.getPhoneNumber();
        String[] params = {Constants.AppID,GlobalConfig.AppID,Constants.ECID,GlobalConfig.ECID,Constants.PhoneNumber,phoneNumber,Constants.AppType,GlobalConfig.AppType,Constants.OrderPayOrderID,orderId};
        String url = ((Integer.valueOf(GlobalConfig.AppType)==2)? GlobalConfig.SERVERROOTPATH:GlobalConfig.SHARER_SERVER_ROOT) +GlobalConfig.SHARER_CONFIRM_ORDER_PAYED;
        httpUtil.callJson(handler,FLAG_SETORDERCOMPLETE,getContext(),url, BaseResultBean.class,params);

    }
}
