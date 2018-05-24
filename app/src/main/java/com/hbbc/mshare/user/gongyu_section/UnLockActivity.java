package com.hbbc.mshare.user.gongyu_section;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.hbbc.R;
import com.hbbc.mshare.user.detail.Detail_Bean;
import com.hbbc.mshare.user.main.MainActivity;
import com.hbbc.util.BaseActivity;

/**
 * Created by Administrator on 2017/11/14.
 *
 */
public class UnLockActivity extends BaseActivity {

    private static final String TAG = "UnLockActivity";

    private Detail_Bean detail_bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.gongyu_unlock);
        handler.sendEmptyMessageDelayed(0,3000);
        detail_bean = (com.hbbc.mshare.user.detail.Detail_Bean) getIntent().getSerializableExtra("detail_bean");

    }


    @Override
    protected void getMessage(Message msg) {

        Intent intent= new Intent();
        intent.setClass(this,MainActivity.class);



     intent.putExtra("detail_bean",detail_bean);
        Log.d(TAG, "getMessage: 000000000"+detail_bean);
        startActivity(intent) ;
        //单车传递对象的值
       /* Bundle bundle = new Bundle();
        bundle.putSerializable("detail_bean",detail_bean);
        Log.d(TAG, "getMessage: 000000000"+detail_bean);
        /*//*intent.putExtra("detail_bean",detail_bean);*//**//*
        intent.putExtras(bundle);
        startActivity(intent);*/

        startActivity(intent);
        finish();

    }
}
