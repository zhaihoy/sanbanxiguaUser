package com.hbbc.mshare.message;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hbbc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/24.
 *
 */
public class Business_Rv_Adapter extends RecyclerView.Adapter {

    private List<BusinessMessageBean> list=new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.mshare_fragment_business_message_item, parent, false);
        return new MyViewHolder(inflate);
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {



    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }



    @Override
    public int getItemCount() {

        return list.size();
    }
}
