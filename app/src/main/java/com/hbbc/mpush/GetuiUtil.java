package com.hbbc.mpush;

import android.content.Context;

import com.igexin.sdk.PushManager;

/**
 * 个推初始化和注销
 */

public class GetuiUtil {


    private static Context context_gt;



    /**
     * 初始化个推
     */
    public static void initGetui(Context context) {
        //初始化推送
        PushManager.getInstance().initialize(context);
        context_gt = context;
    }



    /**
     * 获取ClientID
     */
    public static String getClientID() {
        return PushManager.getInstance().getClientid(context_gt);
    }



    /**
     * 开启个推
     */
    public static void turnonGetui() {
        //开启推送
        PushManager.getInstance().turnOnPush(context_gt);
    }



    /**
     * 判断个推是否开启
     */
    public static boolean isOpenGetui() {
        return PushManager.getInstance().isPushTurnedOn(context_gt);
    }



    /**
     * 关闭个推
     */
    public static void turnoffGetui() {
        //关闭个推
        PushManager.getInstance().turnOffPush(context_gt);
        //关闭个推服务
        PushManager.getInstance().stopService(context_gt);
    }

}
