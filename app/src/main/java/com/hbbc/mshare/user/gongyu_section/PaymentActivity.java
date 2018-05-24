package com.hbbc.mshare.user.gongyu_section;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hbbc.R;
import com.hbbc.mshare.user.detail.Detail_Bean;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.MyTopbar;

/**
 * Created by Administrator on 2017/11/10.
 *
 */
public class PaymentActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener {


    private TextView tv_amount;

    private TextView tv_desc;

    private Detail_Bean detail_bean;

    private LinearLayout ll_zfb;

    private LinearLayout ll_wechat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_layout_payment);
        detail_bean = (Detail_Bean) getIntent().getSerializableExtra("detail_bean");

        initView();
    }



    @Override
    protected void initView() {

        MyTopbar topbar = (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);
        tv_amount = (TextView) findViewById(R.id.tv_amount);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        ll_zfb = (LinearLayout) findViewById(R.id.ll_zfb);
        ll_wechat = (LinearLayout) findViewById(R.id.ll_wechat);
        TextView tv_desc = (TextView) findViewById(R.id.tv_desc);
        ImageView imageView = (ImageView) findViewById(R.id.img);
        Button btn_submit = (Button) findViewById(R.id.btn_submit);

        ll_zfb.setOnClickListener(this);
        ll_wechat.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        if(detail_bean == null)return;
        Glide.with(this).load(detail_bean.getPicList().get(0)).placeholder(R.drawable.global_img_error).error(R.drawable.global_img_error).into(imageView);
        tv_desc.setText(detail_bean.getGoodsIntroduceText());
        tv_amount.setText(detail_bean.getGoodsDeposit());

    }



    @Override
    public void onClick(View v) {

        super.onClick(v);
        switch (v.getId()){
            case R.id.ll_zfb:
                ((RadioButton)ll_zfb.findViewById(R.id.rb_zfb)).setChecked(true);
                ((RadioButton)ll_wechat.findViewById(R.id.rb_wechat)).setChecked(false);
                break;
            case R.id.ll_wechat:
                ((RadioButton)ll_zfb.findViewById(R.id.rb_zfb)).setChecked(false);
                ((RadioButton)ll_wechat.findViewById(R.id.rb_wechat)).setChecked(true);
                break;
            case R.id.btn_submit:
                //选择好点击的那个按钮并调起微信支付或者是支付宝

                break;

        }
    }



    @Override
    public void setOnLeftClick() {
        finish();

    }



    @Override
    public void setOnRightClick() {


    }
}
