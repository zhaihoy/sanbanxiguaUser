package com.hbbc.mshare.sharer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.hbbc.R;
import com.hbbc.mshare.ProtocolActivity;
import com.hbbc.mshare.SignedOrderBean;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.mshare.sharer.login.SingnChargeOrderBean;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/11.
 */
public class ChargeActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener, View.OnTouchListener {

    private static final int SDK_PAY_FLAG = 101;

    private static final int FLAG_REAL_PAY_STATUS = 1001;

    private static final int RETRIEVE_SIGNED_ORDER = 1;

    private static final String TAG = "ChargeActivity";

    private WXPayResultReceiver receiver;

    private IWXAPI api;

    private LocalBroadcastManager localBroadcastManager;

    private HttpUtil httpUtil;

    private List<TextView> options = new ArrayList<>();

    private SignedOrderBean orderBean;

    private SingnChargeOrderBean chargeOrderBean;

    private LinearLayout ll_wechat;

    private LinearLayout ll_zfb;

    private RadioButton rbs_weixins;

    private RadioButton rbs_zfbs;

    private long lastSubmitTime;


    private ArrayList<Integer> money;

    private Button btns_submits;

    private String moneys;

    private String pay_type;

    private TextView tv_first;

    private TextView tv_second;

    private TextView tv_third;

    private TextView tv_forth;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharer_recharge);
        String s = SPUtils.getString("s");

        System.out.println("============="+s);
        initView();
        registerLocalReceiver();
        Log.d(TAG, "onCreate: 000000" + money);
    }



    protected void initView() {

        MyTopbar topbar = (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);

        tv_first = (TextView) findViewById(R.id.tv_firsts);

        tv_second = (TextView) findViewById(R.id.tv_seconds);
        tv_third = (TextView) findViewById(R.id.tv_thirds);
        tv_forth = (TextView) findViewById(R.id.tv_forths);

        rbs_weixins = (RadioButton) findViewById(R.id.rbs_weixins);
        rbs_zfbs = (RadioButton) findViewById(R.id.rbs_zfbs);
        btns_submits = (Button) findViewById(R.id.btns_submits);


        ll_wechat = (LinearLayout) findViewById(R.id.ll_weixin);
        ll_zfb = (LinearLayout) findViewById(R.id.ll_zfb);

        //这两个键不可响应点击
        rbs_zfbs.setEnabled(false);
        rbs_zfbs.setEnabled(false);
        if (SPUtils.getString("s") == null) {
        tv_first.setPressed(true);
        }
        ll_wechat.setOnClickListener(this);
        ll_zfb.setOnClickListener(this);
        btns_submits.setOnClickListener(this);
        options.add(tv_first);
        options.add(tv_second);
        options.add(tv_third);
        options.add(tv_forth);

        for (int i = 0; i < options.size(); i++) {
            options.get(i).setOnTouchListener(this);
        }
/*
        ll_wechat = (LinearLayout) findViewById(R.id.ll_weixin);
        ll_zfb = (LinearLayout) findViewById(R.id.ll_zfb);
        ll_wechat.setOnClickListener(this);
        ll_zfb.setOnClickListener(this);*/

        TextView tv_protocol = (TextView) findViewById(R.id.tv_charge_protocol);
        tv_protocol.setOnClickListener(this);
        btns_submits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submit(v);

               /* Toast.makeText(ChargeActivity.this, "哈哈", Toast.LENGTH_SHORT).show();*/
            }
        });
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
    public void finish() {

        super.finish();
        overridePendingTransition(R.anim.global_in, R.anim.global_out);
    }



    @Override
    public void onClick(View v) {


        super.onClick(v);

        switch (v.getId()) {
            case R.id.btn_submit:

            case R.id.ll_weixin:
                if (rbs_weixins.isChecked())
                    return;
                uncheckedAll();
                rbs_weixins.setChecked(true);
                break;
            case R.id.ll_zfb:
                if (rbs_zfbs.isChecked())
                    return;
                uncheckedAll();
                rbs_zfbs.setChecked(true);
                break;
            case R.id.tv_firsts:
            case R.id.tv_seconds:
            case R.id.tv_thirds:
            case R.id.tv_forths:
                ToastUtil.toast("点击了其中一个选项");
                System.out.println("==================" + tv_first.getText());
                modifySelection(v.getId());

                break;
            case R.id.tv_charge_protocol:
                Intent intent = new Intent(this, ProtocolActivity.class);
                intent.putExtra("protocol_type", 1);
                startActivity(intent);
                overridePendingTransition(R.anim.global_in, R.anim.global_out);
                break;
        }
    }



    private void submit(View v) {

        if (httpUtil == null) {
            httpUtil = new HttpUtil();
        }
        LoginResultBean query = UserDao.getDaoInstance(ChargeActivity.this).query();
        String phoneNumber = query.getPhoneNumber();

        String s = "";
        if (tv_first.isPressed()) {
            s = "100";
            Log.d(TAG, "submit: ===========1+" + s);
        } else if (tv_second.isPressed()) {
            s = "50";
            Log.d(TAG, "submit: ===========2+" + s);
        } else if (tv_third.isPressed()) {
            s = "20";
            Log.d(TAG, "submit: ===========3+" + s);
        }
        if (tv_forth.isPressed()) {
            s = "10";
            Log.d(TAG, "submit: ===========4+" + s);

        }
        SPUtils.putString("s", s);
      /* if (tv_first==options.get(size-1)){
            s="100";
           Log.d(TAG, "submit: 111111"+s);
       }else if (tv_forth==options.get(size))*/


      /*  money = new ArrayList<>();
        if (money.size() > 0) {
            Integer integer = money.get(money.size() - 1);
            moneys = String.valueOf(integer);
            Log.d(TAG, "submit: --------=======" + money);
        } else {
            Toast.makeText(ChargeActivity.this, "请选择金额", Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, "submit: ++++++++++++0" + money.size());*/


        {
            if (phoneNumber == null) {
                ToastUtil.toast("请先登陆");
                return;
            }
        }
        pay_type = null;
        if (rbs_weixins.isChecked()) {
            pay_type = getString(R.string.type_pay_weixin);
        } else if (rbs_zfbs.isChecked()) {
            pay_type = getString(R.string.type_pay_zfb);
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
                   /* ECID:100121,				//商户编号
                    AppID:24,				//app编号
                    AppType:2,				//app类型
                    PhoneNumber:15711459781,	//手机号码
                    RechargeAmount:0.01,			//充值金额
                    OpenID:"oEIaQ043Tr37Ers7LQwLAIoqy7RI",		//OpenID
                    PayType:1					//支付方式1、微信支付；2、支付宝支付*/
                Constants.ECID, GlobalConfig.ECID, Constants.AppID, GlobalConfig.AppID, Constants.AppType, GlobalConfig.AppType, Constants.PhoneNumber, phoneNumber, Constants.PayType, pay_type

                , Constants.RechargeAmount, s, Constants.OpenID, ""};

        httpUtil.callJson(handler, RETRIEVE_SIGNED_ORDER, this, GlobalConfig.SERVERROOTPATH + GlobalConfig.RECHARGEMONEY, SingnChargeOrderBean.class, params);
//        startActivity(new Intent(this, MainActivity.class));
    }



    @Override
    protected void getMessage(Message msg) {

        switch (msg.what) {
            case RETRIEVE_SIGNED_ORDER:
                //根据paytype区分是支付宝,还是微信支付
                chargeOrderBean = (SingnChargeOrderBean) msg.obj;
                if (chargeOrderBean.getResult()) {

                    if (pay_type.equals(getString(R.string.type_pay_weixin))) {
                        executeWXPayTask(msg);
                    } else if (pay_type.equals(getString(R.string.type_pay_zfb))) {
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
            PayResultUtil.openQueryService(this, orderBean, "DEPOSIT", com.hbbc.mshare.user.QueryPayResultService.class);
        } else {
            ToastUtil.toast("支付失败");
        }
    }



    /**
     * 调用SDK进行支付
     */
    private void executeWXPayTask(Message msg) {

        SingnChargeOrderBean orderBean = (SingnChargeOrderBean) msg.obj;
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
                    api = WXAPIFactory.createWXAPI(this, GlobalConfig.WEIXIN_SHARER_APP_ID);
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

        SingnChargeOrderBean orderBean = (SingnChargeOrderBean) msg.obj;

        if (orderBean != null && orderBean.getResult()) {
            final String signedOrder = orderBean.getOrderString();
            Log.d(TAG, "executeZFBPayTask: signed order--->" + signedOrder);
            //拿到订单信息后,开子线程进行支付
            Runnable r_payTask = new Runnable() {
                @Override
                public void run() {

                    PayTask payTask = new PayTask(ChargeActivity.this);
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

        rbs_weixins.setChecked(false);
        rbs_zfbs.setChecked(false);

    }



    @Override
    public void setOnLeftClick() {

        finish();
    }



    @Override
    public void setOnRightClick() {

    }



    /*
    * 選擇其中的一個金額進行充值
    * */
    public void modifySelection(int id) {

        for (int i = 0; i < options.size(); i++) {
            TextView option = options.get(i);
            if (option.getId() == id) {
                option.setPressed(true);

            } else {
//                option.setEnabled(false);
                option.setPressed(false);
            }

        }

    }
   /* @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_weixin:
            case R.id.ll_zfb:
                modifyChargeMethod(v.getId());
                break;
            case R.id.tv_first:
            case R.id.tv_second:
            case R.id.tv_third:
            case R.id.tv_forth:
                ToastUtil.toast("点击了其中一个选项");
                modifySelection(v.getId());
                break;
            case R.id.tv_charge_protocol:
                Intent intent = new Intent(this, ProtocolActivity.class);
                intent.putExtra("protocol_type", 1);
                startActivity(intent);
                overridePendingTransition(R.anim.global_in, R.anim.global_out);
                break;

        }
    }



    private void modifyChargeMethod(int id) {

        if (id == R.id.ll_weixin) {
            ll_wechat.getChildAt(3).setEnabled(true);
            ((RadioButton) ll_wechat.getChildAt(3)).setChecked(true);
            ((RadioButton) ll_zfb.getChildAt(3)).setChecked(false);
            ll_zfb.getChildAt(3).setEnabled(false);
        } else {
            ll_wechat.getChildAt(3).setEnabled(false);
            ((RadioButton) ll_wechat.getChildAt(3)).setChecked(false);
            ((RadioButton) ll_zfb.getChildAt(3)).setChecked(true);
            ll_zfb.getChildAt(3).setEnabled(true);
        }

    }
*/



    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {
            modifySelection(v.getId());

        }
        return true;
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



    //接收微信支付结果的广播
    public class WXPayResultReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int errCode = intent.getIntExtra(Constants.WEIXIN_PAY_RESULT, -1);
            //去服务器请求异步结果后再展示
//            ToastUtil.toast("去服务器请求异步结果!");

            if (errCode == 0) {
                //TODO 开启服务去服务器查询真实的支付结果,根据payOrderId:Wechat支付结果必须从此发起最终查询服务,才是有效的
                PayResultUtil.openQueryService(ChargeActivity.this, orderBean, "DEPOSIT", com.hbbc.mshare.user.QueryPayResultService.class);

            } else {
                ToastUtil.toast("支付失败");

            }


        }
    }
}
