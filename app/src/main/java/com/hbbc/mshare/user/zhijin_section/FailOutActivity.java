package com.hbbc.mshare.user.zhijin_section;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.hbbc.R;
import com.hbbc.mshare.user.detail.Detail_Bean;
import com.hbbc.mshare.user.main.MainActivity;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyTopbar;

/**
 * Created by Administrator on 2017/12/6.
 */
public class FailOutActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, MyTopbar.OnTopBarClickListener {

    private static final String TAG = "BuyPaperActivity";

    private HttpUtil httpUtil;

    private TextView btn_free_result;

    private Detail_Bean detail_bean;

    private TextView tv_amount;

    private String payMoneys;

    private ResultBeans resultBeans;

    private String payOrderID;



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }



    @Override
    public void setOnLeftClick() {
        Intent intent = new Intent(FailOutActivity.this, MainActivity.class);
        startActivity(intent);
    }



    @Override
    public void setOnRightClick() {

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.myue_layout_fail_outpaper);

        initViews(savedInstanceState);
    }




    //初始化布局
    private void initViews(Bundle savedInstanceState) {
        MyTopbar topbar = (MyTopbar) findViewById(R.id.top_bar);
        if (topbar != null) {
            topbar.setOnTopBarClickListener(new MyTopbar.OnTopBarClickListener() {
                @Override
                public void setOnLeftClick() {

                    Intent intent = new Intent(FailOutActivity.this, MainActivity.class);
                    startActivity(intent);
                }



                @Override
                public void setOnRightClick() {

                }
            });
        }

    }
}
