package com.hbbc.mshare.user;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbbc.R;
import com.hbbc.mshare.sharer.OrderResultBean;
import com.hbbc.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/25.
 *
 */
public class OrderPageAdapter extends RecyclerView.Adapter{

    private List<OrderResultBean.OrderInfoBean> data=new ArrayList<>();

    //支付状态：1、待支付；2、支付成功；3、支付失败；4、支付取消；
    int[] colors=new int[]{0xff0000ff,0xff00ff00,0xffff0000,0xff0f0f0f};
    String[] descriptions=new String[]{"待支付","支付成功","支付失败","支付取消"};



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {

        super.onAttachedToRecyclerView(recyclerView);

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View inflated = LayoutInflater.from(parent.getContext()).inflate(R.layout.sharer_order_item_layout, parent, false);
        return new OrderViewHolder(inflated);
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OrderViewHolder vh= (OrderViewHolder) holder;
        OrderResultBean.OrderInfoBean orderBean = data.get(position);

        vh.tv_goods_id.setText(orderBean.getGoodsSNID());
        vh.tv_order_id.setText(orderBean.getOrderID());
        vh.tv_purchaser.setText(orderBean.getUserName());
        vh.tv_phone_number.setText(orderBean.getPhoneNumber());
        vh.tv_timestamp.setText(orderBean.getOrderBeginTime());
//        Glide.with(vh.itemView.getContext()).load(***)
        int status = Integer.valueOf(orderBean.getStatus());
        vh.tv_status.setBackgroundColor(colors[status-1]);
        vh.tv_status.setText(descriptions[status-1]);
        // TODO: 2017/9/15 条目的加载动画
//        Animation anim = AnimationUtils.loadAnimation(vh.itemView.getContext(), R.anim.sharer_rv_item_loading_anim);
//        vh.itemView.setAnimation(anim);
//        Log.d("tag", "onBindViewHolder: position--->"+position);




    }



    @Override
    public int getItemCount() {

        return data.size();
    }

    public void refreshData(List<OrderResultBean.OrderInfoBean> data){
        this.data.clear();
        for (OrderResultBean.OrderInfoBean e :
                data) {
            this.data.add(e);
        }
        //
        notifyDataSetChanged();
    }


    class OrderViewHolder extends RecyclerView.ViewHolder{


        private final TextView tv_status;

        private final TextView tv_goods_id;

        private final TextView tv_order_id;

        private final ImageView iv_thumbnail;

        private final TextView tv_purchaser;

        private final TextView tv_phone_number;

        private final TextView tv_timestamp;



        public OrderViewHolder(View itemView) {
            super(itemView);
            //添加滤镜
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    UIUtil.drawViewTouchShow(v,event);
                    return false;
                }
            });
            tv_goods_id = (TextView) itemView.findViewById(R.id.tv_goods_id);
            tv_order_id = (TextView) itemView.findViewById(R.id.tv_order_id);
            iv_thumbnail = (ImageView) itemView.findViewById(R.id.iv_thumbnail);
            tv_purchaser = (TextView) itemView.findViewById(R.id.tv_purchaser);
            tv_phone_number = (TextView) itemView.findViewById(R.id.tv_contact_number);
            tv_timestamp = (TextView) itemView.findViewById(R.id.tv_timestamp);
            tv_status = (TextView) itemView.findViewById(R.id.tv_order_status);


        }
    }





}
