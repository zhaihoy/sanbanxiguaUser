package com.hbbc.mshare.user.zhijin_section;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.hbbc.R;
import com.hbbc.mshare.SignedOrderBean;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.mshare.sharer.QueryPayResultService;
import com.hbbc.mshare.user.UserBean;
import com.hbbc.mshare.user.detail.Detail_Bean;
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
 * Created by Administrator on 2017/11/10.
 */
public class ReBuyPaperActivity extends BaseActivity {

    private static final String TAG = "PaymentActivity";

    private static final int RETRIEVE_SIGNED_ORDER = 1;

    private static final int SDK_PAY_FLAG = 101;

    private static final int FLAG_REAL_PAY_STATUS = 1001;

    private LocalBroadcastManager localBroadcastManager;

    private TextView tv_amount;

    private WXPayResultReceiver receiver;

    private IWXAPI api;

    private TextView tv_desc;

    private Detail_Bean detail_bean;

    private HttpUtil httpUtil;

    private RadioButton yue;

    private RadioButton rb_weixin;

    private RadioButton rb_zfb;

    private SignedOrderBean orderBean;

    private ImageView imageView;

    private long lastSubmitTime;

    private String goodsDeposit;

    private Button btn_submits;

    private String goodsUsePrice;

    private UserBean userBean;

    private String userBalance;

    private String paymoneys;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_layout_buypaper);
        detail_bean = (Detail_Bean) getIntent().getSerializableExtra("detail_bean");
        paymoneys = detail_bean.getGoodsUsePrice();
        System.out.println("paymoneys" + paymoneys);
        SPUtils.putString("paymoneys", paymoneys);
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
        assert topbar != null;
        topbar.setOnTopBarClickListener(new MyTopbar.OnTopBarClickListener() {
            @Override
            public void setOnLeftClick() {

                finish();
            }



            @Override
            public void setOnRightClick() {

            }
        });
        tv_amount = (TextView) findViewById(R.id.tv_amount);
        yue = (RadioButton) findViewById(R.id.yue);
        rb_weixin = (RadioButton) findViewById(R.id.rb_weixin);
        rb_zfb = (RadioButton) findViewById(R.id.rb_zfb);
        btn_submits = (Button) findViewById(R.id.sbtns_submits);
        btn_submits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (System.currentTimeMillis() - lastSubmitTime > 5000L) {

                    submit();
                    lastSubmitTime = System.currentTimeMillis();
                }
            }
        });

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
        tv_amount.setText(paymoneys);

    }



    public void onClick(View v) {


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

        }

    }



    /**
     * todo:调用支付接口
     */
    private void submit() {

        if (httpUtil == null) {
            httpUtil = new HttpUtil();
        }
        LoginResultBean query = UserDao.getDaoInstance(ReBuyPaperActivity.this).query();

        String phoneNumber = query.getPhoneNumber();
        float deposit2Pay = query.getPayDespositNum();
        //TODO 会不会出现,有,但是偶然查不到的情况
        Log.d(TAG, "submit: depositcount--->" + deposit2Pay);

        if (phoneNumber == null) {
            ToastUtil.toast("请先登陆");
            return;
        }
        String ORDER_TYPE = null;
        String PAY_TYPE = null;
        if (rb_weixin.isChecked()) {
            PAY_TYPE = getString(R.string.type_pay_weixin);
            ORDER_TYPE = "1";
        } else if (rb_zfb.isChecked()) {
            PAY_TYPE = getString(R.string.type_pay_zfb);
            ORDER_TYPE = "1";
        } else if (yue.isChecked()) {
            PAY_TYPE = getString(R.string.type_pay_yue);
            ORDER_TYPE = "1";
        }

       /* {
            ECID:100123,		// 商户编号
                    AppID:26,				//  app编号
                AppType:2,				// 客户端类型：1、共享端；2、用户端
                PhoneNumber:18210386513,// 手机号码
                GoodsSNID:1000203,		// 物品编码
                PayMoney:0.01,			//押金加费用
                PayType:1			//支付类型，1、微信支付；2、支付宝支付；3、余额支付
            ，：			//用于微信公众号支付
        }*/

       /* 这里使用的时用的支付押金的接口 */
        String[] params = {
                Constants.ECID, GlobalConfig.ECID, Constants.AppID, GlobalConfig.AppID, Constants.AppType, GlobalConfig.AppType, Constants.PayType, PAY_TYPE, Constants.PhoneNumber, phoneNumber, Constants.GoodsSNID, detail_bean.getGoodsSNID(), Constants.payMoney, paymoneys
                , Constants.OrderType, ORDER_TYPE, Constants.OpenID, ""};
        httpUtil.callJson(handler, RETRIEVE_SIGNED_ORDER, this, GlobalConfig.SERVERROOTPATH + GlobalConfig.BUY_PAPER, SignedOrderBean.class, params);

    }



    @Override
    protected void getMessage(Message msg) {

        switch (msg.what) {
            case RETRIEVE_SIGNED_ORDER:
                //根据paytype区分是支付宝,还是微信支付
                orderBean = (SignedOrderBean) msg.obj;
                if (orderBean.getResult()) {
                    String payType = orderBean.getPayType();
                    String payOrderID = orderBean.getPayOrderID();
                    System.out.println("payOrderId+-+_+===" + payOrderID);
                    //订单号存起来
                    SPUtils.putString("payOrderId", payOrderID);
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



    private void executeYuEPayTask() {
        //支付成功跑这个方法
        if (orderBean.getResult()) {
            //如果支付成功的话就走开锁界面如果支付失败的话就弹出对话框提示（暂时不写这一个逻辑）
            Intent intent = new Intent(this, OutPaperResultProcess.class);

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
            PayResultUtil.openQueryService(this, orderBean, "ORDERPAYMENT", QueryPayResultService.class);
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

                    PayTask payTask = new PayTask(ReBuyPaperActivity.this);
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

    //接收微信支付结果的广播
    public class WXPayResultReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int errCode = intent.getIntExtra(Constants.WEIXIN_PAY_RESULT, -1);
            //去服务器请求异步结果后再展示
//            ToastUtil.toast("去服务器请求异步结果!");

            System.out.println("eeeeeeeeeee" + errCode);
            if (errCode == 0) {
                //TODO 开启服务去服务器查询真实的支付结果,根据payOrderId:Wechat支付结果必须从此发起最终查询服务,才是有效的
                PayResultUtil.openQueryService(ReBuyPaperActivity.this, orderBean, "ORDERPAYMENT", QueryPayResultService.class);

            } else {
                ToastUtil.toast("支付失败");

            }


        }
    }


}
