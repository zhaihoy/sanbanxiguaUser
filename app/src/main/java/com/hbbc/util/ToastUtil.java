package com.hbbc.util;

import android.widget.Toast;

/**
 * Created by Administrator on 2017/8/1.
 *
 */
public class ToastUtil {

    /**
     * 用于Debug时的toast,上线时,更改GlobalConfig.Debug_Flag=false;
     * @param content
     */
    public static void toast_debug(String content){
        if(!GlobalConfig.ToastUtil_DebugFlag){
            return;
        }
        Toast.makeText(GlobalConfig.APP_Context,content, Toast.LENGTH_SHORT).show();
    }



    /**
     * 正常的toast.
     * @param content
     */
    public static void toast(String content){
        Toast.makeText(GlobalConfig.APP_Context, content, Toast.LENGTH_SHORT).show();
    }
}
