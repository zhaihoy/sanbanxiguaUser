package com.hbbc.mshare.sharer.Bill;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.hbbc.R;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.MyTopbar;

/**
 * Created by Administrator on 2017/12/15.
 */
public class BilllActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener {


    private TabLayout bill_tab_layout;

    private ViewPager bill_vp;

    private BillAdapter billAdapter;

    private MyTopbar top_bar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.myuser_layout_bill);
         initView();
    }



    @Override
    protected void initView() {

        bill_tab_layout = (TabLayout) findViewById(R.id.bill_tab_layout);
        bill_vp = (ViewPager) findViewById(R.id.bill_vp);
        top_bar = (MyTopbar) findViewById(R.id.top_bar);
        top_bar.setOnTopBarClickListener(new MyTopbar.OnTopBarClickListener() {
            @Override
            public void setOnLeftClick() {
                finish();
            }


            @Override
            public void setOnRightClick() {

            }
        });
        //给ViewPage设置数据的适配器
        billAdapter = new BillAdapter(getSupportFragmentManager());
        bill_vp.setAdapter(billAdapter);
        //将ViewPage与TabLayout相关联
        bill_tab_layout.setupWithViewPager(bill_vp);
    }



    @Override
    public void setOnLeftClick() {

    }



    @Override
    public void setOnRightClick() {

    }
}
