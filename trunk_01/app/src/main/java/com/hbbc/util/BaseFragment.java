package com.hbbc.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment基类
 */
public abstract class BaseFragment extends Fragment {

    /**
     * 加载网络数据
     */
    @SuppressWarnings("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            getMessage(msg);
        }
    };

    //是否加载视图
    private boolean isInit = false;

    private boolean isLoad = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(setContentView(), container, false);
        initControlView(view);
        //刚进入就加载数据
        isInit = true;
        isCanLoad();
        return view;
    }


    /**
     * 初始化控件
     */
    protected abstract void initControlView(View view);


    protected abstract int setContentView();



    /**
     * 消息回调
     */
    protected void getMessage(Message msg) {

    }



    /**
     * 获取Bundle
     */
    public Bundle getFragmentBundle() {

        Bundle bundle = null;
        if (getArguments() != null)
            bundle = getArguments();
        return bundle;
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        isCanLoad();
    }



    /**
     * 可以加载数据
     */
    public void isCanLoad() {

        if (!isInit) {
            return;
        }

        if (getUserVisibleHint()) {
            LoadData();
            isLoad = true;
        } else {
            if (isLoad)
                stopData();
        }
    }



    /**
     * 停止加载数据的逻辑业务
     */
    protected abstract void stopData();

    /**
     * 加载数据
     */
    protected abstract void LoadData();
}
