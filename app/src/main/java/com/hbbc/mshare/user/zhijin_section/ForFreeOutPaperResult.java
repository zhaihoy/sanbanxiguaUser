package com.hbbc.mshare.user.zhijin_section;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.hbbc.R;
import com.hbbc.mshare.sharer.CustomerServiceActivity;
import com.hbbc.mshare.user.detail.Detail_Bean;
import com.hbbc.mshare.user.main.MainActivity;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.MyTopbar;
import com.hbbc.util.SPUtils;

/**
 * Created by Administrator on 2017/12/6.
 */
public class ForFreeOutPaperResult extends BaseActivity implements CompoundButton.OnCheckedChangeListener, MyTopbar.OnTopBarClickListener {


    private static final String TAG = "BuyPaperActivity";

    private TextView btn_free_result;

    private Detail_Bean detail_bean;

    private TextView tv_amount;

    private String paymoneys;



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
        setContentView(R.layout.myue_layout_buyresult);
        detail_bean = (Detail_Bean) getIntent().getSerializableExtra("detail_bean");
        paymoneys = SPUtils.getString("paymoneys");
        System.out.println("===============++======"+paymoneys);
        initViews(savedInstanceState);
    }



    //初始化布局
    private void initViews(Bundle savedInstanceState) {

        btn_free_result = (TextView) findViewById(R.id.btn_submit);
        tv_amount = (TextView) findViewById(R.id.tvs_amount);
        tv_amount.setText(paymoneys);
        MyTopbar topbar = (MyTopbar) findViewById(R.id.top_bar);
        if (topbar != null) {
            topbar.setOnTopBarClickListener(new MyTopbar.OnTopBarClickListener() {
                @Override
                public void setOnLeftClick() {
                    Intent intent = new Intent(ForFreeOutPaperResult.this, MainActivity.class);
                    startActivity(intent);
                }



                @Override
                public void setOnRightClick() {

                }
            });
        }
        btn_free_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击按钮跳转到疑问
                Intent intent = new Intent(ForFreeOutPaperResult.this, CustomerServiceActivity.class);
                startActivity(intent);
            }
        });


    }
}
