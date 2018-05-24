package com.hbbc.mshare.message;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hbbc.R;
import com.hbbc.util.HttpUtil;

/**
 * Created by Administrator on 2017/7/24.
 *
 */
public class MessageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final int SYSTEM_MESSAGE_ADAPTER = 0;

    private static final int BUSINESS_MESSAGE_ADAPTER = 1;

    private RecyclerView recyclerView;

    private SwipeRefreshLayout refreshLayout;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            refreshLayout.setRefreshing(false);//结束刷新状态,取消刷新条的显示

        }
    };

    private String url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.mshare_fragment_message, container, false);
        initControlView(inflate);
        return inflate;
    }

    private void initControlView(View view) {

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,android.R.color.holo_green_light,
                android.R.color.black,android.R.color.holo_red_light);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout.setOnRefreshListener(this);

        int adapter_type = getArguments().getInt("Adapter_Type");

        RecyclerView.Adapter adapter = null;
        if (adapter_type == SYSTEM_MESSAGE_ADAPTER) {
            adapter = new System_Rv_Adapter();
        } else if (adapter_type == BUSINESS_MESSAGE_ADAPTER) {
            adapter = new Business_Rv_Adapter();
        }
        if (adapter != null)
            recyclerView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        loadData();
    }



    private void loadData() {

        String[] params = new String[]{};
        new HttpUtil().callJson(handler, getActivity(), url, params);//发起网络请求数据
    }



    @Override
    public void onRefresh() {


    }
}
