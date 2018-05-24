package com.hbbc.pgynandroid.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.hbbc.R;
import com.hbbc.util.Constants;
import com.hbbc.util.GlobalConfig;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by Administrator on 2017/9/19.
 * 共享端接收微信支付的结果接口
 *
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler, View.OnClickListener {


    private IWXAPI api;
    private LocalBroadcastManager localBroadcastManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
//        setContentView(R.layout.mshare_weixin_pay_result_layout);
        api = WXAPIFactory.createWXAPI(this, GlobalConfig.WEIXIN_USER_APP_ID);
        api.handleIntent(getIntent(),this);//必须加上//接收微信请求的Intent

//        Button btn_confirm = (Button) findViewById(R.id.btn_confirm);
//        btn_confirm.setOnClickListener(this);

    }



    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent,this);

    }



    @Override
    public void onReq(BaseReq baseReq) {

    }


    //第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //在此处理支付结果
    @Override
    public void onResp(BaseResp resp) {
        Log.d("tag", "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

//            TextView tv_msg = (TextView) findViewById(R.id.tv);
//            tv_msg.setText("支付"+ (resp.errCode==0?"成功!":"失败!"));
            // TODO: 2017/9/22 组件间通信:本地广播来实现
            if(localBroadcastManager ==null)
            {
                localBroadcastManager = LocalBroadcastManager.getInstance(this);
            }
            Intent intent = new Intent(getString(R.string.filter_weixin_receiver));
            intent.putExtra(Constants.WEIXIN_PAY_RESULT,resp.errCode);
            localBroadcastManager.sendBroadcast(intent);
            finish();
        }
    }



    @Override
    public void onClick(View v) {
        //判断是否支付成功了: 成功:关闭押金页面,进入主页 ,失败,留在当前押金支付页面并弹出提示
        finish();
    }
}
