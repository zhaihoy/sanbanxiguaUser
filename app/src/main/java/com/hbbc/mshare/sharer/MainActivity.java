package com.hbbc.mshare.sharer;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.hbbc.R;
import com.hbbc.mshare.order.OrderActivity;
import com.hbbc.mshare.wallet.WalletActivity;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.MyApplication;
import com.hbbc.util.MyTopbar;
import com.hbbc.util.ToastUtil;

/**
 * Created by Administrator on 2017/8/18.
 *
 */
public class MainActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener {

    private long prevMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharer_main);
        initView();
    }



    @Override
    protected void initView() {

        MyTopbar topbar = (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);

        /*if(GlobalConfig.AppName!=null){
            topbar.setTitle(GlobalConfig.AppName);
        }*/

        LinearLayout ll_add_items= (LinearLayout) findViewById(R.id.ll_add);
        LinearLayout ll_check_items= (LinearLayout) findViewById(R.id.ll_check_items);
        LinearLayout ll_check_orders= (LinearLayout) findViewById(R.id.ll_check_orders);
        LinearLayout ll_check_messages= (LinearLayout) findViewById(R.id.ll_check_wallet);

        ll_add_items.setOnClickListener(this);
        ll_check_items.setOnClickListener(this);
        ll_check_orders.setOnClickListener(this);
        ll_check_messages.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        super.onClick(v);
        Intent intent=new Intent();
        switch (v.getId()){//开启对应的页面,不关闭主页面
            case R.id.ll_add:
                intent.setClass(this,NewItemActivity.class);
                break;
            case R.id.ll_check_items:
                intent.setClass(this,MapActivity.class);
                break;
            case R.id.ll_check_orders:
                intent.setClass(this,OrderActivity.class);
                break;
            case R.id.ll_check_wallet:
                intent.setClass(this, WalletActivity.class);
                break;

        }
        startActivity(intent);
        overridePendingTransition(R.anim.global_in,0);
    }


    @Override
    public void setOnLeftClick() {
        startActivity(new Intent(this, UserActivity.class));
        overridePendingTransition(R.anim.global_in, 0);
    }


    @Override
    public void setOnRightClick() {

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(System.currentTimeMillis()-prevMillis<2000){
                MyApplication.getInstance().exit();
            }else{
                prevMillis = System.currentTimeMillis();
                ToastUtil.toast("再点一次退出应用");
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    public void finish() {

        super.finish();
        overridePendingTransition(R.anim.global_in,0);
    }
}
