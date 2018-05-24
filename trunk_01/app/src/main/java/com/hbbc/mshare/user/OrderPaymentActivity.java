package com.hbbc.mshare.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.hbbc.R;
import com.hbbc.mshare.SignedOrderBean;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.mshare.user.detail.Detail_Bean;
import com.hbbc.mshare.user.main.MainActivity;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.Constants;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyTopbar;
import com.hbbc.util.PayResult;
import com.hbbc.util.ToastUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/17.
 */
public class OrderPaymentActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener {

    private static final String TAG = "OrderPaymentActivity";


    private static final int ZFB = 0;

    private static final int WEIXIN = 1;

    private static final int THIRDPARTY = 2;

    private static final int RETRIEVE_ORDER = 101;

    private static final int SDK_PAY_FLAG = 1001;


    private LinearLayout ll_zfb;

    private LinearLayout ll_weiXin;

    private LinearLayout ll_thirdParty;

    private Detail_Bean detail_bean;

    private long lastSubmitTime;//记录上一次点击支付定金按钮的时间,此字段用于防抖动.

    private HttpUtil httpUtil;

    private IWXAPI api;



    @Override
    protected void getMessage(Message msg) {

        switch (msg.what)
        {
            case RETRIEVE_ORDER:
                SignedOrderBean orderbean = (SignedOrderBean) msg.obj;
                if(!orderbean.getResult()){
                    ToastUtil.toast("操作超时请重试");
                    return;
                }
                String payType = orderbean.getPayType();
                String order = orderbean.getOrderString();
                if(payType.equals("1"))
                {
                    executeWeiXinPayment(order);
                }
                else if(payType.equals("2"))
                {
                    executeZFBPayment(order);

                }else if(payType.equals(3))
                {
                    executeThirdPartyPayment(order);
                }

                break;
            case SDK_PAY_FLAG:
                handleZFBPayResult(msg);
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
            startActivity(new Intent(this, MainActivity.class));
            Toast.makeText(OrderPaymentActivity.this, "预定成功", Toast.LENGTH_LONG).show();
            finish();

            //todo 向服务器拉取真实的支付结果,并更新数据库信息

        } else {
            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
            Toast.makeText(OrderPaymentActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
        }

    }



    /**
     * 第三方金融支付,暂时不用
     */
    private void executeThirdPartyPayment(String order) {


    }



    /**
     * 支付宝支付
     */
    private void executeZFBPayment(final String order) {
        if (order!=null) {
            Log.d(TAG, "executeZFBPayTask: signed order--->" + order);
            //拿到订单信息后,开子线程进行支付
            Runnable r_payTask = new Runnable() {
                @Override
                public void run() {

                    PayTask payTask = new PayTask(OrderPaymentActivity.this);
                    Map<String, String> resultMap = payTask.payV2(order, true);
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



    /**
     * 微信支付
     */
    private void executeWeiXinPayment(String order) {
        try {
            JSONObject json = new JSONObject(order);
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
                    api = WXAPIFactory.createWXAPI(this,GlobalConfig.WEIXIN_APP_ID);
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_order_payment);
        detail_bean = (Detail_Bean) getIntent().getSerializableExtra("detail_bean");
        initView();
    }



    @Override
    protected void initView() {

        MyTopbar topbar = (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);

        ll_zfb = (LinearLayout) findViewById(R.id.ll_zfb);
        ll_weiXin = (LinearLayout) findViewById(R.id.ll_weixin);
        ll_thirdParty = (LinearLayout) findViewById(R.id.ll_third_party);


        ll_zfb.setOnClickListener(this);
        ll_weiXin.setOnClickListener(this);
        ll_thirdParty.setOnClickListener(this);

        ImageView iv_img = (ImageView) findViewById(R.id.iv_img);
        TextView tv_price = (TextView) findViewById(R.id.tv_price);
        TextView tv_amount = (TextView) findViewById(R.id.tv_amount);
        TextView tv_desc= (TextView) findViewById(R.id.tv_desc);
        Glide.with(this).load(detail_bean.getPicList().get(0)).placeholder(R.drawable.ic_launcher).into(iv_img);

        tv_price.setText(String.valueOf(detail_bean.getGoodsUsePrice()));
        tv_amount.setText(String.valueOf(detail_bean.getGoodsDeposit()));
        tv_desc.setText(detail_bean.getGoodsIntroduceText());


        Button btn_pay = (Button) findViewById(R.id.btn_pay);
        btn_pay.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            Detail_Bean bean = (Detail_Bean) intent.getSerializableExtra("detail_bean");
//            if(bean!=null)
                // TODO: 2017/8/1 以后,统一将error/placeHolder图片进行替换成指定图片.
//                Glide.with(this).load(bean.getGoodsIntroducePic1FileID()).error(R.drawable.test_with_background)
//                        .placeholder(R.drawable.test_with_background).into(iv_img);
        }

    }



    @Override
    public void onClick(View v) {

        super.onClick(v);

        switch (v.getId()) {
            case R.id.ll_zfb:
                enable(ZFB);
                break;
            case R.id.ll_weixin:
                enable(WEIXIN);
                break;
            case R.id.ll_third_party:
                enable(THIRDPARTY);
                break;
            case R.id.btn_pay:
                // TODO: 2017/7/17 调用具体的支付接口进行支付
                // 按钮防抖动
                if(System.currentTimeMillis()-lastSubmitTime>5000L) {//暂定为5000L,网络框架延时最长是5000L
                    processOrder();
                    lastSubmitTime = System.currentTimeMillis();
                }else{
                    ToastUtil.toast("请勿重复点击");
                }
                break;
        }

    }



    /**
     * 调用,物品预约接口，产生订单
     * ”ECID”:100001							// 商户编号
     ,“GoodsSNID”: “10000001”		       		// 物品编号
     ,”PhoneNumber”:”13113131313”				// 用户手机号
     //,”ShareID”:”1”							//共享模块ID		(目前不用)
     , “PayType:”1”			  //支付类型：1、微信支付；2、支付宝支付；3、第三方金融；
     ，OrderAmount：”10”  //订单金额：单位(元)
     */
    private void processOrder() {
        LoginResultBean user = UserDao.getDaoInstance(this).query();
        if(user==null)
        {
            ToastUtil.toast("请先登陆");
            return;
        }
        String phoneNumber = user.getPhoneNumber();
        if(phoneNumber==null)
        {
            ToastUtil.toast("操作超时");
            return;
        }

        if(httpUtil==null)
        {
            httpUtil = new HttpUtil();
        }

        String payType=null;
        if(ll_zfb.getChildAt(2).isEnabled())
        {
            payType = "2";
        }else if(ll_weiXin.getChildAt(2).isEnabled())
        {
            payType = "1";
        }else if(ll_thirdParty.getChildAt(2).isEnabled())
        {
            payType = "3";
        }

        if(payType == null)
        {
            ToastUtil.toast("请先选择支付方式");
            return;
        }
        // TODO: 2017/9/22  暂时屏蔽第三方金融支付
        if(payType.equals("3"))
        {
            ToastUtil.toast("暂不支付该支付方式");
            return;
        }

        String[] params = new String[]{Constants.ECID,GlobalConfig.ECID,Constants.GoodsSNID,detail_bean.getGoodsSNID()
                            ,Constants.PhoneNumber,phoneNumber,Constants.PayType,payType,Constants.OrderAmount,detail_bean.getGoodsUsePrice()};
        httpUtil.callJson(handler,RETRIEVE_ORDER,this, GlobalConfig.MSHARE_SERVER_ROOT+GlobalConfig.MSHARE_RESERVE_SELECTED_PRODUCT
                            , SignedOrderBean.class,params);

    }



    //
    private void disableAll() {

        ll_zfb.getChildAt(2).setEnabled(false);
        ll_weiXin.getChildAt(2).setEnabled(false);
        ll_thirdParty.getChildAt(2).setEnabled(false);
    }



    private void enable(int position) {

        disableAll();
        switch (position) {
            case ZFB:
                ll_zfb.getChildAt(2).setEnabled(true);
                break;
            case WEIXIN:
                ll_weiXin.getChildAt(2).setEnabled(true);
                break;
            case THIRDPARTY:
                ll_thirdParty.getChildAt(2).setEnabled(true);
                break;
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
