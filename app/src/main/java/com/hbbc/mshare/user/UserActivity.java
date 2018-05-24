package com.hbbc.mshare.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbbc.R;
import com.hbbc.mshare.AboutUsActivity;
import com.hbbc.mshare.CustomerServiceActivity;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.mshare.message.MessageActivity;
import com.hbbc.mshare.order.OrderActivity;
import com.hbbc.mshare.wallet.WalletActivity;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.Constants;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyTopbar;
import com.hbbc.util.ToastUtil;

/**
 * Created by Administrator on 2017/7/5.
 */
public class UserActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener {

    private static final int REQUESTWATER_BEAN = 7;

    private HttpUtil httpUtil;
    private TextView tv_name;
    private static long prevTimemillis = 0;

    private UserBean userBean;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_user);
        initView();


    }



    @Override
    protected void initView() {

        MyTopbar topbar = (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);
        ImageView iv_user = (ImageView) findViewById(R.id.iv_user);
        tv_name = (TextView) findViewById(R.id.tv_name);
        findViewById(R.id.btn_sign_out).setOnClickListener(this);

        LinearLayout ll_guide = (LinearLayout) findViewById(R.id.ll_guide);
        LinearLayout ll_message = (LinearLayout) findViewById(R.id.ll_message);
        LinearLayout ll_contact = (LinearLayout) findViewById(R.id.ll_contact);
        LinearLayout ll_about_us = (LinearLayout) findViewById(R.id.ll_about_us);

        LinearLayout ll_wallet = (LinearLayout) findViewById(R.id.ll_wallet);
        LinearLayout ll_order = (LinearLayout) findViewById(R.id.ll_order);

        ll_guide.setOnClickListener(this);
        ll_message.setOnClickListener(this);
        ll_contact.setOnClickListener(this);
        ll_about_us.setOnClickListener(this);
        iv_user.setOnClickListener(this);
        ll_wallet.setOnClickListener(this);
        ll_order.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {

        super.onClick(v);
        if(System.currentTimeMillis()-prevTimemillis < GlobalConfig.Button_Press_Interval){
            return;
        }

        prevTimemillis = System.currentTimeMillis();

        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_user://更换头像
                if (UserDao.getDaoInstance(this).query() != null)
                {
                    changeUserIcon();
                  /*  startActivity(intent);
                    overridePendingTransition(R.anim.global_in, 0);*/
                }
                else
                {
                    intent.setClass(this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.global_in,R.anim.global_out);
                }
                return;
            case R.id.ll_guide:
                intent.setClass(this, GuideActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.global_in, 0);
                break;
            case R.id.ll_message:
                intent.setClass(this, MessageActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.global_in, 0);
                break;
            case R.id.ll_contact:
                intent.setClass(this, CustomerServiceActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.global_in, 0);
                break;
            case R.id.ll_about_us:
                intent.setClass(this, AboutUsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.global_in, 0);
                break;
            case R.id.btn_sign_out://登出
                 signOut();

                overridePendingTransition(R.anim.global_in, 0);
                break;
            case R.id.ll_wallet:
/*
                intent.setClass(this, WalletActivity.class);
*/
                //请求网络
                requestNet();
                setOnSubmitRequestListener(new SubmitRequestListener() {
                    @Override
                    public void onSubmitRequestSuccess() {

                        Intent intent = new Intent(UserActivity.this, WalletActivity.class);
                        intent.putExtra("UserBean", userBean);
                        startActivity(intent);
                        overridePendingTransition(R.anim.global_in, 0);
                    }
                });

                break;
            case R.id.ll_order:
                intent.setClass(this,OrderActivity.class);
                Log.d("tag", "onClick: clicked ");
                startActivity(intent);
                overridePendingTransition(R.anim.global_in, 0);
                break;
        }




    }



    private void requestNet() {

        //// TODO: 2017/12/7
        if (httpUtil == null) {
            httpUtil = new HttpUtil();
        }

        LoginResultBean query = UserDao.getDaoInstance(UserActivity.this).query();
        String phoneNumber = query.getPhoneNumber();

        String[] params = new String[]{Constants.ECID, GlobalConfig.ECID, Constants.PhoneNumber, phoneNumber,  Constants.AppType, GlobalConfig.AppType,Constants.AppID, GlobalConfig.AppID,
        };
        httpUtil.callJson(handler, REQUESTWATER_BEAN, this, GlobalConfig.SERVERROOTPATH + GlobalConfig.USERWALLET, UserBean.class, params);

    }
    public SubmitRequestListener submitRequestListener;
    public interface SubmitRequestListener {
        void onSubmitRequestSuccess();
    }
    public void setOnSubmitRequestListener(SubmitRequestListener submitRequestListener) {
        this.submitRequestListener = submitRequestListener;
    }



    @Override
    protected void getMessage(Message msg) {

        switch (msg.what) {
            case REQUESTWATER_BEAN:
                userBean = (UserBean) msg.obj;
                submitRequestListener.onSubmitRequestSuccess();
                super.getMessage(msg);
        }
    }



    @Override
    protected void onDestroy() {

        super.onDestroy();
        overridePendingTransition(R.anim.global_in,R.anim.global_out);
    }



    @Override
    public void finish() {

        super.finish();
        overridePendingTransition(R.anim.global_in,R.anim.global_out);
    }



    /**
     * 登出操作
     */
    private void signOut() {

        final UserDao dao = UserDao.getDaoInstance(this);
        LoginResultBean currentUser = dao.query();
        if (currentUser == null) {
            ToastUtil.toast("你还未登陆");
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("真的要注销当前用户吗?")
                    .setNegativeButton("再想想", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("确认退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dao.resetAllUsersStatus();
                            tv_name.setText(queryCurrentUserName());
                            ToastUtil.toast("注销成功");
                            finish();

                        }
                    })
                    .setCancelable(false)
                    .show();


        }
    }



    @Override
    protected void onResume() {

        super.onResume();
        tv_name.setText(queryCurrentUserName());
    }



    /**
     * 改变用户头像
     */
    private void changeUserIcon() {

        ToastUtil.toast("改变头像");

    }



    @Override
    public void setOnLeftClick() {

        finish();
    }



    @Override
    public void setOnRightClick() {

    }



    /**
     * 查询当前用户的手机号绑定到tv_name
     */
    private String queryCurrentUserName() {

        LoginResultBean currentUser = UserDao.getDaoInstance(this).query();
        if (currentUser == null || TextUtils.isEmpty(currentUser.getPhoneNumber()))
            return "点击此处登陆";
        else
            return currentUser.getPhoneNumber();
    }


}
