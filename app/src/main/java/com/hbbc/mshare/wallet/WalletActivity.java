package com.hbbc.mshare.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbbc.R;
import com.hbbc.mshare.login.BaseResultBean;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.mshare.sharer.BillActivity;
import com.hbbc.mshare.sharer.ChargeActivity;
import com.hbbc.mshare.sharer.UserActivity;
import com.hbbc.mshare.sharer.WithDrawActivity;
import com.hbbc.mshare.sharer.authentication.BillBean;
import com.hbbc.mshare.sharer.login.WithDrawActivitys;
import com.hbbc.mshare.user.UserBean;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.Constants;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyTopbar;

import com.hbbc.mshare.sharer.Bill.BilllActivity;

/**
 * Created by Administrator on 2017/10/11.
 */
public class WalletActivity extends BaseActivity implements View.OnClickListener, MyTopbar.OnTopBarClickListener {

    private static final String TAG = "WalletActivity";

    private static final int REQUESTBILL_BEAN = 6;

    private static long prevTimemillis = 0;

    public SubmitRequestListener submitRequestListener;

    private HttpUtil httpUtil;

    private LinearLayout ll_user;

    private LinearLayout ll_bill;

    private LinearLayout ll_recharge;

    private LinearLayout ll_withdraw;

    private LinearLayout ll_deposit_send_back;

    private TextView tv_name;

    private TextView tv_user;

    private TextView tv_balance;

    private BaseResultBean resultBean;

   /* private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            resultBean = (BaseResultBean) msg.obj;
            if (resultBean.getResult()) {
                if (Integer.valueOf(GlobalConfig.AppType) == 2) {
                    UserWalletBean userWalletBean = (UserWalletBean) resultBean;
                    tv_name.setText(userWalletBean.getUserName());
                    tv_balance.setText(userWalletBean.getUserBalance() + "元");

                } else {
                    SharerWalletBean sharerWalletBean = (SharerWalletBean) resultBean;
                    tv_name.setText(sharerWalletBean.getSharerName());
                    tv_balance.setText(sharerWalletBean.getSharerBalance() + "元");

                }
            }
            return true;
        }
    });
*/
    private BillBean billBean;

    private UserBean userBean;

    private String userName;

    private String userBalance;

    private String depositBalance;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_wallet);
        userBean = (UserBean) getIntent().getSerializableExtra("UserBean");
        userName = userBean.getUserName();
        depositBalance = userBean.getUserBalance();
        Log.d(TAG, "onCreate: +++"+userBean.getUserBalance());
        initView();
        getUserWalletInfo();
    }



    protected void initView() {

        MyTopbar topbar = (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);
        ll_user = (LinearLayout) findViewById(R.id.ll_user);
        ll_bill = (LinearLayout) findViewById(R.id.ll_bill);
        ll_recharge = (LinearLayout) findViewById(R.id.ll_recharge);
        ll_withdraw = (LinearLayout) findViewById(R.id.ll_withdraw);
        ll_deposit_send_back = (LinearLayout) findViewById(R.id.ll_request_deposit_send_back);

        ll_user.setOnClickListener(this);
        ll_bill.setOnClickListener(this);
        ll_recharge.setOnClickListener(this);
        ll_withdraw.setOnClickListener(this);
        ll_deposit_send_back.setOnClickListener(this);

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_user = (TextView) findViewById(R.id.tv_user);
        tv_balance = (TextView) findViewById(R.id.tv_balance);
        tv_name.setText(userName);
        tv_balance.setText(depositBalance+"元");

    }



    public void getUserWalletInfo() {

        LoginResultBean user = UserDao.getDaoInstance(this).query();
        String phoneNumber = null;
        if (user != null) {
            phoneNumber = user.getPhoneNumber();
        }
        if (phoneNumber == null) {
            return;
        }
        tv_user.setText(phoneNumber);
        String url = GlobalConfig.MSHARE_SERVER_ROOT + ((Integer.valueOf(GlobalConfig.AppType) == 2) ? GlobalConfig.USER_WALLET_INFO : GlobalConfig.SHARER_USER_WALLET_INFO);
        String[] params = {Constants.ECID, GlobalConfig.ECID, Constants.PhoneNumber, phoneNumber, Constants.AppType, GlobalConfig.AppType, Constants.AppID, GlobalConfig.AppID};
        Class cls = Integer.valueOf(GlobalConfig.AppType) == 2 ? UserWalletBean.class : SharerWalletBean.class;
        new HttpUtil().callJson(handler, this, url, cls, params);//UserWalletBean

    }



    /*
    * 接受来自于网络请求的类型
    *
    * */
    @Override
    protected void getMessage(Message msg) {

        switch (msg.what) {
            case REQUESTBILL_BEAN:
                billBean = (BillBean) msg.obj;
                System.out.println(billBean);
                submitRequestListener.onSubmitRequestSuccess();
                super.getMessage(msg);
        }
    }

    @Override
    public void onClick(View v) {//TODO 对于这种多种重复按钮,如何更好的防抖动
        if (System.currentTimeMillis() - prevTimemillis < GlobalConfig.Button_Press_Interval) {
            return;
        }
        prevTimemillis = System.currentTimeMillis();

        int withdraw_type = -1;
        Class cls = null;
        switch (v.getId()) {
            case R.id.ll_user:
                cls = GlobalConfig.AppType.equals("1") ? UserActivity.class : com.hbbc.mshare.user.UserActivity.class;
                break;
            case R.id.ll_bill:
                cls = BilllActivity.class;
                break;
            case R.id.ll_balance://无响应
                break;
            case R.id.ll_recharge:
                cls = ChargeActivity.class;
                break;
            case R.id.ll_withdraw:
                cls = WithDrawActivity.class;
                withdraw_type = 1;
                break;
            case R.id.ll_request_deposit_send_back:
                cls = WithDrawActivitys.class;
                withdraw_type = 2;
                break;

        }
        /*判断是不是从billActivity*/
        if (cls == BillActivity.class) {

            //请求网络
            requestNet();
            //将返回的网络的Bean传递到BillActivity
            setOnSubmitRequestListener(new SubmitRequestListener() {
                @Override
                public void onSubmitRequestSuccess() {

                    Intent intent = new Intent(WalletActivity.this, BillActivity.class);
                    intent.putExtra("BillBean", billBean);
                    startActivity(intent);
                }
            });

        }
        else if (cls==WithDrawActivity.class){
            Intent intent = new Intent(WalletActivity.this, WithDrawActivity.class);
            intent.putExtra("UserBean", userBean);
            Log.d(TAG, "onClick: ++++++++"+userBean);
            startActivity(intent);

        }else {

            Intent intent = new Intent(this, cls);
            if (withdraw_type != -1) {
                intent.putExtra("withdraw_type", withdraw_type);
            }

            startActivity(intent, ActivityOptionsCompat.makeCustomAnimation(this, R.anim.global_in, R.anim.global_out).toBundle());
        }
    }



    private void requestNet() {
        //// TODO: 2017/9/1
        if (httpUtil == null) {
            httpUtil = new HttpUtil();
        }

        LoginResultBean query = UserDao.getDaoInstance(WalletActivity.this).query();
        String phoneNumber = query.getPhoneNumber();
        String[] params = new String[]{Constants.ECID, GlobalConfig.ECID, Constants.AppID, GlobalConfig.AppID, Constants.AppType, GlobalConfig.AppType, Constants.PhoneNumber, phoneNumber,
        };
        httpUtil.callJson(handler, REQUESTBILL_BEAN, this, GlobalConfig.SERVERROOTPATH + GlobalConfig.BILL, BillBean.class, params);

    }



    public void setOnSubmitRequestListener(SubmitRequestListener submitRequestListener) {

        this.submitRequestListener = submitRequestListener;
    }



    @Override
    public void finish() {

        super.finish();
        overridePendingTransition(R.anim.global_in, R.anim.global_out);
    }



    @Override
    protected void onDestroy() {

        super.onDestroy();
        overridePendingTransition(R.anim.global_in, R.anim.global_out);
    }



    @Override
    public void setOnLeftClick() {

        finish();
    }



    @Override
    public void setOnRightClick() {

    }



    public interface SubmitRequestListener {

        void onSubmitRequestSuccess();
    }
}
