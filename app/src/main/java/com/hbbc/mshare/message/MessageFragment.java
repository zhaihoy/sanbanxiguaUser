package com.hbbc.mshare.message;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.hbbc.R;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.util.Constants;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.ToastUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/24.
 *
 */
public class MessageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final int SYSTEM_MESSAGE_ADAPTER = 0;

    private static final int BUSINESS_MESSAGE_ADAPTER = 1;

    private MessageAdapter adapter;

    private SwipeRefreshLayout refreshLayout;

    private String url = (Integer.valueOf(GlobalConfig.AppType) == 2) ? GlobalConfig.SERVERROOTPATH : GlobalConfig.SHARER_SERVER_ROOT;

    private FrameLayout fl_no_order;

    private ProgressBar pb;

    private int FLAG_AUTO_REFRESH = 2;

    private int FLAG_PULL_REFRESH = 3;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            MessageBean messageBean = (MessageBean) msg.obj;

            if(msg.what == FLAG_AUTO_REFRESH){

                if(messageBean!=null && messageBean.getResult()){
                    ArrayList<MessageBean.Message> messages = messageBean.getMessageList();
                    if(messages != null && messages.size() > 0){
                        adapter.refreshData(messages);
                        pb.setVisibility(View.INVISIBLE);
                        fl_no_order.setVisibility(View.INVISIBLE);
                    }else{
                        pb.setVisibility(View.INVISIBLE);
                        fl_no_order.setVisibility(View.VISIBLE);
                    }
                }else{
                    pb.setVisibility(View.INVISIBLE);
                    fl_no_order.setVisibility(View.VISIBLE);
                }

            }else if(msg.what == FLAG_PULL_REFRESH){

                refreshLayout.setRefreshing(false);//结束刷新状态,取消刷新条的显示
                if(messageBean != null && messageBean.getResult()){
                    ArrayList<MessageBean.Message> messageList = messageBean.getMessageList();
                    if(messageList != null && messageList.size() > 0){
                        adapter.refreshData(messageList);
                    }else{
                        ToastUtil.toast("刷新失败，请稍后重试");
                    }
                }else{
                    ToastUtil.toast("刷新失败，请稍后重试");
                }


            }



            if (messageBean != null) {
                ArrayList<MessageBean.Message> messages = messageBean.getMessageList();
                if (messages != null && messages.size() > 0) {
                    adapter.refreshData(messages);
                }
            }

        }
    };



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.mshare_fragment_message, container, false);
        initControlView(inflate);
        return inflate;
    }



    private void initControlView(View view) {

        pb = (ProgressBar) view.findViewById(R.id.pb);
        fl_no_order = (FrameLayout) view.findViewById(R.id.fl_no_order);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_green_light,
                android.R.color.black, android.R.color.holo_red_light);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout.setOnRefreshListener(this);

        int adapter_type = getArguments().getInt("Adapter_Type");


        if (adapter_type == SYSTEM_MESSAGE_ADAPTER) {
            adapter = new System_Rv_Adapter();
            url += (Integer.valueOf(GlobalConfig.AppType) == 2) ? GlobalConfig.USER_SYSTEM_MESSAGE : GlobalConfig.SHARER_SYSTEM_MESSAGE;
        } else if (adapter_type == BUSINESS_MESSAGE_ADAPTER) {
            adapter = new Business_Rv_Adapter();
            url += (Integer.valueOf(GlobalConfig.AppType) == 2) ? GlobalConfig.USER_BUSINESS_MESSAGE : GlobalConfig.SHARER_BUSINESS_MESSAGE;
        }

        recyclerView.setAdapter(adapter);
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        pb.setVisibility(View.VISIBLE);
        fl_no_order.setVisibility(View.INVISIBLE);
        loadData(FLAG_AUTO_REFRESH);
    }


    private void loadData(int flag) {

        LoginResultBean user = UserDao.getDaoInstance(getContext()).query();

        if (user == null || user.getPhoneNumber() == null) {
            return;
        }

        String phoneNumber = user.getPhoneNumber();
        String[] params = new String[]{Constants.AppID,GlobalConfig.AppID,Constants.ECID, GlobalConfig.ECID,
                Constants.PhoneNumber, phoneNumber, Constants.AppType, GlobalConfig.AppType};
        new HttpUtil().callJson(handler, flag ,getActivity(), url, MessageBean.class, params);//发起网络请求数据

    }



    @Override
    public void onRefresh() {

        refreshLayout.setRefreshing(true);
        pb.setVisibility(View.INVISIBLE);
        fl_no_order.setVisibility(View.INVISIBLE);
        loadData(FLAG_PULL_REFRESH);
        Log.e("tag", "onRefresh: url------>"+url);


    }
}
