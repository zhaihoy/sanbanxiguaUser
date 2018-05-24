package com.hbbc.mshare.message;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.hbbc.R;
import com.hbbc.util.MyLinearLayoutWithoutSelector;
import com.hbbc.util.MyTopbar;

/**
 * Created by Administrator on 2017/7/21.
 *
 */
public class MessageActivity extends FragmentActivity implements MyTopbar.OnTopBarClickListener, View.OnClickListener {

    private FrameLayout fl_container;//存放系统消息与业务消息对应Fragment的Container

    private MessageFragment systemMessageFragment;

    private MessageFragment businessMessageFragment;

    private MyLinearLayoutWithoutSelector ll_system;

    private MyLinearLayoutWithoutSelector ll_business;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_message);
        initView();
    }

    /**
     * TextView的局限性所在,想单独控制一个drawable不易实现
     */
    protected void initView() {

        MyTopbar topbar = (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);

        ll_system = (MyLinearLayoutWithoutSelector) findViewById(R.id.ll_system);
        ll_business = (MyLinearLayoutWithoutSelector) findViewById(R.id.ll_business);

        ll_system.setOnClickListener(this);
        ll_business.setOnClickListener(this);

        ll_system.setEnabled(false);
        ll_business.setEnabled(true);

        fl_container = (FrameLayout) findViewById(R.id.fl_container);
        initFragment();

    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    /**
     * 2个fragment对象用来切换界面
     */
    private void initFragment() {

        systemMessageFragment = new MessageFragment();
        businessMessageFragment = new MessageFragment();

        Bundle args = new Bundle();
        args.putInt("Adapter_Type", 0);
        systemMessageFragment.setArguments(args);

        Bundle args1 = new Bundle();
        args.putInt("Adapter_Type", 1);
        businessMessageFragment.setArguments(args1);

        if (fl_container.getChildCount() == 0) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_container, systemMessageFragment)
                    .add(R.id.fl_container, businessMessageFragment)
                    .hide(businessMessageFragment)
                    .show(systemMessageFragment)
                    .commit();
        }
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_system:
                enableBusinessFragment();
                showSystemMessageFragment();
                break;
            case R.id.ll_business:
                enableSystemFragment();
                showBusinessFragment();
                break;

        }
    }



    private void showBusinessFragment() {
        getSupportFragmentManager().beginTransaction()
                .show(businessMessageFragment)
                .hide(systemMessageFragment)
                .commit();

    }



    private void showSystemMessageFragment() {
        getSupportFragmentManager().beginTransaction()
                .show(systemMessageFragment)
                .hide(businessMessageFragment)
                .commit();

    }



    private void enableSystemFragment() {

        ll_system.setEnabled(true);
        ll_business.setEnabled(false);

        ll_system.getChildAt(0).setEnabled(true);
        ll_system.getChildAt(1).setVisibility(View.INVISIBLE);

        ll_business.getChildAt(0).setEnabled(false);
        ll_business.getChildAt(1).setVisibility(View.VISIBLE);

    }



    private void enableBusinessFragment() {

        ll_system.setEnabled(false);
        ll_business.setEnabled(true);

        ll_system.getChildAt(0).setEnabled(false);
        ll_system.getChildAt(1).setVisibility(View.VISIBLE);

        ll_business.getChildAt(0).setEnabled(true);
        ll_business.getChildAt(1).setVisibility(View.INVISIBLE);
    }



    @Override
    public void setOnLeftClick() {

        finish();
    }



    @Override
    public void finish() {

        super.finish();
        overridePendingTransition(0, R.anim.global_out);
    }



    @Override
    public void setOnRightClick() {

    }
}
