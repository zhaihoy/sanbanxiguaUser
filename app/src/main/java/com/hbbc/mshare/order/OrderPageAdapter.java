package com.hbbc.mshare.order;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hbbc.R;
import com.hbbc.mshare.sharer.OrderResultBean;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/25.
 *
 */
public class OrderPageAdapter extends RecyclerView.Adapter implements View.OnClickListener {


    private List<OrderResultBean.OrderInfoBean> data=new ArrayList<>();
    onButtonClickListener onButtonClickListener;

    //支付状态：1、待支付；2、已支付
    int[] colors=new int[]{0xffff6600,0xff30c90c,0xffff0d0c}; //橙色/绿色/红色  分别对应待支付/已支付/失败
    String[] status=new String[]{"支付完成","订单已完成","交易失败"};

    protected Context context;





    public OrderPageAdapter(Context context) {

        this.context = context;
    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {

        super.onAttachedToRecyclerView(recyclerView);

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View inflated = LayoutInflater.from(parent.getContext()).inflate(R.layout.sharer_order_item_layout, parent, false);
        return new OrderViewHolder(inflated);
    }

    //{"OrderTime":"2017-10-20 14:46:51.0","Status":10,"GoodsSNID":10000043,"OrderPayOrderID":"O_Wx_P_1000159_20171015201233","PhoneNumber":"15093337480",
    // "GoodsPic":"http://home.handbbc.com:8680/sdsebo/asset/distfile/ae46a405-2979-45ed-a2fd-5582ae515c6d_0.jpg","SharerName":"大"}
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        OrderViewHolder vh= (OrderViewHolder) holder;
        OrderResultBean.OrderInfoBean orderBean = data.get(position);

        vh.tv_goods_id.setText(String.valueOf(orderBean.getGoodsSNID()));
        vh.tv_order_id.setText(orderBean.getOrderPayOrderID());
        vh.tv_purchaser.setText(Integer.valueOf(GlobalConfig.AppType)==2 ? orderBean.getSharerName() : orderBean.getUserName());
        vh.tv_phone_number.setText(orderBean.getPhoneNumber());
        vh.tv_timestamp.setText(orderBean.getOrderTime());
        Glide.with(vh.itemView.getContext())
                .load(orderBean.getGoodsPic())
                .placeholder(R.drawable.global_img_error)
                .error(R.drawable.global_img_error)
                .into(vh.iv_thumbnail);


        int status = orderBean.getStatus();


        vh.btn_confirm.setTag(R.id.TAG_CONFIRM_ORDER,orderBean.getOrderPayOrderID());
        if (status==10||status==8) {
            vh.btn_delete.setTag(R.id.TAG_DELETE_ORDER, orderBean.getOrderPayOrderID());
        }else{


        }
        vh.btn_confirm.setOnClickListener(this);
        vh.btn_delete.setOnClickListener(this);


     if(status == 8){//待支付
            vh.tv_status.setBackgroundColor(colors[0]);// TODO: 2017/9/26
            vh.tv_status.setText(this.status[0]);
            vh.btn_delete.setVisibility(View.GONE);
            vh.tv_status.setTextSize(12);
        }else if(status == 10){//订单已完成
            vh.tv_status.setBackgroundColor(colors[1]);// TODO: 2017/9/26
            vh.tv_status.setText(this.status[1]);
            vh.tv_status.setTextSize(12);
            vh.btn_confirm.setVisibility(View.GONE);
            vh.btn_delete.setVisibility(View.VISIBLE);
        }else{//支付失败status=6 & 7
            vh.tv_status.setBackgroundColor(colors[2]);// TODO: 2017/9/26
            vh.tv_status.setText(this.status[2]);
            vh.btn_confirm.setVisibility(View.GONE);
            vh.btn_delete.setVisibility(View.VISIBLE);
        }

        if(GlobalConfig.AppType.equals("2")){//用户端没有交易完成按钮
            vh.btn_confirm.setVisibility(View.GONE);
        }

        vh.tv_sum.setText(String.valueOf(orderBean.getGoodsUsePrice()));


        // TODO: 2017/9/15 条目的加载动画
//        Animation anim = AnimationUtils.loadAnimation(vh.itemView.getContext(), R.anim.sharer_rv_item_loading_anim);
//        vh.itemView.setAnimation(anim);
//        Log.d("tag", "onBindViewHolder: position--->"+position);

    }

    @Override
    public int getItemCount() {

        return data.size();
    }

    public void refreshData(List<OrderResultBean.OrderInfoBean> newData){
        this.data.clear();
        for (OrderResultBean.OrderInfoBean e :
                newData) {
            this.data.add(e);
        }
        notifyDataSetChanged();
    }

    public void loadMoreData(List<OrderResultBean.OrderInfoBean> newData){
        for (OrderResultBean.OrderInfoBean e :
                newData) {
            this.data.add(e);
        }
        notifyDataSetChanged();
    }

    public void clearData(){
        data.clear();
    }

    public void removeItem(String orderId){

        for (int i = 0; i < data.size(); i++) {
            if(data.get(i).getOrderPayOrderID().equals(orderId)){
                data.remove(i);
                break;
            }
        }
        notifyDataSetChanged();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirm://完成订单
                this.onButtonClickListener.onConfirm((String) v.getTag(R.id.TAG_CONFIRM_ORDER));
                break;
            case R.id.btn_delete://删除订单
                this.onButtonClickListener.onDelete((String) v.getTag(R.id.TAG_DELETE_ORDER));
                break;
        }
    }

    public void setOnButtonClickListener(onButtonClickListener listener){

        this.onButtonClickListener = listener;

    }







    public interface onButtonClickListener{

        void onDelete(String orderId);
        void onConfirm(String orderId);
    }



    class OrderViewHolder extends RecyclerView.ViewHolder{


        private final TextView tv_status;

        private final TextView tv_goods_id;

        private final TextView tv_order_id;

        private final ImageView iv_thumbnail;

        private final TextView tv_purchaser;

        private final TextView tv_phone_number;

        private final TextView tv_timestamp;

        private final Button btn_confirm;

        private final Button btn_delete;

        private final  TextView tv_sum;


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

            btn_confirm = (Button) itemView.findViewById(R.id.btn_confirm);
            btn_delete = (Button) itemView.findViewById(R.id.btn_delete);
            tv_sum  = (TextView) itemView.findViewById(R.id.tv_sum);
        }
    }





}
