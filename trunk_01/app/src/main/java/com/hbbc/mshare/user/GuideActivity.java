package com.hbbc.mshare.user;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.hbbc.R;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.MyTopbar;

/**
 * Created by Administrator on 2017/7/19.
 *
 */
public class GuideActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_guide);
        initView();
    }


    @Override
    protected void initView() {

        MyTopbar topbar= (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);

        LinearLayout ll_deposit_intro= (LinearLayout) findViewById(R.id.ll_deposit_intro);
        LinearLayout ll_deposit_protocol= (LinearLayout) findViewById(R.id.ll_deposit_protocol);
        LinearLayout ll_location_failed= (LinearLayout) findViewById(R.id.ll_location_failed);
        LinearLayout ll_no_result= (LinearLayout) findViewById(R.id.ll_no_result);
        LinearLayout ll_all_questions= (LinearLayout) findViewById(R.id.ll_all_questions);

        ll_deposit_intro.setOnClickListener(this);
        ll_deposit_protocol.setOnClickListener(this);
        ll_location_failed.setOnClickListener(this);
        ll_no_result.setOnClickListener(this);
        ll_all_questions.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        super.onClick(v);
        switch (v.getId()){
            case R.id.ll_deposit_intro:

                break;
            case R.id.ll_deposit_protocol:

                break;
            case R.id.ll_location_failed:

                break;
            case R.id.ll_no_result:

                break;
            case R.id.ll_all_questions:

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
