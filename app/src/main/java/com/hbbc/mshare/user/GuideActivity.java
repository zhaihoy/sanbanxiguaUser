package com.hbbc.mshare.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.hbbc.R;
import com.hbbc.mshare.ProtocolActivity;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.MyTopbar;

/**
 * Created by Administrator on 2017/7/19.
 */
public class GuideActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener {

    private static long prevTimemillis = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_guide);
        initView();
    }



    @Override
    protected void initView() {

        MyTopbar topbar = (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);

        LinearLayout ll_user_protocol = (LinearLayout) findViewById(R.id.ll_user_protocol);
        LinearLayout ll_refund_protocol = (LinearLayout) findViewById(R.id.ll_charge_protocol);
        LinearLayout ll_deposit_protocol = (LinearLayout) findViewById(R.id.ll_deposit_protocol);

        ll_user_protocol.setOnClickListener(this);
        ll_refund_protocol.setOnClickListener(this);
        ll_deposit_protocol.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if(System.currentTimeMillis()-prevTimemillis < 350){
            return;
        }
        prevTimemillis = System.currentTimeMillis();//
        Intent intent = new Intent(this, ProtocolActivity.class);
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_user_protocol:
                intent.putExtra("protocol_type", 0);
                break;
            case R.id.ll_charge_protocol:
                intent.putExtra("protocol_type", 1);
                break;
            case R.id.ll_deposit_protocol:
                intent.putExtra("protocol_type", 2);
                break;
        }
        startActivity(intent);
        overridePendingTransition(R.anim.global_in,R.anim.global_out);
    }



    @Override
    public void setOnLeftClick() {

        finish();
    }



    @Override
    public void setOnRightClick() {

    }
}
