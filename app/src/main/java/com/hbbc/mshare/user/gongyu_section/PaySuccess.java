package com.hbbc.mshare.user.gongyu_section;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.hbbc.R;
import com.hbbc.mshare.sharer.CustomerServiceActivity;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.MyTopbar;
import com.hbbc.util.SPUtils;

/**
 * Created by Administrator on 2017/12/6.
 */
public class PaySuccess extends BaseActivity implements CompoundButton.OnCheckedChangeListener, MyTopbar.OnTopBarClickListener {


    private static final String TAG = "PaySuccess";

//    private com.hbbc.mshare.endStroke_bean endStroke_bean;

    private TextView tvs_amount;

    private String orderMoney;

    private Button btn_submit;



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }



    @Override
    public void setOnLeftClick() {

    }



    @Override
    public void setOnRightClick() {

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.myue_layout_paysuccess);
//        endStroke_bean = (com.hbbc.mshare.endStroke_bean) getIntent().getSerializableExtra("endStroke_bean");
//        orderMoney = endStroke_bean.getOrderMoney();
//        Log.d(TAG, "onCreate: +++++sss" + endStroke_bean);
        orderMoney = SPUtils.getString("orderMoney");
        initViews(savedInstanceState);
    }



    //初始化布局
    private void initViews(Bundle savedInstanceState) {

        tvs_amount = (TextView) findViewById(R.id.tvs_amount);
        tvs_amount.setText(orderMoney);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        MyTopbar topbar = (MyTopbar) findViewById(R.id.top_bar);
        if (topbar != null) {
            topbar.setOnTopBarClickListener(new MyTopbar.OnTopBarClickListener() {
                @Override
                public void setOnLeftClick() {

                    Intent intent = new Intent(PaySuccess.this, com.hbbc.mshare.user.main.MainActivity.class);
                    startActivity(intent);
                    finish();

                }



                @Override
                public void setOnRightClick() {

                }
            });
        }
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击按钮跳转到疑问
                Intent intent = new Intent(PaySuccess.this, CustomerServiceActivity.class);
                startActivity(intent);
            }
        });

    }

}
