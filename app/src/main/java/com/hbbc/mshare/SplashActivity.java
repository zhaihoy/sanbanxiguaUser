package com.hbbc.mshare;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.hbbc.R;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.mshare.user.main.MainActivity;
import com.hbbc.util.Constants;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyApplication;
import com.hbbc.util.ToastUtil;

/**
 * Created by Administrator on 2017/9/27.
 * SplashActivity
 * 把Activity中的多数任务放到这个activity中去实现
 * 自动登陆成功后,再从此跳到其他界面
 *
 * 客户端类型: 1.共享端(车主端) 2.用户端
 *
 */
public class SplashActivity extends Activity {

    private HttpUtil httpUtil;

    private static final int AUTOLOGIN = 0x0001;
    private static final int DELAYED = 0x0002;

    private static final String SharerApp = "1";
    private static final String UserApp = "2";

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case AUTOLOGIN:
                    LoginResultBean result = (LoginResultBean) msg.obj;
                    if (result == null||!result.getResult())//todo 怎么现在老是回到这里.
                    {

                        //TODO: 待优化逻辑,现在是遇到这个问题后直接就退出了应用,为什么会返回结果失败呢?
                        // 如果联网更新失败
//                        MyApplication.getInstance().exit();//
                        //在此处理一个用户在两个设备上先后登陆，先登陆的帐户自动取消并且重新登陆！
                        LoginResultBean user = UserDao.getDaoInstance(SplashActivity.this).query();
                        if(user != null){
                            user.setCurrentUser(false);
                            UserDao.getDaoInstance(SplashActivity.this).update(user);
                        }

                        Class cls = Integer.valueOf(GlobalConfig.AppType)==2 ? MainActivity.class : com.hbbc.mshare.sharer.login.LoginActivity.class;
                        startActivity(new Intent(SplashActivity.this, cls));
                        finish();
                        break;

                    }
                    Log.d("tag", "handleMessage: resultBean_in_Application===" + result.toString());
                    UserDao dao = UserDao.getDaoInstance(getApplicationContext());
                    LoginResultBean currentUser = dao.query();
                    result.setId(currentUser.getId());
                    GlobalConfig.AppName = result.getAppName();// TODO: 2017/10/31

                    result.setCurrentUser(true);// TODO: 2017/9/2 自动登陆的问题可能出现在这里
                    int rows = dao.update(result);//与服务器同步自动登陆用户的状态
                    ToastUtil.toast_debug("update_rows==" + rows);

                    Class cls;
                    if(GlobalConfig.AppType.equals(UserApp))
                    {
                        cls = com.hbbc.mshare.user.main.MainActivity.class;
                    }
                    else

                    {
                        if(result.getOpenCertification()==1 && result.getCertificationStatus()!=1)
                        {
                            //开启实名认证页面
                            cls = com.hbbc.mshare.sharer.authentication.AuthenticationActivity.class;
                        }
                        else if(result.getOpenDesposit()==1 && result.getDespositStatus()!=2)
                        {
                            //开启押金页面
                            cls = com.hbbc.mshare.sharer.deposit.DepositActivity.class;
                        }
                        else
                        {
                            //进入主页
                            cls = com.hbbc.mshare.sharer.MainActivity.class;
                        }
                    }
                    openActivity(cls);
//                    overridePendingTransition(R.anim.global_in,0);
                    finish();

                    break;
                case DELAYED:
                    AutoLogin();
                    break;
            }
            return true;
        }
    });

    private void openActivity(Class cls) {

        Intent intent = new Intent(SplashActivity.this, cls);
        ActivityOptions options = ActivityOptions.makeCustomAnimation(SplashActivity.this, R.anim.global_in,R.anim.global_out_left);
        startActivity(intent,options.toBundle());
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.mshare_network_fullscreen);
    }


    @Override
    public void finish() {

        super.finish();
        overridePendingTransition(0,0);
    }



    @Override
    protected void onResume() {

        super.onResume();


        if(!isNetworkAvailable(this))
        {
            setWifiAlertDialog(this);
        }
        else
        {
            handler.sendEmptyMessageDelayed(DELAYED,1000);//
        }

    }



    //// TODO: 2017/8/10 会有两个进程,那么他们会同时产生请求二次AutoLogin()方法 .可以优化成只请求一次!
    //默认直接自动登陆一次,同步一次默认登陆用户的登陆,认证,押金状态
    private void AutoLogin() {

        if (httpUtil == null)
            httpUtil = new HttpUtil();

        LoginResultBean result = UserDao.getDaoInstance(this).query();
        if (result == null)//如果当前没有默认要登陆的用户,即所有用户的isCurrentUser=false,不登陆!
        {
            //直接跳到登陆界面
            if(GlobalConfig.AppType.equals(UserApp))
            {
                openActivity(com.hbbc.mshare.user.main.MainActivity.class);
            }
            else
            {
                openActivity(com.hbbc.mshare.sharer.login.LoginActivity.class);
            }
            finish();
            return;
        }

        String[] params = new String[]{Constants.ECID, GlobalConfig.ECID,
                Constants.AppID,GlobalConfig.AppID,
                Constants.PhoneNumber, result.getPhoneNumber(), Constants.TempVerifyCode, "",
                Constants.AutoLoginKey, result.getAutoLoginKey(),
                Constants.AppType, GlobalConfig.AppType};
        Log.d("tag", "AutoLogin: params===" + params);

        httpUtil.callJson(handler, AUTOLOGIN, this,
                GlobalConfig.AppType.equals(UserApp)?(GlobalConfig.SERVERROOTPATH + GlobalConfig.MSHARE_LOGIN):(GlobalConfig.SERVERROOTPATH+GlobalConfig.SHARER_LOGIN_IN),
                LoginResultBean.class, params);//todo 特别注意这一点,不同客户端对应不同的自动登陆地址,别忘了!

    }

    /**
     * 检查联网情况（手机是否正常联网）。 eg：isNetworkAvailable(context);
     */
    public boolean isNetworkAvailable(Context context) {

        ConnectivityManager mgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 无网络时跳转到设置网络页面
     */
    public void setWifiAlertDialog(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("" + context.getResources().getString(R.string.app_name_user_watermelon));
        builder.setMessage("网络不可用，请设置网络！");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = null;
                /**
                 * 判断手机系统的版本！如果API大于10 就是3.0+
                 * 因为3.0以上的版本的设置和3.0以下的设置不一样，调用的方法不同
                 */
                if (android.os.Build.VERSION.SDK_INT > 10) {
                    intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                } else {
                    intent = new Intent();
                    ComponentName component = new ComponentName(
                            "com.android.settings",
                            "com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                context.startActivity(intent);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                MyApplication.getInstance().exit();
            }
        });
        builder.show();
    }
}
