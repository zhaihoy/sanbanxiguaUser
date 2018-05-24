package com.hbbc.mshare.message;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by Administrator on 2017/10/26.
 *
 */
public abstract class MessageAdapter extends RecyclerView.Adapter {

    abstract void refreshData(List<MessageBean.Message> list);

}
