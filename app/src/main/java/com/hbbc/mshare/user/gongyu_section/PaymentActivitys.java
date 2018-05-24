/*
package com.hbbc.mshare.user.gongyu_section;

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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.hbbc.R;
import com.hbbc.mshare.SignedOrderBean;
import com.hbbc.mshare.user.QueryPayResultService;
import com.hbbc.mshare.user.detail.Detail_Bean;
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

*/
/**
 * Created by Administrator on 2017/11/10.
 * <p/>
 * 调用SDK进行支付
 * <p/>
 * 调用SDK进行支付
 *//*

public class PaymentActivitys extends BaseActivity implements MyTopbar.OnTopBarClickListener {

    private static final int RETRIEVE_SIGNED_ORDER = 1;
    private static final String number="18519143836";
    private static final String TAG = "PaymentActivitys";

    private HttpUtil httpUtil;

    private TextView tv_amount;

    private TextView tv_desc;

    private RadioButton rb_weixin;

    private RadioButton rb_zfb;

    private Detail_Bean detail_bean;

    private LinearLayout ll_zfb;

    private LinearLayout ll_wechat;

    private  LinearLayout ll_yue;

    private long lastSubmitTime;

    private Button btn_submit;

    private SignedOrderBean orderBean;

    private static final int SDK_PAY_FLAG = 101;

    private static final int FLAG_REAL_PAY_STATUS = 1001;
    private IWXAPI api;

    private LocalBroadcastManager localBroadcastManager;

    private WXPayResultReceiver receiver;

    private RadioButton yue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_layout_payments);
        */
/*;*//*

        registerLocalReceiver();
        initView();
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

        MyTopbar topbar = (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);
        tv_amount = (TextView) findViewById(R.id.tv_amount);
       */
/* tv_desc = (TextView) findViewById(R.id.tv_desc);*//*

        rb_weixin = (RadioButton) findViewById(R.id.rb_weixin);
        rb_zfb = (RadioButton) findViewById(R.id.rb_zfb);
        yue = (RadioButton) findViewById(R.id.yue);
      */
/*  TextView tv_desc = (TextView) findViewById(R.id.tv_desc);
        ImageView imageView = (ImageView) findViewById(R.id.img);*//*

        btn_submit = (Button) findViewById(R.id.btn_submit);
*/
/*
        ll_zfb.setOnClickListener(this);
        ll_wechat.setOnClickListener(this);
        btn_submit.setOnClickListener(this);*//*

        LinearLayout ll_weixin = (LinearLayout) findViewById(R.id.ll_weixin);
        LinearLayout ll_zfb = (LinearLayout) findViewById(R.id.ll_zfb);
        LinearLayout ll_yue = (LinearLayout) findViewById(R.id.ll_yue);
        //这两个键不可响应点击
        rb_weixin.setEnabled(false);
        rb_zfb.setEnabled(false);
        yue.setEnabled(false);

        ll_weixin.setOnClickListener(this);
        ll_zfb.setOnClickListener(this);
        ll_yue.setOnClickListener(this);
        //它处理的优先级是最后的,要屏蔽掉radioButton的干扰,就必须要重写父窗口的拦截事件!
        btn_submit.setOnClickListener(this);

        if (detail_bean == null) return;
*/
/*
        Glide.with(this).load(detail_bean.getPicList().get(0)).placeholder(R.drawable.global_img_error).error(R.drawable.global_img_error).into(imageView);

        tv_desc.setText(detail_bean.getGoodsIntroduceText());
*//*

        tv_amount.setText(detail_bean.getGoodsDeposit());

    }



    */
/*
        @Override
        public void onClick(View v) {

            super.onClick(v);
            switch (v.getId()){
                case R.id.ll_zfb:
                    ((RadioButton)ll_zfb.findViewById(R.id.rb_zfb)).setChecked(true);
                    ((RadioButton)ll_wechat.findViewById(R.id.rb_wechat)).setChecked(false);
                    break;
                case R.id.ll_wechat:
                    ((RadioButton)ll_zfb.findViewById(R.id.rb_zfb)).setChecked(false);
                    ((RadioButton)ll_wechat.findViewById(R.id.rb_wechat)).setChecked(true);
                    break;
                case R.id.btn_submit:
                    //选择好点击的那个按钮并调起微信支付或者是支付宝
    submit();
                    break;

            }
        }*//*

    @Override
    public void onClick(View v) {

        Log.d(TAG, "onClick: this is onclick listener");
        super.onClick(v);
        //点击交纳支付金
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
            case R.id.ll_yue:
                if (yue.isChecked())
                    return;
                uncheckedAll();
                yue.setChecked(true);
                break;

            case R.id.btn_submit:
                if (System.currentTimeMillis() - lastSubmitTime > 5000L) {
//                    Toast.makeText(DepositActivity.this, "调用支付定金接口", Toast.LENGTH_SHORT).show();
                    submit();
                    lastSubmitTime = System.currentTimeMillis();
                }
                //TODO 交纳押金成功进入首页，并以通知形式通知用户，显示在业务通知里；
                break;
        }
    }



    private void uncheckedAll() {


        rb_weixin.setChecked(false);
        rb_zfb.setChecked(false);
        yue.setChecked(false);


    }



    */
/*
    * 调用起支付的接口
    * *//*

    private void submit() {

        {

            if (httpUtil == null) {
                httpUtil = new HttpUtil();
            }
         */
/*   LoginResultBean currentProcessingUser = UserDao.getDaoInstance(this).queryCurrentProcessingUser();
            String phoneNumber = currentProcessingUser.getPhoneNumber();
            Log.d(TAG, "submit: ---------"+phoneNumber);
            float deposit2Pay = currentProcessingUser.getPayDespositNum();//TODO 会不会出现,有,但是偶然查不到的情况
            Log.d(TAG, "submit: depositcount--->" + deposit2Pay);*//*

*/
/*
            if (phoneNumber == null) {
                ToastUtil.toast("请先登陆");
                return;
            }*//*

            String PAY_TYPE = null;
            if (rb_weixin.isChecked()) {
                PAY_TYPE = getString(R.string.type_pay_weixin);
            } else if (rb_zfb.isChecked()) {
                PAY_TYPE = getString(R.string.type_pay_zfb);
            }
            //请求访问网络
            String[] params = {
                    Constants.ECID, GlobalConfig.ECID, Constants.AppID, GlobalConfig.AppID, Constants.AppType, GlobalConfig.AppType, Constants.PhoneNumber,number , Constants.PayType, PAY_TYPE
                    , Constants.DespositCount, Constants.GoodsSNID,detail_bean.getGoodsSNID(),Constants.CurrentTime,"10" };
            httpUtil.callJson(handler, RETRIEVE_SIGNED_ORDER, this, GlobalConfig.MSHARE_SERVER_ROOT + GlobalConfig.ENDSTROKE, SignedOrderBean.class, params);

//        startActivity(new Intent(this, MainActivity.class));
        }


    }

    @Override
    protected void getMessage(Message msg) {

        switch (msg.what) {
            case RETRIEVE_SIGNED_ORDER:
                //根据paytype区分是支付宝,还是微信支付
                orderBean = (SignedOrderBean) msg.obj;
                if (orderBean.getResult()) {
                    String payType = orderBean.getPayType();
                    Log.d(TAG, "getMessage: payType===>" + payType);
                    if (payType.equals(getString(R.string.type_pay_weixin))) {
                        executeWXPayTask(msg);
                    } else if (payType.equals(getString(R.string.type_pay_zfb))) {
                        executeZFBPayTask(msg);
                    }
                }
                break;
            case SDK_PAY_FLAG:
                handleZFBPayResult(msg);
                break;
            case FLAG_REAL_PAY_STATUS:
                break;
        }
    }



    private void handleZFBPayResult(Message msg) {

        //支付状态：1、支付成功；2、支付取消；3、支付失败；4、准备支付；
        //如果同步返回结果为支付成功,才有必要去查询服务器确认最终支付结果
        //如果同步返回结果为支付失败,就没有必要去查询了

        PayResult payResult = new PayResult((Map<String, String>) msg.obj);

//        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
        String resultStatus = payResult.getResultStatus();
        // 判断resultStatus 为9000则代表支付成功
        if (TextUtils.equals(resultStatus, "9000")) {
            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
            PayResultUtil.openQueryService(this,orderBean,"DEPOSIT", QueryPayResultService.class);
        } else {
            ToastUtil.toast("支付失败");
        }
    }

    */
/**
 * 调用SDK进行支付
 *//*

    private void executeWXPayTask(Message msg) {

        SignedOrderBean orderBean = (SignedOrderBean) msg.obj;
        try {
            JSONObject json = new JSONObject(orderBean.getOrderString());
            if (null != json && !json.has("retcode")) {
                PayReq req = new PayReq();
                //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                req.appId = json.getString("appid");
                req.partnerId = json.getString("partnerid");
                req.prepayId = json.getString("prepayid");
                req.nonceStr = json.getString("noncestr");
                req.timeStamp = json.getString("timestamp");
                req.packageValue = json.getString("package");
                req.sign = json.getString("sign");
//                req.extData			= "app data"; // optional
//                Toast.makeText(this, "正常调起支付", Toast.LENGTH_SHORT).show();
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                if (api == null) {
                    api = WXAPIFactory.createWXAPI(this, GlobalConfig.WEIXIN_USER_APP_ID);
                }
                api.sendReq(req);
            } else {
                Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
                ToastUtil.toast_debug("返回错误" + json.getString("retmsg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



    */
/**
 * 调用SDK进行支付
 *//*

    private void executeZFBPayTask(Message msg) {

        SignedOrderBean orderBean = (SignedOrderBean) msg.obj;

        if (orderBean != null && orderBean.getResult()) {
            final String signedOrder = orderBean.getOrderString();
            Log.d(TAG, "executeZFBPayTask: signed order--->" + signedOrder);
            //拿到订单信息后,开子线程进行支付
            Runnable r_payTask = new Runnable() {
                @Override
                public void run() {

                    PayTask payTask = new PayTask(PaymentActivitys.this);
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





    //接收微信支付结果的广播
    public class WXPayResultReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int errCode = intent.getIntExtra(Constants.WEIXIN_PAY_RESULT, -1);
            //去服务器请求异步结果后再展示
//            ToastUtil.toast("去服务器请求异步结果!");

            if(errCode==0){
                //TODO 开启服务去服务器查询真实的支付结果,根据payOrderId:Wechat支付结果必须从此发起最终查询服务,才是有效的
                PayResultUtil.openQueryService(PaymentActivitys.this,orderBean,"DEPOSIT", QueryPayResultService.class);

            }else{
                ToastUtil.toast("支付失败");

            }


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
*/
package com.hbbc.mshare.user.gongyu_section;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.hbbc.R;
import com.hbbc.mshare.SignedOrderBean;
import com.hbbc.mshare.detail.Detail_Bean;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.mshare.sharer.QueryPayResultService;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.Constants;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyTopbar;
import com.hbbc.util.PayResult;
import com.hbbc.util.PayResultUtil;
import com.hbbc.util.SPUtils;
import com.hbbc.util.ToastUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/14.
 */
public class PaymentActivitys extends BaseActivity implements CompoundButton.OnCheckedChangeListener, MyTopbar.OnTopBarClickListener {

    private static final int RETRIEVE_SIGNED_ORDER = 1;

    private static final int SDK_PAY_FLAG = 101;

    private static final int FLAG_REAL_PAY_STATUS = 1001;

    private static final String TAG = "DepositActivity";

    private RadioButton rb_weixin;

    private RadioButton rb_zfb;

    private Button btn_submit;

    private MyTopbar topbar;

    private TextView tvs_amount;

    private long lastSubmitTime;

    private HttpUtil httpUtil;

    private IWXAPI api;

    private LocalBroadcastManager localBroadcastManager;

    private WXPayResultReceiver receiver;

    private SignedOrderBean orderBean;

    private RadioButton yue;

    private Detail_Bean detail_bean;

    private com.hbbc.mshare.endStroke_bean endStroke_bean;

    private float orderMoney;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_layout_payments);
        registerLocalReceiver();
        detail_bean = (Detail_Bean) getIntent().getSerializableExtra("detail_bean");
        Log.d(TAG, "onCreate: oio" + detail_bean);
        endStroke_bean = (com.hbbc.mshare.endStroke_bean) getIntent().getSerializableExtra("endStroke_bean");
        Log.d(TAG, "onCreate: Love" + detail_bean);
        orderMoney = endStroke_bean.getOrderMoney();
        SPUtils.putString("orderMoney", String.valueOf(orderMoney));
        initView();

    }



    private void registerLocalReceiver() {

        if (localBroadcastManager == null) {
            localBroadcastManager = LocalBroadcastManager.getInstance(this);
        }
        receiver = new WXPayResultReceiver();
        IntentFilter intentFilter = new IntentFilter(getString(R.string.filter_weixin_receiver));
        localBroadcastManager.registerReceiver(receiver, intentFilter);
    }



    //判断当前登陆用户的押金支付状态,决定是否进入下一步:主页面.
    @Override
    protected void onResume() {

        super.onResume();

    }



    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (localBroadcastManager != null)
            localBroadcastManager.unregisterReceiver(receiver);

    }



    @Override
    protected void initView() {

        topbar = (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);

        rb_weixin = (RadioButton) findViewById(R.id.rb_weixin);
        rb_zfb = (RadioButton) findViewById(R.id.rb_zfb);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        tvs_amount = (TextView) findViewById(R.id.tvs_amount);
        yue = (RadioButton) findViewById(R.id.yue);

        LinearLayout ll_weixin = (LinearLayout) findViewById(R.id.ll_weixin);
        LinearLayout ll_zfb = (LinearLayout) findViewById(R.id.ll_zfb);
        LinearLayout ll_yue = (LinearLayout) findViewById(R.id.ll_yue);
        //这两个键不可响应点击
        rb_weixin.setEnabled(false);
        rb_zfb.setEnabled(false);
        yue.setEnabled(false);


        ll_weixin.setOnClickListener(this);
        ll_zfb.setOnClickListener(this);
        ll_yue.setOnClickListener(this);
        //它处理的优先级是最后的,要屏蔽掉radioButton的干扰,就必须要重写父窗口的拦截事件!

        btn_submit.setOnClickListener(this);
        //显示需要支付的押金金额
        float deposit2Pay = UserDao.getDaoInstance(this).query().getPayDespositNum();
        tvs_amount.setText(String.valueOf(orderMoney) + "元");
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
            case R.id.ll_yue:
                if (yue.isChecked())
                    return;
                uncheckedAll();
                yue.setChecked(true);
                break;

            case R.id.btn_submit:
                if (System.currentTimeMillis() - lastSubmitTime > 5000L) {
//                    Toast.makeText(DepositActivity.this, "调用支付定金接口", Toast.LENGTH_SHORT).show();
                    submit();
                    lastSubmitTime = System.currentTimeMillis();
                }

                break;
        }
    }



    /**
     * todo:调用支付接口
     */
    private void submit() {

        if (httpUtil == null) {
            httpUtil = new HttpUtil();
        }
        LoginResultBean query = UserDao.getDaoInstance(PaymentActivitys.this).query();
        String phoneNumber = query.getPhoneNumber();
        float deposit2Pay = query.getPayDespositNum();
        //TODO 会不会出现,有,但是偶然查不到的情况
        Log.d(TAG, "submit: depositcount--->" + deposit2Pay);

        if (phoneNumber == null) {
            ToastUtil.toast("请先登陆");
            return;
        }
        String PAY_TYPE = null;
        if (rb_weixin.isChecked()) {
            PAY_TYPE = getString(R.string.type_pay_weixin);
        } else if (rb_zfb.isChecked()) {
            PAY_TYPE = getString(R.string.type_pay_zfb);
        } else if (yue.isChecked()) {
            PAY_TYPE = getString(R.string.type_pay_yue);
        }
       /* String[] params = {
                Constants.ECID, GlobalConfig.ECID,  Constants.AppID,GlobalConfig.AppID,Constants.AppType, GlobalConfig.AppType,  Constants.PayType, PAY_TYPE
                , Constants.OpenID ,"" };
        httpUtil.callJson(handler, RETRIEVE_SIGNED_ORDER, this, GlobalConfig.MSHARE_SERVER_ROOT + GlobalConfig.PAYBIKEORDERMONEY, SignedOrderBean.class, params);
*/
        /*
        这里使用的时用的支付押金的接口
        * */
        String[] params = {
               /* ECID:100101,				// 商户编号
                AppID:21,				//  app编号
                AppType:2,				// 客户端类型：1、共享端；2、用户端
                PhoneNumber:18210386513,// 手机号码
                GoodsSNID:1000225,		// 物品编码
                PayType:1,			//支付类型，1、微信支付；2、支付宝支付；3、余额支付
                OrderMoney:0.01,		//支付金额
                OpenID：			//用于微信公众号支付*/
                Constants.ECID, GlobalConfig.ECID, Constants.AppID, GlobalConfig.AppID, Constants.AppType, GlobalConfig.AppType, Constants.PhoneNumber, phoneNumber, Constants.GoodsSNID, endStroke_bean.getGoodsSNID(), Constants.PayType, PAY_TYPE

                , Constants.OrderMoney, String.valueOf(orderMoney), Constants.OpenID, ""};
        Log.d(TAG, "submit: orderMoney++++" + endStroke_bean.getGoodsSNID());
        httpUtil.callJson(handler, RETRIEVE_SIGNED_ORDER, this, GlobalConfig.SERVERROOTPATH + GlobalConfig.PAYBIKEORDERMONEY, SignedOrderBean.class, params);
//        startActivity(new Intent(this, MainActivity.class));
    }



    @Override
    protected void getMessage(Message msg) {

        switch (msg.what) {
            case RETRIEVE_SIGNED_ORDER:
                //根据paytype区分是支付宝,还是微信支付
                orderBean = (SignedOrderBean) msg.obj;
                if (orderBean.getResult()) {
                    String payType = orderBean.getPayType();
                    Log.d(TAG, "getMessage: payType===>" + payType);
                    if (payType.equals(getString(R.string.type_pay_weixin))) {
                        executeWXPayTask(msg);
                    } else if (payType.equals(getString(R.string.type_pay_zfb))) {
                        executeZFBPayTask(msg);
                    } else if (payType.equals(getString(R.string.type_pay_yue))) {
                        //执行余额的支付
                      executeYuEPayTask();
                    }
                }
                break;
            case SDK_PAY_FLAG:
                handleZFBPayResult(msg);
                break;
            case FLAG_REAL_PAY_STATUS:
                break;
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


            if (keyCode == KeyEvent.KEYCODE_BACK
                    && event.getRepeatCount() == 0) {
                ToastUtil.toast("zeee");
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }




    private void executeYuEPayTask() {

        //支付成功跑这个方法
        if (endStroke_bean.getResult()) {
            Intent intent = new Intent(this, PaySuccess.class);
            intent = intent.putExtra("endStroke_bean", endStroke_bean);
            startActivity(intent);
        } else {
            //支付失败跑这个
            ToastUtil.toast("支付失败");
        }
    }



    private void handleZFBPayResult(Message msg) {

        //支付状态：1、支付成功；2、支付取消；3、支付失败；4、准备支付；
        //如果同步返回结果为支付成功,才有必要去查询服务器确认最终支付结果
        //如果同步返回结果为支付失败,就没有必要去查询了

        PayResult payResult = new PayResult((Map<String, String>) msg.obj);

//        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
        String resultStatus = payResult.getResultStatus();
        // 判断resultStatus 为9000则代表支付成功
        if (TextUtils.equals(resultStatus, "9000")) {
            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
            PayResultUtil.openQueryService(this, orderBean, "DEPOSIT", QueryPayResultService.class);
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
            if (null != json && !json.has("retcode")) {
                PayReq req = new PayReq();
                //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                req.appId = json.getString("appid");
                req.partnerId = json.getString("partnerid");
                req.prepayId = json.getString("prepayid");
                req.nonceStr = json.getString("noncestr");
                req.timeStamp = json.getString("timestamp");
                req.packageValue = json.getString("package");
                req.sign = json.getString("sign");
//                req.extData			= "app data"; // optional
//                Toast.makeText(this, "正常调起支付", Toast.LENGTH_SHORT).show();
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                if (api == null) {
                    api = WXAPIFactory.createWXAPI(this, GlobalConfig.WEIXIN_USER_APP_ID);
                }
                api.sendReq(req);
            } else {
                Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
                ToastUtil.toast_debug("返回错误" + json.getString("retmsg"));
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

                    PayTask payTask = new PayTask(PaymentActivitys.this);
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
        yue.setChecked(false);
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

            if (errCode == 0) {
                //TODO 开启服务去服务器查询真实的支付结果,根据payOrderId:Wechat支付结果必须从此发起最终查询服务,才是有效的
                PayResultUtil.openQueryService(PaymentActivitys.this, orderBean, "ORDERPAYMENT", QueryPayResultService.class);

            } else {
                ToastUtil.toast("支付失败");

            }


        }
    }

}
