package com.hbbc.mshare.sharer.Bill;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hbbc.R;
import com.hbbc.util.MyCircleImageView;
import com.hbbc.util.UIUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/12/15.
 * 这个适配器用于条目的显示和刷新
 */
public class BillPageAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private  List<BillBean.BillSetBean> data=new ArrayList<>();
    protected Context context;


    private BillFragment onButtonClickListener;

    private String billType;



    public BillPageAdapter(Context context) {
        this.context = context;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View inflated = LayoutInflater.from(parent.getContext()).inflate(R.layout.mshare_bill_item_layout, parent, false);
        return new BillViewHolder(inflated);
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
     //绑定条目
        BillViewHolder myViewHolder= (BillViewHolder) holder;
        BillBean.BillSetBean billSet = data.get(position);

        myViewHolder.tv_time.setText(billSet.getBillTime());
        billType = billSet.getBillType();
        if (billType.equals("1")){


            myViewHolder.tv_amountbill.setText("订单支付");
            myViewHolder.tv_payMoney.setText("-"+billSet. getBillMoney()+"元");
            myViewHolder.tv_payMoney.setTextSize(13);
        }else {

            myViewHolder.tv_amountbill.setText("账户充值");
            myViewHolder.tv_payMoney.setText("+"+billSet. getBillMoney()+"元");
            myViewHolder.tv_payMoney.setTextSize(13);

        }

    }



    @Override
    public int getItemCount() {

        return data.size();
    }



    @Override
    public void onClick(View v) {

    }


/*
* 用来刷新数据
*
* */
    public void refreshData(List<BillBean.BillSetBean> billSet) {
        this.data.clear();
        for (BillBean.BillSetBean e :
                billSet) {
            this.data.add(e);
        }
        notifyDataSetChanged();
    }



    public void setOnButtonClickListener(BillFragment onButtonClickListener) {

        this.onButtonClickListener = onButtonClickListener;
    }



    private class BillViewHolder extends RecyclerView.ViewHolder {


        private final MyCircleImageView circle;

        private final TextView tv_amountbill;

        private final TextView tv_time;

        private final TextView tv_payMoney;



        public BillViewHolder(View itemView) {
            super(itemView);
            //添加滤镜
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    UIUtil.drawViewTouchShow(v,event);
                    return false;
                }
            });
            circle = (MyCircleImageView) itemView.findViewById(R.id.circle);
            tv_amountbill = (TextView) itemView.findViewById(R.id.tv_amountbill);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_payMoney = (TextView) itemView.findViewById(R.id.tv_payMoney);
        }
    }
}
