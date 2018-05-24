package com.hbbc.mshare.message;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hbbc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/24.
 *
 */
public class System_Rv_Adapter extends MessageAdapter{

    private List<MessageBean.Message> list=new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.mshare_fragment_business_message_item,parent,false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MessageBean.Message bean = list.get(position);
        MyViewHolder vh= (MyViewHolder) holder;
        //TODO 接口还没有呢!!!
        Glide.with(vh.itemView.getContext())
                .load(bean.getMessageTypePic())
                .placeholder(R.drawable.global_img_error)
                .error(R.drawable.global_img_error)
                .into(vh.iv_img);

        vh.tv_title.setText(bean.getMessageName());
        vh.tv_content.setText(bean.getMessageContent());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    @Override
    void refreshData(List<MessageBean.Message> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }



    static class  MyViewHolder extends RecyclerView.ViewHolder{

        public final ImageView iv_img;

        public final TextView tv_title;

        public final TextView tv_content;

        public final TextView tv_publishedTime;

        public Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            tv_publishedTime = (TextView) itemView.findViewById(R.id.tv_published);
            context=itemView.getContext();
        }
    }


}
