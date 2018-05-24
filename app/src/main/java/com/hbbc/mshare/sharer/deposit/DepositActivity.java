package com.hbbc.mshare.sharer.deposit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.hbbc.R;
import com.hbbc.mshare.SignedOrderBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.mshare.sharer.QueryPayResultService;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.Constants;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyTopbar;
import com.hbbc.util.PayResult;
import com.hbbc.util.PayResultUtil;
import com.hbbc.util.ToastUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/14.
 *
 */
public class DepositActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, MyTopbar.OnTopBarClickListener {

    private static final int SDK_PAY_FLAG = 1;

    private static final int RETRIEVE_SIGNED_ORDER = 2;

    private static final String TAG = "DepositActivity";

    private RadioButton rb_weixin;

    private RadioButton rb_zfb;

    private Button btn_deposit;

    private MyTopbar topbar;

    private TextView tv_amount;

    private long lastSubmitTime;

    private HttpUtil httpUtil;

    private IWXAPI api;

    private SignedOrderBean orderBean;

    private LocalBroadcastManager localBroadcastManager;

    private WXPayResultReceiver receiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_deposit);
        registerLocalReceiver();
        initView();

    }



    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (localBroadcastManager != null)
            localBroadcastManager.unregisterReceiver(receiver);
    }



    private void registerLocalReceiver() {

        if (localBroadcastManager == null) {
            localBroadcastManager = LocalBroadcastManager.getInstance(this);
        }
        receiver = new WXPayResultReceiver();
        IntentFilter intentFilter = new IntentFilter(getString(R.string.filter_weixin_receiver));
        localBroadcastManager.registerReceiver(receiver, intentFilter);
    }



    @Override
    protected void initView() {

        topbar = (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);

        rb_weixin = (RadioButton) findViewById(R.id.rb_weixin);
        rb_zfb = (RadioButton) findViewById(R.id.rb_zfb);
        btn_deposit = (Button) findViewById(R.id.btn_deposit);
        tv_amount = (TextView) findViewById(R.id.tv_amount);

        LinearLayout ll_weixin = (LinearLayout) findViewById(R.id.ll_weixin);
        LinearLayout ll_zfb = (LinearLayout) findViewById(R.id.ll_zfb);
        //这两个键不可响应点击
        rb_weixin.setEnabled(false);
        rb_zfb.setEnabled(false);

        ll_weixin.setOnClickListener(this);
        ll_zfb.setOnClickListener(this);//它处理的优先级是最后的,要屏蔽掉radioButton的干扰,就必须要重写父窗口的拦截事件!

        btn_deposit.setOnClickListener(this);
        //显示需要支付的押金金额
        float deposit2Pay = UserDao.getDaoInstance(this).query().getPayDespositNum();
        tv_amount.setText(String.valueOf(deposit2Pay) + "元");

    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        int id = buttonView.getId();
        Log.d("deposit", "onCheckedChanged: id=" + id);
        buttonView.setChecked(false);
    }



    @Override
    public void onClick(View v) {

        Log.d(TAG, "onClick: this is onclick listener");
        super.onClick(v);
        //点击交纳押金
        switch (v.getId()) {
            case R.id.ll_weixin:
                if (rb_weixin.isChecked())
                    return;
                uncheckedAll();
                rb_weixin.setChecked(true);
                break;
            case R.id.ll_zfb:
                if (rb_zfb.isChecked())
                    return;
                uncheckedAll();
                rb_zfb.setChecked(true);
                break;

            case R.id.btn_deposit:
                if (System.currentTimeMillis() - lastSubmitTime > 5000L) {
                    processOrder();
                    lastSubmitTime = System.currentTimeMillis();
                }
                //TODO 交纳押金成功进入首页，并以通知形式通知用户，显示在业务通知里；
                break;
        }
    }



    /**
     * todo:调用支付接口
     */
    private void processOrder() {

        if (httpUtil == null) {
            httpUtil = new HttpUtil();
        }
        String phoneNumber = UserDao.getDaoInstance(this).query().getPhoneNumber();
        float deposit2Pay = UserDao.getDaoInstance(this).query().getPayDespositNum();//TODO 会不会出现,有,但是偶然查不到的情况
        Log.d(TAG, "processOrder: depositcount--->" + deposit2Pay);
        if (phoneNumber == null) {
            ToastUtil.toast("请先登陆");
            return;
        }
        String PAY_TYPE = null;
        if (rb_weixin.isChecked()) {
            PAY_TYPE = getString(R.string.type_pay_weixin);
        } else if (rb_zfb.isChecked()) {
            PAY_TYPE = getString(R.string.type_pay_zfb);
        }
        String[] params = {Constants.AppID,GlobalConfig.AppID,Constants.ECID, GlobalConfig.ECID, Constants.PhoneNumber, phoneNumber, Constants.AppType, GlobalConfig.AppType
                , Constants.PayType, PAY_TYPE, Constants.DespositCount, String.valueOf(deposit2Pay), Constants.OpenID,""};
        httpUtil.callJson(handler, RETRIEVE_SIGNED_ORDER, this, GlobalConfig.SERVERROOTPATH + GlobalConfig.MSHARE_PAY_DEPOSIT, SignedOrderBean.class, params);
    }



    @Override
    protected void getMessage(Message msg) {

        switch (msg.what) {
            case SDK_PAY_FLAG:
                handleZFBPayResult(msg);
                break;
            case RETRIEVE_SIGNED_ORDER:
                //根据paytype区分是支付宝,还是微信支付
                orderBean = (SignedOrderBean) msg.obj;
                if (orderBean.getResult()) {
                    String payType = orderBean.getPayType();
                    Log.d(TAG, "getMessage: payType===>"+payType);
                    if (payType.equals(getString(R.string.type_pay_weixin))) {
                        executeWXPayTask(msg);
                    } else if (payType.equals(getString(R.string.type_pay_zfb))) {
                        executeZFBPayTask(msg);
                    }
                }

                break;
        }
    }

    private void handleZFBPayResult(Message msg) {

        PayResult payResult = new PayResult((Map<String, String>) msg.obj);
        /**
         对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
         */
        //TODO ...
        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
        Log.d(TAG, "handleZFBPayResult: resultinfo--->" + resultInfo);
        String resultStatus = payResult.getResultStatus();
        // 判断resultStatus 为9000则代表支付成功
        if (TextUtils.equals(resultStatus, "9000")) {
            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
            //开启后台服务,向服务器拉取真实的支付结果,并更新数据库信息
            PayResultUtil.openQueryService(this,orderBean, "DEPOSIT", QueryPayResultService.class);

        } else {
            ToastUtil.toast("支付失败");
        }

    }


    /**
     * 调用SDK进行支付
     */
    private void executeWXPayTask(Message msg) {

        SignedOrderBean orderBean = (SignedOrderBean) msg.obj;
        try {
            JSONObject json = new JSONObject(orderBean.getOrderString());
            if(null != json && !json.has("retcode") ){
                PayReq req = new PayReq();
                //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                req.appId			= json.getString("appid");
                req.partnerId		= json.getString("partnerid");
                req.prepayId		= json.getString("prepayid");
                req.nonceStr		= json.getString("noncestr");
                req.timeStamp		= json.getString("timestamp");
                req.packageValue	= json.getString("package");
                req.sign			= json.getString("sign");
//                req.extData			= "app data"; // optional
//                Toast.makeText(this, "正常调起支付", Toast.LENGTH_SHORT).show();
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                if(api==null){
                    api = WXAPIFactory.createWXAPI(this,GlobalConfig.WEIXIN_USER_APP_ID);
                }
                api.sendReq(req);
            }else{
                Log.d("PAY_GET", "返回错误"+json.getString("retmsg"));
                Toast.makeText(this, "返回错误"+json.getString("retmsg"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * 调用SDK进行支付
     */
    private void executeZFBPayTask(Message msg) {

        SignedOrderBean orderBean = (SignedOrderBean) msg.obj;

        if (orderBean != null && orderBean.getResult()) {
            final String signedOrder = orderBean.getOrderString();
            Log.d(TAG, "executeZFBPayTask: signed order--->" + signedOrder);

            //拿到订单信息后,开子线程进行支付
            Runnable r_payTask = new Runnable() {
                @Override
                public void run() {

                    PayTask payTask = new PayTask(DepositActivity.this);
                    Map<String, String> resultMap = payTask.payV2(signedOrder, true);
                    Message m = new Message();
                    m.what = SDK_PAY_FLAG;
                    m.obj = resultMap;
                    handler.sendMessage(m);

                }
            };

            new Thread(r_payTask).start();

        } else {
            ToastUtil.toast("操作超时,请重试");
        }

    }



    private void uncheckedAll() {

        rb_weixin.setChecked(false);
        rb_zfb.setChecked(false);
    }



    @Override
    public void setOnLeftClick() {

        finish();
    }



    @Override
    public void setOnRightClick() {

    }

    //接收微信支付结果的广播
    public class WXPayResultReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int errCode = intent.getIntExtra(Constants.WEIXIN_PAY_RESULT, -1);
            //去服务器请求异步结果后再展示
//            ToastUtil.toast("去服务器请求异步结果!");

            if(errCode==0){
                //TODO 开启服务去服务器查询真实的支付结果,根据payOrderId:Wechat支付结果必须从此发起最终查询服务,才是有效的
                PayResultUtil.openQueryService(DepositActivity.this,orderBean, "DEPOSIT", QueryPayResultService.class);

            }else{
                ToastUtil.toast("支付失败");

            }


        }
    }
}
