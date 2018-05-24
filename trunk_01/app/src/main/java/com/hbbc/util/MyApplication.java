package com.hbbc.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hbbc.mpush.GetuiUtil;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * 应用程序入口，全程都可调用
 * eg：DisplayImageOptions option=MyApplication.getInstance().getOptions();
 */
public class MyApplication extends Application {

    private static final int AUTOLOGIN = 0x0001;

    private static MyApplication app;

    public static MyApplication getInstance() {
        return app;
    }

    private HttpUtil httpUtil;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case AUTOLOGIN:
                    LoginResultBean result= (LoginResultBean) msg.obj;
                    if(result==null)
                        break;
                    Log.d("tag", "handleMessage: resultBean_in_Application==="+result.toString());
                    UserDao dao=UserDao.getDaoInstance(MyApplication.this);
                    LoginResultBean currentUser = dao.query();
                    result.setId(currentUser.getId());

                    result.setCurrentUser(true);// TODO: 2017/9/2 自动登陆的问题可能出现在这里
                    int rows = dao.update(result);//与服务器同步自动登陆用户的状态
                    ToastUtil.toast_debug("update_rows=="+rows);
                    break;
            }
            return true;
        }
    });

    private List<Activity> activityList;

    @Override
    public void onCreate() {
        super.onCreate();
//        if(LeakCanary.isInAnalyzerProcess(this)){
//            return;
//        }
//
//        LeakCanary.install(this);

        //TODO 判断是否网,如果没有网,就不要进来了!

        activityList = Collections.synchronizedList(new ArrayList<Activity>());//保存打开的所有activity

        //初始化HttpUtils
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout(10000L, TimeUnit.MICROSECONDS)
                .readTimeout(10000L, TimeUnit.MICROSECONDS).build();
        OkHttpUtils.initClient(okHttpClient);

        app = this;
        GlobalConfig.APP_Context = this;

        //获取本地参数信息
        GlobalParameter.initSharePreferences(getApplicationContext());

        //个推初始化
        GetuiUtil.initGetui(getApplicationContext());

        GlobalConfig.PackageName=getPackageName();

        ////////////////////////////////////////////////////
        //以下为mshare模块相关
        ////////////////////////////////////////////////////
        AutoLogin();
        GlobalConfig.display_width = getResources().getDisplayMetrics().widthPixels;

//        MapsInitializer.sdcardDir="";

        registerActivityLifecycleCallbacks(new SimpleActivityLifecycleCallback() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activityList.add(activity);

            }



            @Override
            public void onActivityDestroyed(Activity activity) {
                activityList.remove(activity);
            }
        });

        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());//捕获,未捕捉到的异常并处理;

    }

    //
    public void exit(){

        Iterator<Activity> iterator = activityList.iterator();
        while (iterator.hasNext()){

            Activity next = iterator.next();
            next.finish();
            iterator.remove();

        }
        activityList.clear();
        System.exit(0);
    }


    //// TODO: 2017/8/10 会有两个进程,那么他们会同时产生请求二次AutoLogin()方法 .可以优化成只请求一次!
    //默认直接自动登陆一次,同步一次默认登陆用户的登陆,认证,押金状态
    private void AutoLogin() {
        if(httpUtil==null)
            httpUtil=new HttpUtil();
        LoginResultBean result = UserDao.getDaoInstance(this).query();
        if(result==null)//如果当前没有默认要登陆的用户,即所有用户的isCurrentUser=false,不登陆!
            return;
        String[] params = new String[]{Constants.ECID, GlobalConfig.ECID,
                Constants.PhoneNumber, result.getPhoneNumber(), Constants.TempVerifyCode, "",
                Constants.AutoLoginKey, result.getAutoLoginKey(),
                Constants.AppType, GlobalConfig.AppType};
        Log.d("tag", "AutoLogin: params==="+params);
        httpUtil.callJson(handler, AUTOLOGIN, this, GlobalConfig.MSHARE_SERVER_ROOT + GlobalConfig.MSHARE_LOGIN,
                LoginResultBean.class, params);//todo 特别注意这一点,不同客户端对应不同的自动登陆地址,别忘了!

    }

}
