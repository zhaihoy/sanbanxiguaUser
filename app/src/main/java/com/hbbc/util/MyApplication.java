package com.hbbc.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.hbbc.mpush.GetuiUtil;
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

    private static MyApplication app;

    private List<Activity> activityList;
    private static Context mContext;

    public static MyApplication getInstance() {

        return app;
    }

    @Override
    public void onCreate() {

        super.onCreate();

        activityList = Collections.synchronizedList(new ArrayList<Activity>());//保存打开的所有activity
        mContext = getApplicationContext();

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

        GlobalConfig.PackageName = getPackageName();

        ////////////////////////////////////////////////////
        //以下为mshare模块相关
        ////////////////////////////////////////////////////
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
    public void exit() {
        Iterator<Activity> iterator = activityList.iterator();
        while (iterator.hasNext()) {

            Activity next = iterator.next();
            next.finish();
            iterator.remove();
        }
        activityList.clear();
        System.exit(0);
    }


    public static Context getContext(){

        return mContext;
    }


}
