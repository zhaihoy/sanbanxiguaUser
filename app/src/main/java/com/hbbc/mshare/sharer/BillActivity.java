package com.hbbc.mshare.sharer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hbbc.R;
import com.hbbc.mshare.sharer.authentication.BillBean;
import com.hbbc.mshare.user.UserBean;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyCircleImageView;
import com.hbbc.util.MyTopbar;

import java.util.List;

/**
 * Created by Administrator on 2017/10/11.
 */
public class BillActivity extends AppCompatActivity implements MyTopbar.OnTopBarClickListener, View.OnClickListener {

    private static final String TAG = "BillActivity";

    private ImageView iv_top;

    private LinearLayout topContainer;

    private LinearLayout bottomContainer;

    private ImageView iv_bottom;

    private HttpUtil httpUtil;

    private boolean FLAG_CHILD_ADDED = true;

    private int normalHeight_top = 0;

    private int normalHeight_bottom = 0;


    @SuppressWarnings("HandlerLeak")
//    private Handler handler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            //add child views into container!
//
////            normalHeight_top = getMeasuredHeight(topContainer);
////            normalHeight_bottom = getMeasuredHeight(bottomContainer);
////            FLAG_CHILD_ADDED = true;//标志窗器中添加child Views 完毕;
////            return false;
////        }
//    });

    private LinearLayout toggle_top;

    private LinearLayout toggle_bottom;

    private ScrollView scrollView;

    private ListView lvBill;

    private View convertView;

    private UserBean userBean;

    private BillBean billBean;

    private BillBean billBean1;

    private List<BillBean.BillSetBean> billSet;


//    public int getMeasuredHeight(View v) {
//
//        v.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        return v.getMeasuredHeight();
//    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_bill);

       /* initView();
        initData();*/
        //找到ListView
        lvBill = (ListView) findViewById(R.id.lv_bill);
        billBean = (BillBean) getIntent().getSerializableExtra("BillBean");
        billSet = billBean.getBillSet();
        MyTopbar MyTopbar = (com.hbbc.util.MyTopbar) findViewById(R.id.top_bar);
        MyTopbar.setOnTopBarClickListener(new MyTopbar.OnTopBarClickListener() {
            @Override
            public void setOnLeftClick() {
                finish();
            }



            @Override
            public void setOnRightClick() {

            }
        });
     /*   normalHeight_bottom = getMeasuredHeight(bottomContainer);
        normalHeight_top = getMeasuredHeight(topContainer);*/

        //设置适配器
         lvBill.setAdapter(new MyBillAdapter());

    }



    private void initData() {

        if (httpUtil == null)
            httpUtil = new HttpUtil();
        //httpUtil.callJson();

    }



    private void initView() {

        MyTopbar topbar = (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);

     /*   iv_top = (ImageView) findViewById(R.id.iv_arrow_top);
        iv_bottom = (ImageView) findViewById(R.id.iv_arrow_bottom);

        toggle_top = (LinearLayout) findViewById(R.id.ll_toggle_top);
        toggle_bottom = (LinearLayout) findViewById(R.id.ll_toggle_bottom);
        toggle_top.setOnClickListener(this);
        toggle_bottom.setOnClickListener(this);

        topContainer = (LinearLayout) findViewById(R.id.ll_record_this_month);
        bottomContainer = (LinearLayout) findViewById(R.id.ll_record_all);

        scrollView = (ScrollView) findViewById(R.id.scroll_container);*/

    }



//    @Override
//    protected void onResume() {
//
//        super.onResume();
//        ViewGroup.LayoutParams params = bottomContainer.getLayoutParams();
//        params.height = 0;
//        bottomContainer.setLayoutParams(params);
//
//    }



//    @Override
//    public void finish() {
//
//        super.finish();
//        overridePendingTransition(R.anim.global_in, R.anim.global_out);
//    }



    @Override
    public void setOnLeftClick() {

        finish();
    }



    @Override
    public void setOnRightClick() {

    }



    @Override
    public void onClick(View v) {
/*
        switch (v.getId()) {
            case R.id.ll_toggle_top:
                //打开or关闭
                if (FLAG_CHILD_ADDED && normalHeight_top != 0)
                    toggleTop();
                break;
            case R.id.ll_toggle_bottom:
                if (FLAG_CHILD_ADDED && normalHeight_bottom != 0)//如果从服务器返回childViews并添加完毕了,那么我们就响应这个切换事件
                    toggleBottom();
                break;

        }*/

    }



//    private void toggleBottom() {//ll_record_all
//
//        ValueAnimator va;
//        if (iv_bottom.getRotation() == 0) {
//            va = ValueAnimator.ofInt(0, normalHeight_bottom);
//        } else {
//            va = ValueAnimator.ofInt(normalHeight_bottom, 0);
//        }
//        va.setDuration(450);
//        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//
//                int value = (int) animation.getAnimatedValue();
//                ViewGroup.LayoutParams params = bottomContainer.getLayoutParams();
//                params.height = value;
//                bottomContainer.setLayoutParams(params);
//                if (scrollView.canScrollVertically(View.SCROLL_AXIS_VERTICAL)) {
//                    scrollView.scrollBy(0, value);
//                }
//            }
//        });

//        va.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//                super.onAnimationStart(animation);
//                toggle_bottom.setEnabled(false);//动画过程中,消费但不响应点击事件
//            }



//            @Override
//            public void onAnimationEnd(Animator animation) {
//
//                super.onAnimationEnd(animation);
//                toggle_bottom.setEnabled(true);//响应点击事件
//            }
//        });

//        va.setInterpolator(new AccelerateDecelerateInterpolator());
//        va.start();
//
//        ObjectAnimator.ofFloat(iv_bottom, "rotation", (iv_bottom.getRotation() == 0) ? 180 : 0).setDuration(450).start();
//    }



    private void toggleTop() {

        ValueAnimator va;
        if (iv_top.getRotation() == 0) {
            va = ValueAnimator.ofInt(normalHeight_top, 0);
        } else {
            va = ValueAnimator.ofInt(0, normalHeight_top);
        }

        va.setDuration(450);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                int value = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams params = topContainer.getLayoutParams();
                params.height = value;
                topContainer.setLayoutParams(params);


            }
        });
        va.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {

                super.onAnimationStart(animation);
                toggle_top.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                super.onAnimationEnd(animation);
                toggle_top.setEnabled(true);

            }
        });
        va.setInterpolator(new AccelerateDecelerateInterpolator());
        va.start();
        //动态获取到这个窗器的高度,这是关键;

        ObjectAnimator.ofFloat(iv_top, "rotation", (iv_top.getRotation() == 0) ? -180 : 0).setDuration(450).start();
    }

    private class MyBillAdapter extends BaseAdapter {


        private String billType;



        @Override
        public int getCount() {

            //请求回来接口中的条目的总数
            return billSet.size();
        }



        @Override
        public Object getItem(int position) {

            return position;
        }



        @Override
        public long getItemId(int position) {

            return 0;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder myViewHolder=null;
           if(convertView==null){
                //不错，布局写好了
               convertView = View.inflate(BillActivity.this, R.layout.mshare_bill_item_layout, null);
               myViewHolder = new  ViewHolder();

               myViewHolder.billMoney = (TextView) convertView.findViewById(R.id.tv_amountbill);
               myViewHolder.ciecle =  (MyCircleImageView) convertView.findViewById(R.id.circle);
               myViewHolder.tv_time=(TextView) convertView.findViewById(R.id.tv_time);
               /*myViewHolder.tv_type=(TextView) convertView.findViewById(R.id.tv_type);*/
               myViewHolder.tv_payMoney=(TextView) convertView.findViewById(R.id.tv_payMoney);
               convertView.setTag(myViewHolder);
           }
            else{
               myViewHolder = (ViewHolder) convertView.getTag();
           }

            myViewHolder.tv_time.setText(billSet.get(position).getBillTime());
            billType = billSet.get(position).getBillType();
            if (billType.equals("1")){


                myViewHolder.billMoney.setText("订单支付");
                myViewHolder.tv_payMoney.setText("-"+billSet.get(position).getBillMoney()+"元");
                myViewHolder.tv_payMoney.setTextSize(13);
            }else {

                myViewHolder.billMoney.setText("账户充值");
                myViewHolder.tv_payMoney.setText("+"+billSet.get(position).getBillMoney()+"元");
                myViewHolder.tv_payMoney.setTextSize(13);

            }
            return convertView;
        }
           class  ViewHolder{
              MyCircleImageView ciecle;
              TextView billMoney;
              TextView tv_time ;
              TextView tv_type;
              TextView tv_payMoney;
          }
    }


}
